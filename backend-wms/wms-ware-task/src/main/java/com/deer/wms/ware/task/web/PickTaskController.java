package com.deer.wms.ware.task.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.base.system.model.Area.AreaInfo;
import com.deer.wms.base.system.model.Cell.CellInfoDto;
import com.deer.wms.base.system.model.Item.ItemInfoDto;
import com.deer.wms.base.system.model.Pack.PackDetail;
import com.deer.wms.base.system.model.Shelf.ShelfInfo;
import com.deer.wms.base.system.service.AreaInfoService;
import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.base.system.service.PackDetailService;
import com.deer.wms.base.system.service.ShelfInfoService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.inventory.service.SerialNoService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.model.QrEntity;
import com.deer.wms.project.root.model.QrEntity.QrStringType;
import com.deer.wms.project.root.util.CacheTool;
import com.deer.wms.project.root.util.MyUtils;
import com.deer.wms.project.root.util.SimpleMap;
import com.deer.wms.project.root.util.StringUtil;
import com.deer.wms.ureport.service.ReportOperateService;
import com.deer.wms.ware.task.model.SO.SoDetail;
import com.deer.wms.ware.task.model.SO.SoMaster;
import com.deer.wms.ware.task.model.SO.SoMasterCriteria;
import com.deer.wms.ware.task.model.SO.SoMasterDto;
import com.deer.wms.ware.task.model.Wave.WaveMaster;
import com.deer.wms.ware.task.model.pickTask.CreatePickTask;
import com.deer.wms.ware.task.model.pickTask.CreatePickTaskDetail;
import com.deer.wms.ware.task.model.pickTask.PickParam;
import com.deer.wms.ware.task.model.pickTask.PickSkuQtyBySoParam;
import com.deer.wms.ware.task.model.pickTask.PickTask;
import com.deer.wms.ware.task.model.pickTask.PickTaskCriteria;
import com.deer.wms.ware.task.model.pickTask.PickTaskDto;
import com.deer.wms.ware.task.model.pickTask.PickTaskMoveCriteria;
import com.deer.wms.ware.task.model.pickTask.PickTaskResetParam;
import com.deer.wms.ware.task.model.pickTask.SeedingWallAlertInfo;
import com.deer.wms.ware.task.quartz.PassQ;
import com.deer.wms.ware.task.service.PickTaskService;
import com.deer.wms.ware.task.service.SoDetailService;
import com.deer.wms.ware.task.service.SoMasterService;
import com.deer.wms.ware.task.service.WaveMasterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.hutool.core.util.StrUtil;

/**
 * Created by guo on 2020/02/02.
 *
 * 拣货任务
 */
@RestController
@RequestMapping("/pick/tasks")
public class PickTaskController {

    @Autowired
    private PickTaskService pickTaskService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private SoDetailService soDetailService;
    @Autowired
    private SoMasterService soMasterService;
    @Autowired
    private PackDetailService packDetailService;
    @Autowired
    private ShelfInfoService shelfInfoService;
    @Autowired
    private AreaInfoService areaInfoService;
    @Autowired
    private ItemInfoService itemInfoService;
    @Autowired
    private SerialNoService serialNoService;

    @Autowired
    private ReportOperateService reportOperateService;

    @Autowired
    private CacheTool cache;

    @Autowired
    private PassQ passQ;


    @PostMapping
    @Authority("pick_tasks_add")
    public Result add(@RequestBody PickTask pickTask) {
        pickTaskService.save(pickTask);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 手动创建拣货任务
     * @param createPickTask
     * @return
     */
    @PostMapping("/manualCreatePickTask")
    @Authority("pick_tasks_manualCreatePickTask")
    public Result manualCreatePickTask(@RequestBody CreatePickTask createPickTask) {
        if (createPickTask.getBillNo().equals("") || createPickTask.getSoDetailId() == null || createPickTask.getCreatePickTaskDetails().size() <= 0) {
            throw new ServiceException(CommonCode.PARAMETER_ERROR);
        }
        List<PickTaskDto> list = pickTaskService.findList(new PickTaskCriteria(createPickTask.getSoDetailId(), 4));
        if (list.size() > 0) {
            throw new ServiceException(CommonCode.PICK_TASK_ALREADY_EXIST);
        }
        SoDetail soDetail = soDetailService.findById(createPickTask.getSoDetailId());
        SoMaster soMaster = soMasterService.findBy("billNo", createPickTask.getBillNo());
        // 分配数量
        double allottedQuantity = 0;
        for (CreatePickTaskDetail createPickTaskDetail : createPickTask.getCreatePickTaskDetails()) {
            Inventory inventory = inventoryService.findById(createPickTaskDetail.getInventoryId());
            // 发运单的单位
            PackDetail packDetail = packDetailService.findById(soDetail.getPackDetailId());
            // 拣货任务的单位
            PackDetail packDetailPick = packDetailService.findById(createPickTaskDetail.getPackDetailId());
            /**
             * 涉及到转换率
             */
            double quantity = MyUtils.calculateDivide(soDetail.getPackDetailId(), inventory.getPackDetailId(), createPickTaskDetail.getPickQuantity(), packDetail.getTransRatio());
            inventory.setAllotQuantity((inventory.getAllotQuantity() == null ? 0.0 : inventory.getAllotQuantity()) + quantity);
            // version
            inventoryService.update(inventory);
            CellInfoDto cellInfoDto = null;
            ShelfInfo shelfInfo = null;
            AreaInfo areaInfo = null;
            if (inventory.getCellCode() != null) {
                cellInfoDto = cellInfoService.findByCodeAndWareId(inventory.getCellCode(), inventory.getWareId());
            }
            if (cellInfoDto != null && cellInfoDto.getShelfInfoId() != null) {
                shelfInfo = shelfInfoService.findById(cellInfoDto.getShelfInfoId());
            }
            if (shelfInfo != null && shelfInfo.getAreaId() != null) {
                areaInfo = areaInfoService.findById(shelfInfo.getAreaId());
            }
            PickTask pickTask = new PickTask();
            pickTask.setSoDetailId(soDetail.getSoDetailId());
            pickTask.setSoMasterId(soMaster.getSoMasterId());
            pickTask.setWareId(soMaster.getWareId());
            pickTask.setWareName(soMaster.getWareName());
            pickTask.setOrganizationId(soMaster.getOrganizationId());
            pickTask.setOrganizationName(soMaster.getOrganizationName());
            pickTask.setAreaCode(areaInfo.getAreaCode());
            pickTask.setAreaName(areaInfo.getAreaName());
            pickTask.setShelfCode(shelfInfo.getShelfCode());
            pickTask.setShelfName(shelfInfo.getShelfName());
            pickTask.setCellCode(inventory.getCellCode());
            pickTask.setCellName(cellInfoDto.getCellName());
            pickTask.setItemCode(inventory.getItemCode());
            pickTask.setItemName(inventory.getItemName());
            pickTask.setBatchId(inventory.getBatchId());
            pickTask.setBatchName(inventory.getBatchName());
            pickTask.setBoxCode(inventory.getBoxCode());
            pickTask.setLpn(inventory.getLpn());
            pickTask.setPackDetailId(soDetail.getPackDetailId());
            pickTask.setPackDescribe(soDetail.getPackDescribe());
            pickTask.setPickQuantity(createPickTaskDetail.getPickQuantity());
            pickTask.setState(0);
            pickTask.setInventoryId(inventory.getInventoryId());
            pickTask.setSource(1);
            pickTask.setImgUrl(soDetail.getImgUrl());
            pickTask.setPackDetailId(packDetailPick.getPackDetailId());
            pickTask.setPackDescribe(packDetailPick.getPackDescribe());

            pickTaskService.save(pickTask);
            allottedQuantity += createPickTaskDetail.getPickQuantity();
        }
        soDetail.setAllottedQuantity(allottedQuantity + soDetail.getAllottedQuantity());
        soDetailService.update(soDetail);

        return ResultGenerator.genSuccessResult();
    }

//    @DeleteMapping("/{id}")
//    public Result delete(@PathVariable Integer id) {
//        pickTaskService.deleteById(id);
//        return ResultGenerator.genSuccessResult();
//    }

//    @PostMapping("/update")
//    public Result update(@RequestBody PickTask pickTask) {
//        pickTaskService.update(pickTask);
//        return ResultGenerator.genSuccessResult();
//    }

    @GetMapping("/{id}")
    @Authority("pick_tasks_get")
    public Result detail(@PathVariable Integer id) {
        PickTask pickTask = pickTaskService.findById(id);
        return ResultGenerator.genSuccessResult(pickTask);
    }

    @GetMapping("/list")
    @Authority("pick_tasks_list")
    public Result list(PickTaskCriteria criteria) {
        StringUtil.trimObjectStringProperties(criteria);
        // criteria.setWaveCode(criteria.getWaveCode().trim());
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<PickTaskDto> list = pickTaskService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据出库单号查询
     * @param criteria
     * @return
     */
    @GetMapping("/findBySoMasterId")
    @Authority("pick_tasks_findBySoMasterId")
    public Result findBySoMasterId(PickTaskCriteria criteria) {
        StringUtil.trimObjectStringProperties(criteria);
        // criteria.setWaveCode(criteria.getWaveCode().trim());
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<PickTaskDto> list = pickTaskService.findByBillNo(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 分配任务
     * @param criteria
     * @param currentUser
     * @return
     */
    @GetMapping("/allotTask")
    @Authority("pick_tasks_allotTask")
    public Result allotTask(PickTaskCriteria criteria, @User CurrentUser currentUser) {
        StringUtil.trimObjectStringProperties(criteria);
        // criteria.setWaveCode(criteria.getWaveCode().trim());
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        criteria.setUserId(currentUser.getUserId());
        List<PickTaskDto> list = pickTaskService.allotTask(criteria);
//        for (PickTaskDto pickTask : list) {
//            if (pickTask.getAllotUserId() == null) {
//                pickTask.setAllotTime(DateUtils.getNowDateTimeString());
//                pickTask.setAllotUserId(currentUser.getUserId());
//                pickTask.setAllotUserName(currentUser.getUserName());
//                pickTaskService.update(pickTask);
//            }
//        }
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

//    @GetMapping("/boxingList")
//    public Result boxingList(PickTaskCriteria criteria) {
//        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
//        List<PickTaskDto> list = new ArrayList<>();
//        if (criteria.getTag() == 1) {
//            list = pickTaskService.findBySo(criteria.getBillNo());
//        } else if (criteria.getTag() == 2) {
//            list = pickTaskService.findByWave(criteria.getWareId());
//        }
//        PageInfo pageInfo = new PageInfo(list);
//
//        return ResultGenerator.genSuccessResult(pageInfo);
//    }

    @PostMapping("/findFirst")
    @Authority("pick_tasks_findFirst")
    public Result findFirst(@RequestBody PickTaskCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<PickTaskDto> list = pickTaskService.findFirst(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 取消拣货任务
     * @param pickTaskCriteria
     * @return
     */
    @PostMapping("/cancelPickTask")
    @Transactional
    @Authority("pick_tasks_cancelPickTask")
    public Result cancelPickTask(@RequestBody PickTaskCriteria pickTaskCriteria) {
        List<PickTaskDto> list = pickTaskService.findList(pickTaskCriteria);
        for (PickTaskDto pickTask : list) {
            SoDetail soDetail = soDetailService.findById(pickTask.getSoDetailId());
            soDetail.setAllottedQuantity(soDetail.getAllottedQuantity() - pickTask.getPickQuantity());
            soDetailService.update(soDetail);
            pickTask.setState(3);
            pickTaskService.update(pickTask);
            Inventory inventory = inventoryService.findById(pickTask.getInventoryId());
            /**
             * 涉及到转换率
             */
            double quantity = MyUtils.calculateMultiplication(inventory.getPackDetailId(), pickTask.getPackDetailId(), pickTask.getPickQuantity(), pickTask.getTransRatio());
            inventory.setAllotQuantity(inventory.getAllotQuantity() - quantity);
            // version
            inventoryService.update(inventory);
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     *
     * @return
     */
    @GetMapping("/pickUpdateBill")
    @Transactional
    @Authority("pick_tasks_pickUpdateBill")
    public Result pickUpdateBill() {
        SoMasterCriteria soMasterCriteria = new SoMasterCriteria();
        soMasterCriteria.setState(4);
        List<SoMasterDto> soMasterDtos = soMasterService.findList(soMasterCriteria);

        for (SoMasterDto soMasterDto : soMasterDtos) {
            String billNo = soMasterDto.getBillNo();
            SoMaster soMaster = soMasterService.findBy("billNo", billNo);
            List<SoDetail> list = soDetailService.findByBillNo(billNo);
            Boolean isEnd = true;
            Integer soMasterState = 0;
            for (SoDetail detail : list) {
                if (detail.getState() == -1) {

                } else if (detail.getState() != 4 && detail.getState() != 5) {
                    isEnd = false;
                    break;
                } else {
                    soMasterState = detail.getState() + 2;
                }
            }
            if (isEnd) {
                soMaster.setState(soMasterState);
                soMasterService.updateState(soMaster);
            }
        }
        return ResultGenerator.genSuccessResult();
    }

    @Autowired
    private CellInfoService cellInfoService;

    @Autowired
    WaveMasterService waveMasterService;

    /**
     * 拣货方式1：确认拣货
     *
     * @param pickParam
     * @param currentUser
     * @return
     */
    @Authority("pick_tasks_pickTaskAffirm")
    @PostMapping("/pickTaskAffirm")
    public Result pickTaskAffirm(@RequestBody @Validated PickParam pickParam, @User CurrentUser currentUser) {
        pickParam.setUserId(currentUser.getUserId());
        pickParam.setUserName(currentUser.getUserName());
        pickTaskService.pick(0x0, pickParam);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 拣货方式2：边检边分
     *
     * @param pickParam
     * @param currentUser
     * @return
     */
    @Authority("pick_tasks_pickTaskBjbfAffirm")
    @PostMapping("/pickTaskBjbfAffirm")
    public Result pickTaskBjbfAffirm(@RequestBody @Validated PickParam pickParam, @User CurrentUser currentUser) {
        pickParam.setUserId(currentUser.getUserId());
        pickParam.setUserName(currentUser.getUserName());
        pickTaskService.pick(0x3, pickParam);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 拣货方式3：拣出SKU（支持拣货任务部分拣货）
     *
     * 按数量为订单（出库单）拣货，区别于pickTaskBjbfAffirm边拣边分：
     *
     * <pre>
     * 支持对拣货任务进行部分完成;
     * 不以拣货任务为单位，以SO+SKU为单位。提交上来数量，通过SO+SKU确定若干拣货任务，进行依次提交完成，超出数量时提示，否则都是可以正常推进的。
     *
     * 一般情况，作为“一拣一扫一提交”使用，也支持同SKU多Qty一次提交。
     * </pre>
     *
     * 最初需求源于：波次拣货
     * 
     * @param pickParam
     * @param currentUser
     * @return
     */
    @Authority("pick_tasks_pickTaskBjbfAffirmSo")
    @PostMapping("/pickTaskBjbfAffirmSo")
    public Result pickTaskBjbfAffirmSo(@RequestBody @Validated PickSkuQtyBySoParam pickParam, @User CurrentUser currentUser) {
        if (StrUtil.isNotBlank(pickParam.getQrCode())) {
            QrEntity qrEntity = QrEntity.stringToEntity(QrStringType.SIMPLE_STRING, pickParam.getQrCode());
            if (qrEntity != null && StrUtil.isNotBlank(qrEntity.getSo()) && StrUtil.isNotBlank(qrEntity.getSku())) {
                pickParam.setSoBillNo(qrEntity.getSo());
                pickParam.setItemCode(qrEntity.getSku());
                pickParam.setQrCode(qrEntity.entityToString(QrStringType.SIMPLE_STRING));
            } else {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "二维码解析错误");
            }
        }
        pickParam.setUserId(currentUser.getUserId());
        pickParam.setUserName(currentUser.getUserName());
        pickTaskService.pickAsSoAndSku(0x5, pickParam);
        SoMaster soMaster = soMasterService.findByBillNo(pickParam.getSoBillNo());
        ItemInfoDto itemInfo = itemInfoService.findByCode(pickParam.getItemCode(), soMaster.getOrganizationId());
        SimpleMap result = SimpleMap.init()
                .append("soMaster", soMaster)
                .append("itemInfo", itemInfo)
                ;
        return ResultGenerator.genSuccessResult(result);
    }

    /**
     * 确认异常
     *
     * @param
     * @param currentUser
     * @return
     */
    @GetMapping("/pickTaskEx")
    @Transactional
    @Authority("pick_tasks_pickTaskEx")
    public Result pickTaskEx(Integer waveMasterId, String boxCode, Integer wareId, Integer pickTaskId, String toCellCode, Double pickQuantity, Double exQuantity, @User CurrentUser currentUser) {
        pickTaskService.ex(waveMasterId, boxCode, wareId, pickTaskId, toCellCode, pickQuantity, exQuantity, currentUser);

        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/getPickTasksForSeeding")
    @Authority("pick_tasks_getPickTasksForSeeding")
    public Result getPickTasksForSeeding(String waveCode, @User CurrentUser currentUser) {

        PickTaskCriteria criteria = new PickTaskCriteria();
        criteria.setWaveCode(waveCode);
        List<PickTaskDto> list = pickTaskService.findList(criteria);
        return ResultGenerator.genSuccessResult(new PageInfo(list));
    }

    @GetMapping("/findByWaveMasterId")
    @Authority("pick_tasks_findByWaveMasterId")
    public Result findByWaveMasterId(PickTaskCriteria criteria, @User CurrentUser currentUser) {

        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());

        List<PickTaskDto> list = pickTaskService.findByWaveMasterId(criteria);
        return ResultGenerator.genSuccessResult(new PageInfo(list));
    }

    @GetMapping("/findRunningByBillNo")
    @Authority("pick_tasks_findRunningByBillNo")
    public Result findByBillNo(PickTaskCriteria criteria, @User CurrentUser currentUser) {

        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());

        List<PickTaskDto> list = pickTaskService.findRunningByBillNo(criteria);
        if (list!=null&&list.size()>0){
            for (PickTaskDto pickTaskDto : list) {
                if (pickTaskDto.getBillNo()==null || pickTaskDto.getBillNo().equals("")){
                    pickTaskDto.setBillNo(criteria.getBillNo());
                }
            }
        }
        return ResultGenerator.genSuccessResult(new PageInfo(list));
    }

    /**
     * 根据追踪号查询
     * @param criteria
     * @param currentUser
     * @return
     */
    @GetMapping("/findByBox")
    @Authority("pick_tasks_findByBox")
    public Result findByBox(PickTaskCriteria criteria, @User CurrentUser currentUser) {

        WaveMaster waveMaster = waveMasterService.findByBoxCode11(criteria.getBoxCode(), 1, criteria.getWareId(), currentUser.getUserId());
        if (waveMaster == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到拣货波次");
        }
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        criteria.setUserId(currentUser.getUserId());
        List<PickTaskDto> list = pickTaskService.findByBox(criteria);

        Map map = new HashMap();
        map.put("waveMaster", waveMaster);
        map.put("tasks", list);
        return ResultGenerator.genSuccessResult(map);
    }

    /**
     * 查询出库明细单播种信息
     * @param criteria
     * @param currentUser
     * @return
     */
    @PostMapping("/getSeedingWallAlertInfo")
    @Authority("pick_tasks_getSeedingWallAlertInfo")
    public Result getSeedingWallAlertInfo(@RequestBody PickTaskCriteria criteria, @User CurrentUser currentUser) {

        List<SeedingWallAlertInfo> list = pickTaskService.getSeedingWallAlertInfo(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Transactional
    @GetMapping("/updateSeededQuantity")
    @Authority("pick_tasks_updateSeededQuantity")
    public Result updateSeededQuantity(String soNo, String itemCode, Double quantity, Integer inventoryId, @User CurrentUser currentUser) {

        PickTaskCriteria taskCriteria = new PickTaskCriteria();
        taskCriteria.setBillNo(soNo);
        taskCriteria.setItemCode(itemCode);
        taskCriteria.setInventoryId(inventoryId);
        List<PickTaskDto> pickTasks = pickTaskService.findList(taskCriteria);
        for (PickTask pickTask : pickTasks) {
            Double pickQuantity = pickTask.getPickQuantity();
            Double seededQuantity = pickTask.getSeededQuantity();
            Double neededQuantity = pickQuantity - seededQuantity;
            if (quantity > neededQuantity) {
                pickTask.setSeededQuantity(pickQuantity);
                quantity -= neededQuantity;
            } else if (quantity <= neededQuantity) {
                pickTask.setSeededQuantity(seededQuantity + quantity);
                pickTaskService.update(pickTask);
                break;
            }
            pickTaskService.update(pickTask);
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 移动拣货任务列表
     * @return
     */
    @GetMapping("/pickTaskMoveList")
    @Authority("pick_tasks_pickTaskMoveList")
    public Result  pickTaskMoveList(PickTaskCriteria criteria){
        //查询未完成订单列表
        List<String> billNos;
        billNos = passQ.getMissedBillNos(null).getUnFinishedNos();
        if(billNos==null){
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未查询到未完成订单，或未进行订单同步！");
        }
        criteria.setBillNos(billNos);
        List<PickTaskDto> list = pickTaskService.pickTaskMoveList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);

    }

    /**
     * 移动拣货任务
     * @return
     */
    @PostMapping("/pickTaskMove")
    @Authority("pick_tasks_pickTaskMove")
    public Result  pickTaskMove(@RequestBody PickTaskMoveCriteria criteria,@User CurrentUser currentUser){
        pickTaskService.pickTaskMove(criteria, currentUser);
        return ResultGenerator.genSuccessResult();

    }

    /**
     * 拣货任务重新分配
     */
    @PostMapping("/pickTaskReset")
    @Authority("pick_tasks_pickTaskReset")
    public Result pickTaskReset(@RequestBody PickTaskResetParam param, @User CurrentUser currentUser) {
        param.setUserId(currentUser.getUserId());
        param.setUsername(currentUser.getUserName());
        pickTaskService.pickTaskReset(param);
        return ResultGenerator.genSuccessResult();
    }
}
