package com.deer.wms.ware.task.web;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.base.system.model.ItemCombined.ItemCombined;
import com.deer.wms.base.system.model.ItemCombined.ItemComponent;
import com.deer.wms.base.system.model.ItemCombined.ItemComponentCriteria;
import com.deer.wms.base.system.service.ItemCombinedService;
import com.deer.wms.base.system.service.ItemComponentService;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.file.configurer.FileSetting;
import com.deer.wms.file.constant.FileUseType;
import com.deer.wms.file.util.FileS3Tool;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.BillNoUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.ware.task.model.ManufactureBill;
import com.deer.wms.ware.task.model.SoDetailStockoutInfo;
import com.deer.wms.ware.task.model.SO.SoDayList;
import com.deer.wms.ware.task.model.SO.SoDetail;
import com.deer.wms.ware.task.model.SO.SoDetailCriteria;
import com.deer.wms.ware.task.model.SO.SoDetailDto;
import com.deer.wms.ware.task.model.SO.SoMaster;
import com.deer.wms.ware.task.service.ManufactureBillService;
import com.deer.wms.ware.task.service.SoDetailService;
import com.deer.wms.ware.task.service.SoMasterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * SO详细信息api接口
 * 
 * Created by guo on 2020/02/02.
 */
@RestController
@RequestMapping("/so/details")
public class SoDetailController {

    @Autowired
    private SoDetailService soDetailService;

    @Autowired
    private FileSetting fileSetting;
    @Autowired
    private FileS3Tool fileS3Tool;
    @Autowired
    private SoMasterService soMasterService;
    @Autowired
    private ItemInfoService itemInfoService;
    @Autowired
    private ItemCombinedService itemCombinedService;
    @Autowired
    private ItemComponentService itemComponentService;
    @Autowired
    private ManufactureBillService manufactureBillService;

    @PostMapping("/insert")
    @Authority("so_details_insert")
    public Result add(@RequestBody SoDetail soDetail) {
        soDetailService.save(soDetail);
        return ResultGenerator.genSuccessResult();
    }

//    @GetMapping("/delete")
//    public Result delete(@PathVariable Integer id) {
//        soDetailService.deleteById(id);
//        return ResultGenerator.genSuccessResult();
//    }

    @PostMapping("/update")
    @Authority("so_details_update")
    public Result update(@RequestBody SoDetail soDetail) {
        soDetailService.update(soDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("so_details_detail")
    public Result detail(@PathVariable Integer id) {
        SoDetail soDetail = soDetailService.findById(id);
        return ResultGenerator.genSuccessResult(soDetail);
    }

    @GetMapping("/list")
    @Authority("so_details_list")
    public Result list(SoDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SoDetailDto> list = soDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    
    /**
     * 查询SO详情的缺货信息
     */
    @GetMapping("/StockoutInfo")
    @Authority("so_details_StockoutInfo")
    public Result getStockoutInfo(@RequestParam("billNo") String billNo) {
        List<SoDetailStockoutInfo> result = soDetailService.getStockoutInfo(billNo);
        PageInfo<SoDetailStockoutInfo> pageInfo = new PageInfo<>(result);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/findDetailsByWaveId")
    @Authority("so_details_findDetailsByWaveId")
    public Result findDetailsByWaveId(Integer wareId, Integer waveId) {
        List<SoDetailDto> list = soDetailService.findDetailsByWaveId(wareId, waveId);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 出库日报表
     */
    @GetMapping("/findDayList")
    @Authority("so_details_findDayList")
    public Result findDayList(SoDetailCriteria criteria, @User CurrentUser currentUser) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SoDayList> list = soDetailService.findDayList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 出库日报表导出
     */
    @GetMapping("/exportDayList")
    @Authority("so_details_exportDayList")
    public Result exportDayList(SoDetailCriteria criteria, @User CurrentUser currentUser) {
        criteria.setPageNum(1);
        criteria.setPageSize(10000);

        List<SoDayList> list = soDetailService.findDayList(criteria);

        String staticPath = fileSetting.getStaticFileDir();
        String downloadPath = fileSetting.getDownloadPath() + "/soDayList" + DateUtils.dateToStr(new Date(), "yyyyMMddHHmmss") + ".xls";
        String modelPath = fileSetting.getModelPath() + "/soDayListModel.xls";
        try {
            String modelFilePath = staticPath + modelPath;
            String filePath = staticPath + downloadPath;

            jxl.Workbook workbook = jxl.Workbook.getWorkbook(new java.io.File(modelFilePath));
            WritableWorkbook copy = jxl.Workbook.createWorkbook(new java.io.File(filePath), workbook);

            WritableSheet wSheet = copy.getSheet(0);
            CellFormat format = wSheet.getCell(1, 1).getCellFormat();
            DecimalFormat two = new DecimalFormat("#.#####");

            Double sum = 0.0;
            for (int i = 0; i < list.size(); i++) {

                wSheet.insertRow(i + 2);

                SoDayList soDayList = list.get(i);

                String indexStr = Integer.toString(i + 1);
                String wareName = "-";
                if (soDayList.getWareName() != null) {
                    wareName = soDayList.getWareName();
                }
                String organizationName = "-";
                if (soDayList.getOrganizationName() != null) {
                    organizationName = soDayList.getOrganizationName();
                }

                String itemName = "-";
                if (soDayList.getItemName() != null) {
                    itemName = soDayList.getItemName();
                }
                String itemCode = "-";
                if (soDayList.getItemCode() != null) {
                    itemCode = soDayList.getItemCode();
                }

                String packDescribe = "-";
                if (soDayList.getPackDescribe() != null) {
                    packDescribe = soDayList.getPackDescribe();
                }

                String deliveryQuantity = "-";
                if (soDayList.getDeliveryQuantity() != null) {
                    deliveryQuantity = two.format(soDayList.getDeliveryQuantity());
                    sum += soDayList.getDeliveryQuantity();
                }
                String actualDeliveryTime = "-";
                if (soDayList.getActualDeliveryTime() != null) {
                    actualDeliveryTime = soDayList.getActualDeliveryTime();
                }

                Label indexLabel = new Label(0, i + 2, indexStr);
                Label wareNameLabel = new Label(1, i + 2, wareName);
                Label organizationNameLabel = new Label(2, i + 2, organizationName);
                Label itemNameLabel = new Label(3, i + 2, itemName);
                Label itemCodeLabel = new Label(4, i + 2, itemCode);
                Label packDescribeLabel = new Label(5, i + 2, packDescribe);
                Label deliveryQuantityLabel = new Label(6, i + 2, deliveryQuantity);
                Label actualDeliveryTimeLabel = new Label(7, i + 2, actualDeliveryTime);

                indexLabel.setCellFormat(format);
                wareNameLabel.setCellFormat(format);
                organizationNameLabel.setCellFormat(format);
                itemNameLabel.setCellFormat(format);
                itemCodeLabel.setCellFormat(format);
                packDescribeLabel.setCellFormat(format);
                deliveryQuantityLabel.setCellFormat(format);
                actualDeliveryTimeLabel.setCellFormat(format);

                wSheet.addCell(indexLabel);
                wSheet.addCell(wareNameLabel);
                wSheet.addCell(organizationNameLabel);
                wSheet.addCell(itemNameLabel);
                wSheet.addCell(itemCodeLabel);
                wSheet.addCell(packDescribeLabel);
                wSheet.addCell(deliveryQuantityLabel);
                wSheet.addCell(actualDeliveryTimeLabel);

            }

            Label sum1Label = new Label(6, list.size() + 3, two.format(sum));
            sum1Label.setCellFormat(format);
            wSheet.addCell(sum1Label);

            String nowStr = "打印日期：" + DateUtils.getNowDateString();
            Label dateLabel = new Label(0, list.size() + 4, nowStr);
            dateLabel.setCellFormat(format);
            wSheet.addCell(dateLabel);

            copy.write();
            copy.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
        }
        String fullPath = fileS3Tool.save(FileUseType.EXPORT_SO_DAY_LIST, "soDayList.xls",
                new java.io.File(staticPath + downloadPath));
        return ResultGenerator.genSuccessResult(fileS3Tool.getUrl(fullPath));
    }

    @Transactional
    @PostMapping("/bomSplit")
    @Authority("so_details_bomSplit")
    public Result bomSplit(@RequestBody List<SoDetail> soDetails) {
        SoMaster soMaster = soMasterService.findBy("billNo", soDetails.get(0).getBillNo());
        Boolean manufactureFlag = soMaster.getManufactureType().equals(1) ? true : false;

        for (SoDetail soDetail : soDetails) {
            String itemCode = soDetail.getItemCode();
            ItemCombined itemCombined = itemCombinedService.findBy("itemCode", itemCode);
            if (itemCombined == null) {
                throw new ServiceException(CommonCode.NO_SUCH_ITEM_COMBINED);
            }
            ItemComponentCriteria itemComponentCriteria = new ItemComponentCriteria();
            itemComponentCriteria.setCombinedItemCode(itemCode);
            List<ItemComponent> itemComponents = itemComponentService.findList(itemComponentCriteria);
            for (ItemComponent itemComponent : itemComponents) {
                SoDetail newDetail = new SoDetail();
                BeanUtils.copyProperties(soDetail, newDetail);
                newDetail.setItemCode(itemComponent.getComponentItemCode());
                newDetail.setItemName(itemComponent.getComponentItemName());
                newDetail.setOrderQuantity(itemComponent.getQuantity() * soDetail.getOrderQuantity());
                newDetail.setBomSplit(0);
                newDetail.setSoDetailId(null);
                soDetailService.save(newDetail);
            }
            if (manufactureFlag) {
                ManufactureBill bill = new ManufactureBill();
                bill.setItemCode(soDetail.getItemCode());
                bill.setItemName(soDetail.getItemName());
                bill.setQuantity(soDetail.getOrderQuantity());
                bill.setCode(BillNoUtils.generateManufactureBillNo());
                bill.setProcedureDesc(itemCombined.getProcedureDesc());
                bill.setCreateTime(DateUtils.getNowDateTimeString());
                manufactureBillService.save(bill);
            }
            soDetail.setState(-1);
            soDetailService.update(soDetail);
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 
     * 一单多品check，前端商品如果已经加满就会调用check当前detail
     * 
     * @param soDetailId
     * @param currentUser
     * @return
     */
    @Transactional
    @GetMapping("/check")
    @Authority("so_details_check")
    public Result check(Integer soDetailId, @User CurrentUser currentUser) {
        SoDetail soDetail = soDetailService.findById(soDetailId);
        SoMaster soMaster1 = soMasterService.findBy("billNo", soDetail.getBillNo());
        boolean flag = true;
        if (soDetail.getOrderQuantity().equals(soDetail.getCheckQuantity())) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "已复核");
        }
        soDetail.setCheckQuantity(soDetail.getOrderQuantity());
        List<SoDetail> list = soDetailService.findByBillNo(soDetail.getBillNo());
        for (SoDetail detail : list) {
            Double checkQ = soDetailId.equals(detail.getSoDetailId()) ? soDetail.getOrderQuantity() : detail.getCheckQuantity();
            if (detail.getOrderQuantity() > checkQ) {
                flag = false;
            }
        }
        if(flag){
            soMaster1.setState(7);  //复核完成
            soMaster1.setCheckTime(DateUtils.getNowDateTimeString());
            soMaster1.setCheckUserId(currentUser.getUserId());
            soMaster1.setCarrierName(currentUser.getUserName());
            soMasterService.update(soMaster1);
            soMasterService.updateState(soMaster1);
            soDetailService.update(soDetail);
            return ResultGenerator.genSuccessResult(7);
        }else{
            soDetailService.update(soDetail);
            return ResultGenerator.genSuccessResult(6);
        }

    }

    /**
     * 
     * 一单多品check，前端商品如果已经加满就会调用check当前detail
     * 
     * @param soDetailId
     * @param currentUser
     * @return
     */
    @Transactional
    @GetMapping("/check2")
    @Authority("so_details_check2")
    public Result check2(Integer soDetailId,Double quantity, @User CurrentUser currentUser) {
        SoDetail soDetail = soDetailService.findById(soDetailId);
        SoMaster soMaster1 = soMasterService.findBy("billNo", soDetail.getBillNo());
        boolean flag = true;
        //单子的 订单数 = 复核数
        if (soDetail.getOrderQuantity().equals(soDetail.getCheckQuantity())) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "已复核");
        }
        //需要复核数  >  复核数  没有处理 quantity+soDetail.getCheckQuantity() > soDetail.getOrderQuantity()
        if(quantity>soDetail.getOrderQuantity()){
            throw new ServiceException(CommonCode.SERVICE_ERROR, "数量异常");
        }
        soDetail.setCheckQuantity(quantity);
        //需要复核数  = 订单数
        if(soDetail.getOrderQuantity().equals(quantity)){
            List<SoDetail> list = soDetailService.findByBillNo(soDetail.getBillNo());
            for (SoDetail detail : list) {
                Double checkQ = soDetailId.equals(detail.getSoDetailId()) ? quantity : detail.getCheckQuantity();
                // 订单数 > 复核数
                if (detail.getOrderQuantity() > checkQ) {
                    flag = false;
                }
            }
            if(flag){
                soMaster1.setState(7);
                soMaster1.setCheckTime(DateUtils.getNowDateTimeString());
                soMaster1.setCheckUserId(currentUser.getUserId());
                soMaster1.setCarrierName(currentUser.getUserName());
                soMasterService.update(soMaster1);
                soMasterService.updateState(soMaster1);
                soDetailService.update(soDetail);
                return ResultGenerator.genSuccessResult(7);
            }
        }
        soDetailService.update(soDetail);
        return ResultGenerator.genSuccessResult(6);
    }

    @GetMapping("/listAndMaster")
    @Authority("so_details_listAndMaster")
    public Result listAndMaster(SoDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SoDetailDto> list = soDetailService.findListAndMaster(criteria);
        PageInfo<SoDetailDto> pageInfo = new PageInfo<SoDetailDto>(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }


    /**
     * 查询出库任务
     * @return
     */
    @PostMapping("/listAndSend")
    @Authority("so_details_listAndSend")
    public Result findListAndSend(@RequestBody  SoDetailCriteria criteria){
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SoDetailDto> list = soDetailService.findListAndSend(criteria);
        PageInfo<SoDetailDto> pageInfo = new PageInfo<SoDetailDto>(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}