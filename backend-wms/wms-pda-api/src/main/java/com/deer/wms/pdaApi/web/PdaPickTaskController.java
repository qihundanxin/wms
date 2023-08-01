package com.deer.wms.pdaApi.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.deer.wms.base.system.model.Item.ItemInfo;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.file.constant.FileUseType;
import com.deer.wms.file.util.FileS3Tool;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.PdaUser;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.configurer.ApiResultPacking;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.model.QrEntity;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.project.root.util.SimpleMap;
import com.deer.wms.ware.task.model.pickTask.MyBatchKanbanRespVO;
import com.deer.wms.ware.task.model.pickTask.PickBatch;
import com.deer.wms.ware.task.model.pickTask.PickBatchDetail;
import com.deer.wms.ware.task.model.pickTask.PickBatchDto;
import com.deer.wms.ware.task.model.pickTask.PickBatchException;
import com.deer.wms.ware.task.model.pickTask.PickBatchItem;
import com.deer.wms.ware.task.model.pickTask.PickBatchItemDto;
import com.deer.wms.ware.task.model.pickTask.PickBatchParam;
import com.deer.wms.ware.task.model.pickTask.PickBatchSearchParam;
import com.deer.wms.ware.task.service.PickBatchDetailService;
import com.deer.wms.ware.task.service.PickBatchExceptionService;
import com.deer.wms.ware.task.service.PickBatchItemService;
import com.deer.wms.ware.task.service.PickBatchService;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApiResultPacking
@RestController
@RequestMapping("/pda/pickTask")
@RequiredArgsConstructor
@Slf4j
public class PdaPickTaskController {

    @Autowired
    private PickBatchService pickBatchService;
    @Autowired
    private PickBatchItemService pickBatchItemService;
    @Autowired
    private PickBatchDetailService pickBatchDetailService;
    @Autowired
    private ItemInfoService itemInfoService;
    @Autowired
    private PickBatchExceptionService pickBatchExceptionService;
    @Autowired
    private FileS3Tool fileS3Tool;

    @Data
    public static class PickBatchFromVo{

        /**
         * 波次编号
         **/
        @NotBlank
        private String pickBatchNo;

        /**
         * 货位编号
         **/
        @NotBlank
        private String cellCode;

        /**
         * 二维码
         **/
        @NotBlank
        private String qrCode;

        /**
         * 拣货数量
         **/
        @NotNull
        @Min(1)
        private Double qty;

        /**
         * 质量/重量（kg）
         **/
        private BigDecimal weight;

        /**
         * 磅数
         **/
        private BigDecimal pound;
    }

    /**
     * 单一波次拣货
     * @author luolin
     * @param vo
     * @param currentUser 当前登录用户
     * @return
     **/
    @PostMapping("/pickBatch")
    @Authority("pda_pickTask_pickBatch")
    public void pickBatch(@RequestBody @Validated PickBatchFromVo vo, @PdaUser CurrentUser currentUser){
        PickBatch pickBatch = pickBatchService.getByNo(vo.getPickBatchNo());
        if (pickBatch == null){
            throw new ServiceException(CommonCode.SERVICE_ERROR, "波次号是" + vo.getPickBatchNo() + "的波次不存在");
        }
        if (!currentUser.getUserId().equals(pickBatch.getPickUserId())){
            throw new ServiceException(CommonCode.SERVICE_ERROR, "此波次不属于您");
        }
        PickBatchParam pickParam = new PickBatchParam();
        if (StrUtil.isNotBlank(vo.getQrCode())) {
            QrEntity qrEntity = QrEntity.stringToEntity(QrEntity.QrStringType.SIMPLE_STRING, vo.getQrCode());
            if (qrEntity != null && StrUtil.isNotBlank(qrEntity.getSo()) && StrUtil.isNotBlank(qrEntity.getSku())) {
                pickParam.setPickBatchNo(vo.getPickBatchNo());
                pickParam.setQrCode(qrEntity.entityToString(QrEntity.QrStringType.SIMPLE_STRING));
                pickParam.setCellCode(vo.getCellCode());
                pickParam.setSoBillNo(qrEntity.getSo());
                pickParam.setItemCode(qrEntity.getSku());
            } else {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "二维码解析错误");
            }
            ItemInfo itemInfo = itemInfoService.findByItemCode(qrEntity.getSku());
            if (itemInfo == null) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "二维码异常：此SKU不存在，或已被WMS系统删除");
            }
            if (vo.getQty() < 1){
                throw new ServiceException(CommonCode.SERVICE_ERROR, "拣货数量最小是1");
            }
            if (itemInfo.getWeighting()){
                if (vo.getQty() > 1){
                    throw new ServiceException(CommonCode.SERVICE_ERROR, "需要称重的SKU,每次拣货数量只能是1");
                }
                if (vo.getWeight() == null && vo.getPound() == null){
                    throw new ServiceException(CommonCode.SERVICE_ERROR, "需要称重，weight和pound至少传一项");
                }
                pickParam.setWeight(vo.getWeight());
                pickParam.setPound(vo.getPound());
            }else {
                if (!Np.i(pickBatch.getWholesale()).o(false)){
                    if (vo.getQty() > 1){
                        throw new ServiceException(CommonCode.SERVICE_ERROR, "非批发单每次拣货数量只能是1");
                    }
                }
            }
        }
        pickParam.setWholesale(Np.i(pickBatch.getWholesale()).o(false));
        pickParam.setUserId(currentUser.getUserId());
        pickParam.setUserName(currentUser.getUserName());
        pickParam.setReviewQuantity(vo.qty);
        pickBatchService.pickBatch(pickParam);
    }

    @Data
    public static class PickBatchListFromVo{

        /**
         * 二维码
         **/
        @Size(min = 1)
        @Valid
        private List<PickBatchFromVo> pickList;
    }

    /**
     * 批量波次拣货
     * @author luolin
     * @param vo
     * @param currentUser 当前登录用户
     * @return com.deer.wms.project.root.util.SimpleMap
     **/
    @PostMapping("/pickBatchList")
    @Authority("pda_pickTask_pickBatchList")
    public void pickBatchList(@RequestBody @Validated PickBatchListFromVo vo, @PdaUser CurrentUser currentUser){
        List<SimpleMap> successList = new ArrayList<>();
        List<SimpleMap> failList = new ArrayList<>();
        for (PickBatchFromVo item : vo.getPickList()){
            PickBatchParam pickParam = new PickBatchParam();
            if (StrUtil.isNotBlank(item.getQrCode())) {
                QrEntity qrEntity = QrEntity.stringToEntity(QrEntity.QrStringType.SIMPLE_STRING, item.getQrCode());
                if (qrEntity != null && StrUtil.isNotBlank(qrEntity.getSo()) && StrUtil.isNotBlank(qrEntity.getSku())) {
                    pickParam.setPickBatchNo(item.getPickBatchNo());
                    pickParam.setQrCode(qrEntity.entityToString(QrEntity.QrStringType.SIMPLE_STRING));
                    pickParam.setCellCode(item.getCellCode());
                    pickParam.setSoBillNo(qrEntity.getSo());
                    pickParam.setItemCode(qrEntity.getSku());
                } else {
                    failList.add(SimpleMap.init().append("qrCode", item.getQrCode()).append("message", "二维码解析错误"));
                    continue;
                }
                PickBatch pickBatch = pickBatchService.getByNo(item.getPickBatchNo());
                if (pickBatch == null){
                    failList.add(SimpleMap.init().append("qrCode", pickParam.getQrCode()).append("message", "波次号是" + item.getPickBatchNo() + "的波次不存在"));
                    continue;
                }
                if (!currentUser.getUserId().equals(pickBatch.getPickUserId())){
                    failList.add(SimpleMap.init().append("qrCode", pickParam.getQrCode()).append("message", "此波次不属于您"));
                    continue;
                }
                pickParam.setWholesale(Np.i(pickBatch.getWholesale()).o(false));
                ItemInfo itemInfo = itemInfoService.findByItemCode(qrEntity.getSku());
                if (itemInfo == null) {
                    throw new ServiceException(CommonCode.SERVICE_ERROR, "二维码异常：此SKU不存在，或已被WMS系统删除");
                }
                if (item.getQty() < 1){
                    failList.add(SimpleMap.init().append("qrCode", pickParam.getQrCode()).append("message", "拣货数量最小是1"));
                    continue;
                }
                if (itemInfo.getWeighting()){
                    if (item.getQty() > 1){
                        failList.add(SimpleMap.init().append("qrCode", pickParam.getQrCode()).append("message", "需要称重的SKU,每次拣货数量只能是1"));
                        continue;
                    }
                    if (item.getWeight() == null && item.getPound() == null){
                        failList.add(SimpleMap.init().append("qrCode", pickParam.getQrCode()).append("message", "需要称重，weight和pound至少传一项"));
                        continue;
                    }
                    pickParam.setWeight(item.getWeight());
                    pickParam.setPound(item.getPound());
                }else {
                    if (!Np.i(pickBatch.getWholesale()).o(false)){
                        if (item.getQty() > 1){
                            failList.add(SimpleMap.init().append("qrCode", pickParam.getQrCode()).append("message", "非批发单每次拣货数量只能是1"));
                            continue;
                        }
                    }
                }
            }
            pickParam.setUserId(currentUser.getUserId());
            pickParam.setUserName(currentUser.getUserName());
            pickParam.setReviewQuantity(item.qty);
            try {
                pickBatchService.pickBatch(pickParam);
                successList.add(SimpleMap.init().append("qrCode", pickParam.getQrCode()));
            } catch (ServiceException e) {
                failList.add(SimpleMap.init().append("qrCode", pickParam.getQrCode()).append("message", e.getMessage()));
            }catch (Exception e){
                log.error("波次拣货异常", e);
                failList.add(SimpleMap.init().append("qrCode", pickParam.getQrCode()).append("message", "系统错误"));
            }
        }
        if (failList.size() != 0){
            SimpleMap simpleMap = SimpleMap.init().append("successList", successList).append("failList", failList);
            throw new ServiceException(CommonCode.PICK_BATCH_ERROR, "成功拣货" + successList.size() + "件SKU，失败" + failList.size() + "件SKU", simpleMap);
        }
    }

    @Data
    public static class PickBatchWholesaleFromVo{

        /**
         * 波次编号
         **/
        @NotBlank
        private String pickBatchNo;

        /**
         * 货位编号
         **/
        @NotBlank
        private String cellCode;

        /**
         * 二维码
         **/
        @NotBlank
        private String qrCode;

        /**
         * 拣货数量
         **/
        @NotNull
        private Double qty;
    }

    /**
     * 批发单拣货
     * @author luolin
     * @param vo
     * @param currentUser 当前登录用户
     * @return
     **/
    @PostMapping("/pickBatchWholesale")
    @Authority("pda_pickTask_pickBatchWholesale")
    public void pickBatchWholesale(@RequestBody @Validated PickBatchWholesaleFromVo vo, @PdaUser CurrentUser currentUser){
        PickBatch pickBatch = pickBatchService.getByNo(vo.getPickBatchNo());
        if (pickBatch == null){
            throw new ServiceException(CommonCode.SERVICE_ERROR, "波次号是" + vo.getPickBatchNo() + "的波次不存在");
        }
        if (!Np.i(pickBatch.getWholesale()).o(false)){
            throw new ServiceException(CommonCode.SERVICE_ERROR, "此波次不是批发单");
        }
        if (!currentUser.getUserId().equals(pickBatch.getPickUserId())){
            throw new ServiceException(CommonCode.SERVICE_ERROR, "此波次不属于您");
        }
        PickBatchParam pickParam = new PickBatchParam();
        if (StrUtil.isNotBlank(vo.getQrCode())) {
            QrEntity qrEntity = QrEntity.stringToEntity(QrEntity.QrStringType.SIMPLE_STRING, vo.getQrCode());
            if (qrEntity != null && StrUtil.isNotBlank(qrEntity.getSo()) && StrUtil.isNotBlank(qrEntity.getSku())) {
                pickParam.setPickBatchNo(vo.getPickBatchNo());
                pickParam.setQrCode(qrEntity.entityToString(QrEntity.QrStringType.SIMPLE_STRING));
                pickParam.setCellCode(vo.getCellCode());
                pickParam.setSoBillNo(qrEntity.getSo());
                pickParam.setItemCode(qrEntity.getSku());
                pickParam.setReviewQuantity(vo.getQty());
            } else {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "二维码解析错误");
            }
        }
        pickParam.setUserId(currentUser.getUserId());
        pickParam.setUserName(currentUser.getUserName());
        pickBatchService.pickBatchWholesale(0x5, pickParam);
    }


    @Data
    public static class GetMyBatchKanbanParam{
        // 时间 (选填，默认今天)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private Date date;
    }

    /**
     * 我的波次拣货看板
     * 
     * @author han.li
     * @date 2023/07/14 16:56
     * @param param
     * @param currentUser
     * @return com.deer.wms.pdaApi.web.PdaPickTaskController.MyBatchKanbanRespVO
     */
    @PostMapping("/getMyBatchKanban")
    @Authority("pda_pickTask_getMyBatchKanban")
    public MyBatchKanbanRespVO getMyBatchKanban(@RequestBody @Validated GetMyBatchKanbanParam param, @PdaUser CurrentUser currentUser) {
        if (param.getDate() == null) {// 默认今天
            param.setDate(DateUtils.now());
        }
        Date date = param.getDate();
        Date startDate = DateUtils.getDateStartTime(date);
        Date endDate = DateUtils.addDay(startDate, 1);

        // 返回数据源
        MyBatchKanbanRespVO result = pickBatchService.getBatchKanbanByDate(startDate, endDate, currentUser.getUserId());
        result.setDatetime(date);
        return result;
    }


    @Data
    public static class GetMyBatchListParam{
        // 时间 (选填，默认今天)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private Date date;
        // 状态，0未开始 1部分拣货 2 拣货完成
        private Integer status;
    }

    @Data
    public static class GetMyBatchListRespVO{
        // 时间 (选填，默认今天)
        private Date datetime;
        private List<PickBatchDto> pickBatchList;
    }

    /**
     * 获取我的波次列表
     * @author han.li
     * @date 2023/07/18 08:56
     * @param param
     * @param currentUser
     * @return com.deer.wms.ware.task.model.pickTask.MyBatchKanbanRespVO
     */
    @PostMapping("/getMyBatchList")
    @Authority("pda_pickTask_getMyBatchList")
    public GetMyBatchListRespVO getMyBatchList(@RequestBody @Validated GetMyBatchListParam param, @PdaUser CurrentUser currentUser){
        if (param.getDate() == null) {// 默认今天
            param.setDate(DateUtils.now());
        }
        Date date = param.getDate();
        Date startDate = DateUtils.getDateStartTime(date);
        Date endDate = DateUtils.addDay(startDate, 1);

        PickBatchSearchParam criteria = new PickBatchSearchParam();
        criteria.setTaskTimeFrom(startDate);
        criteria.setTaskTimeTo(endDate);
        criteria.setPickUserId(currentUser.getUserId());
        criteria.setStatus(param.getStatus());
        List<String> sorts = new ArrayList<>();
        //app查询我的波次列表排序规则
        sorts.add("appSelect");
        criteria.setSorts(sorts);
        // 返回数据源
        GetMyBatchListRespVO respVO = new GetMyBatchListRespVO();
        respVO.setDatetime(date);
        List<PickBatchDto> list = pickBatchService.getBatchDtoList(criteria);
        respVO.setPickBatchList(list);
        return respVO;
    }

    @Data
    public static class TagBatchParam{
        @NotNull
        private String pickBatchNo ;
    }
    /**
     * 为我标记了一个波次
     * @author han.li
     * @date 2023/07/18 17:28
     * @param param
     * @param currentUser
     * @return com.deer.wms.ware.task.model.pickTask.PickBatchDto
     */
    @PostMapping("/tagBatch")
    @Authority("pda_pickTask_tagBatch")
    public PickBatchDto tagBatch(@RequestBody @Validated TagBatchParam param, @PdaUser CurrentUser currentUser){
        return pickBatchService.tagBatch(param.getPickBatchNo(),currentUser.getUserId());
    }

    @Data
    public static class GetBatchParam{
        private Long id;
        private String pickBatchNo ;
    }
    
    @Data
    public static class GetBatchResult {
        /** ID */
        private Long id;
        /** 波次组编号 */
        private String pickBatchGroupNo;
        /** 波次编号 */
        private String pickBatchNo;
        /** 0未开始 1部分拣货 2 拣货完成 */
        private Integer status;
        /** 创建时间 */
        private String createTime;
        /** 波次包含N种SKU */
        private Integer skuNum;
        /** 商品总数量 */
        private Double skuQty;
        /** 未拣货数量 */
        private Double unPickedQty;
        /** 已拣货数量 */
        private Double pickedQty;
        /** 异常数量 */
        private Double exceptionQty;
        /** 司机姓名 */
        private String driverName;
        /** 拣货人ID，当前波次任务归属的人员ID */
        private Integer pickUserId;
        /** 本波次涉及的货货位数 */
        private Integer cellNum;
        /** 任务分配、领取时间 */
        private Date taskTime;
        /** 最后拣货时间 */
        private Date pickTime;
        /** 干货数量 */
        private Double scbSkuQty;
        /** 冷货数量 */
        private Double coldSkuQty;
        /** 批发单 */
        private Boolean wholesale;
        /** 出库单号 */
        private String soBillNo;
        /** item列表（一物一码） */
        private List<PickBatchItemDto> batchItemList;
        /** detail列表（SKU+货位汇总） */
        private List<GetBatchDetailResult> batchDetailList;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class GetBatchDetailResult extends PickBatchDetail {
        /** 商品图片 */
        private String imgUrl;
        /** 需要称重 */
        private Boolean weighting;
        /** 散件（需要拆包装） */
        private Boolean unpacking;
        /** Item二维码 */
        private String qrCode;
        /** 出库单号 */
        private String soBillNo;
        /** 数量 */
        private Double qty;
        /** 异常数量 */
        private Double exceptionalQty;
        /** 未拣货数量 */
        private Double pickingQty;
        /** 已拣货数量 */
        private Double pickedQty;
    }

    /**
     * ID查询波次（一物一码）（用于波次拣货）
     * @author han.li
     * @date 2023/07/19 14:20
     * @param param
     * @param currentUser
     * @return com.deer.wms.ware.task.model.pickTask.PickBatchItemResponseVO
     */
    @PostMapping("/getBatchById")
    @Authority("pda_pickTask_getBatchById")
    public GetBatchResult getBatchById(@RequestBody @Validated GetBatchParam param, @PdaUser CurrentUser currentUser) {
        GetBatchResult result = new GetBatchResult();
        PickBatch pickBatch = null;
        if (param.getId() != null) {
            pickBatch = pickBatchService.getById(param.getId());
        } else if (StrUtil.isNotBlank(param.getPickBatchNo())) {
            pickBatch = pickBatchService.getByNo(param.getPickBatchNo());
        } else {
            throw new ServiceException(CommonCode.PARAMETER_ERROR, "id与pickBatchNo至少填一项");
        }
        if (pickBatch == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到该波次");
        }

        PickBatchDto dto = pickBatchService.entityToDto(pickBatch);
        BeanUtils.copyBeanProp(result, dto);
        result.setBatchItemList(new ArrayList<>());
        List<PickBatchItem> itemList = pickBatchItemService.getByPickBatchNo(pickBatch.getPickBatchNo());
        for (PickBatchItem pickBatchItem : itemList) {
            PickBatchItemDto itemDto = pickBatchItemService.entityToDto(pickBatchItem);
            itemDto.setItemName(itemInfoService.getItemNameI18n(itemDto.getItemName()));
            result.getBatchItemList().add(itemDto);
        }
        return result;
    }


    /**
     * ID查询波次（货位SKU汇总）（用于批发单拣货）
     * @author han.li
     * @date 2023/07/19 14:20
     * @param param
     * @param currentUser
     * @return com.deer.wms.ware.task.model.pickTask.PickBatchWholesaleResponseVO
     */
    @PostMapping("/getBatchWholesaleById")
    @Authority("pda_pickTask_getBatchWholesaleById")
    public GetBatchResult getBatchWholesaleById(@RequestBody @Validated GetBatchParam param, @PdaUser CurrentUser currentUser) {
        GetBatchResult result = new GetBatchResult();
        PickBatch pickBatch = null;
        if (param.getId() != null) {
            pickBatch = pickBatchService.getById(param.getId());
        } else if (StrUtil.isNotBlank(param.getPickBatchNo())) {
            pickBatch = pickBatchService.getByNo(param.getPickBatchNo());
        } else {
            throw new ServiceException(CommonCode.PARAMETER_ERROR, "id与pickBatchNo至少填一项");
        }
        if (pickBatch == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到该波次");
        }
        if (pickBatch.getWholesale() == false) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "此波次不是批发单");
        }

        PickBatchDto dto = pickBatchService.entityToDto(pickBatch);
        BeanUtils.copyBeanProp(result, dto);
        result.setBatchDetailList(new ArrayList<>());
        List<PickBatchDetail> detailList = pickBatchDetailService.findByPickBatchNo(pickBatch.getPickBatchNo());
        for (PickBatchDetail detail : detailList) {
            GetBatchDetailResult detailResult = new GetBatchDetailResult();
            BeanUtils.copyBeanProp(detailResult, detail);
            detailResult.setItemName(itemInfoService.getItemNameI18n(detailResult.getItemName()));
            detailResult.setQty(detail.getItemQty());
            detailResult.setPickedQty(detail.getReviewQty());
            detailResult.setPickingQty(detail.getItemQty() - detail.getReviewQty() - Np.i(detail.getExceptionQty()).o(0D));
            detailResult.setExceptionalQty(detail.getExceptionQty());
            ItemInfo itemInfo = itemInfoService.findByItemCode(detail.getItemCode());
            detailResult.setImgUrl(Np.i(itemInfo.getImgUrl()).o(null));
            detailResult.setWeighting(itemInfo.getWeighting());
            detailResult.setUnpacking(itemInfo.getUnpacking());
            List<PickBatchItem> pickBatchItem = pickBatchItemService.getByDetailId(detail.getId());
            detailResult.setQrCode(pickBatchItem.get(0).getQrCode());
            detailResult.setSoBillNo(pickBatchItem.get(0).getSoBillNo());
            result.getBatchDetailList().add(detailResult);
        }
        return result;
    }

    @Data
    public static class  GetItemParam{
        @NotBlank
        private String pickBatchNo;
        @NotBlank
        private String cellCode;
        @NotBlank
        private String qrCode;
    }
    /**
     * QrCode查询波次Item（单一拣货标签上的信息）
     * @author han.li
     * @date 2023/07/19 14:22
     * @param param
     * @param currentUser
     * @return com.deer.wms.ware.task.model.pickTask.PickBatchItemDto
     */
    @PostMapping("/getItemByQrCode")
    @Authority("pda_pickTask_getItemByQrCode")
    public PickBatchItemDto getItemByQrCode(@RequestBody @Validated GetItemParam param, @PdaUser CurrentUser currentUser){
        PickBatchItem item = pickBatchItemService.getByQrCode(param.getQrCode());
        if (item == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "没有查询到此拣货任务");
        }
        if (!item.getCellCode().equals(param.getCellCode())) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "此拣货任务不在此货位上");
        }
        if (!item.getPickBatchNo().equals(param.getPickBatchNo())) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "此拣货任务不在此波次中");
        }
        PickBatchItemDto result = pickBatchItemService.entityToDto(item);
        result.setItemCode(itemInfoService.getItemNameI18n(result.getItemName()));
        return result;
    }

    @Data
    public static class ReportParam{

        /** 波次号 **/
        @NotBlank
        private String pickBatchNo;

        /** 货位编号 **/
        @NotBlank
        private String cellCode;

        /** sku **/
        @NotBlank
        private String itemCode;

        /** 图片地址 **/
        @Size(max = 9)
        private List<String> imgUrl;

        /** 异常数量 **/
        @NotNull
        @Min(1)
        private Double exceptionQty;

        /** 异常类型 **/
        @NotNull
        private Integer exceptionType;

        /** 描述 **/
        @Size(max = 255)
        private String description;
    }

    /**
     * 波次拣货上报异常
     * @author luolin
     * @param param
     * @param currentUser
     * @return
     **/
    @PostMapping("/reportBatchException")
    @Authority("pda_pickTask_reportBatchException")
    public void reportBatchException(@RequestBody @Validated ReportParam param, @PdaUser CurrentUser currentUser){
        PickBatchException pickBatchException = new PickBatchException();
        BeanUtils.copyProperties(param, pickBatchException);
        pickBatchException.setReportUserId(currentUser.getUserId());
        pickBatchException.setReportTime(DateUtils.now());
        pickBatchException.setStatus(0);
        StringBuffer urlBuffer = new StringBuffer();
        if (param.getImgUrl() != null && param.getImgUrl().size() > 0){
            for (String str : param.getImgUrl()){
                urlBuffer.append(str + ",");
            }
        }
        if (urlBuffer.length() > 0){
            String imgUrl = urlBuffer.toString();
            imgUrl = imgUrl.substring(0, imgUrl.lastIndexOf(","));
            pickBatchException.setImgUrl(imgUrl);
        }
        pickBatchExceptionService.reportBatchException(pickBatchException);
    }

    @Data
    public static class PickBatchExceptionResult{

        private String cellCode;

        private String itemCode;

        private Double exceptionQty;

    }

    /**
     * 波次拣货异常图片上传
     * @author luolin
     * @param multipartFile
     * @return com.deer.wms.project.root.util.SimpleMap
     **/
    @PostMapping("/uploadExcption")
    @Authority("pda_pickTask_uploadExcption")
    public SimpleMap uploadExcption(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        if (multipartFile.getSize() > 10 * 1024 * 1024) {
            throw new ServiceException(CommonCode.UPLOAD_FILE_ERROR,"单一文件大小不超过10MB");
        }
        //获取文件名
        String fileName = multipartFile.getOriginalFilename();
        //检查文件拓展名
        String extName = FileNameUtil.extName(fileName);
        if (!extName.equals("png")&&!extName.equals("jpg")&&!extName.equals("jpeg")){
            throw new ServiceException(CommonCode.PARAMETER_ERROR, "传文件格式错误，请上传PNG、JPG、JPEG、PDF格式");
        }
        String fullPath = fileS3Tool.save(FileUseType.UPLOAD_PDA_PICK_BATCH_EXCEPTION, multipartFile);
        return SimpleMap.init().append("fullPath", fullPath);
    }
}
