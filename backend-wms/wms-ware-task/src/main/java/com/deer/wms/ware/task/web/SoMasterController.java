package com.deer.wms.ware.task.web;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

import com.deer.wms.base.system.enums.SendState;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.bstek.ureport.Utils;
import com.bstek.ureport.export.ExportManager;
import com.bstek.ureport.export.html.HtmlReport;
import com.deer.wms.base.system.model.SeedingCell;
import com.deer.wms.base.system.model.Item.ItemInfo;
import com.deer.wms.base.system.model.Pack.PackDetail;
import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.base.system.service.PackDetailService;
import com.deer.wms.base.system.service.SeedingCellService;
import com.deer.wms.busine.tactic.model.PrintTempInfo;
import com.deer.wms.busine.tactic.service.PrintTempInfoService;
import com.deer.wms.file.configurer.FileSetting;
import com.deer.wms.file.constant.FileUseType;
import com.deer.wms.file.util.FileS3Tool;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.Inventory.InventoryCriteria;
import com.deer.wms.inventory.model.Inventory.InventoryDto;
import com.deer.wms.inventory.model.Inventory.InventorySelectDto;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.inventory.service.InventoryTransactService;
import com.deer.wms.project.root.constant.DataOperationLogModule;
import com.deer.wms.project.root.constant.DataOperationLogOperation;
import com.deer.wms.project.root.constant.LockName;
import com.deer.wms.project.root.constant.QueueKey;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.core.text.Convert;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.model.QrEntity;
import com.deer.wms.project.root.model.QrEntity.QrStringType;
import com.deer.wms.project.root.sync.annotation.Synchronized;
import com.deer.wms.project.root.util.DataOperationLogTool;
import com.deer.wms.project.root.util.DataOperationLogTool.DataOperationLog;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.ExcelUtils;
import com.deer.wms.project.root.util.HttpClientUtil;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.project.root.util.PrintTempUtils;
import com.deer.wms.project.root.util.QueueTool;
import com.deer.wms.project.root.util.SimpleMap;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.service.BillRecordService;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.deer.wms.system.manage.model.address.ReceivingAddress;
import com.deer.wms.system.manage.model.carrier.Carrier;
import com.deer.wms.system.manage.model.customer.Customer;
import com.deer.wms.system.manage.model.organization.Organization;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.system.manage.service.CarrierService;
import com.deer.wms.system.manage.service.CustomerService;
import com.deer.wms.system.manage.service.OrganizationService;
import com.deer.wms.system.manage.service.ReceivingAddressService;
import com.deer.wms.system.manage.service.ShippingAddressService;
import com.deer.wms.system.manage.service.ValidatorService;
import com.deer.wms.system.manage.service.WareInfoService;
import com.deer.wms.ware.task.model.SO.FileData;
import com.deer.wms.ware.task.model.SO.PdaSearchSoData;
import com.deer.wms.ware.task.model.SO.PrinterLog;
import com.deer.wms.ware.task.model.SO.SoCancelRequest;
import com.deer.wms.ware.task.model.SO.SoData;
import com.deer.wms.ware.task.model.SO.SoDetail;
import com.deer.wms.ware.task.model.SO.SoDetailCriteria;
import com.deer.wms.ware.task.model.SO.SoDetailDto;
import com.deer.wms.ware.task.model.SO.SoImport;
import com.deer.wms.ware.task.model.SO.SoMaster;
import com.deer.wms.ware.task.model.SO.SoMasterCriteria;
import com.deer.wms.ware.task.model.SO.SoMasterDto;
import com.deer.wms.ware.task.model.SO.SoMasterShortageStatistics;
import com.deer.wms.ware.task.model.SO.SoMasterYdyp;
import com.deer.wms.ware.task.model.SO.SoPrintDataAllRequest;
import com.deer.wms.ware.task.model.SO.CreateSo.CreateSo;
import com.deer.wms.ware.task.model.Send.SendDetail;
import com.deer.wms.ware.task.model.Send.SendOutCriteria;
import com.deer.wms.ware.task.model.Wave.WaveMaster;
import com.deer.wms.ware.task.model.pickTask.PickBatch;
import com.deer.wms.ware.task.model.pickTask.PickBatchDetail;
import com.deer.wms.ware.task.model.pickTask.PickBatchGroup;
import com.deer.wms.ware.task.model.pickTask.PickBatchItem;
import com.deer.wms.ware.task.model.pickTask.PickBatchListResponse;
import com.deer.wms.ware.task.model.pickTask.PickBatchResponse;
import com.deer.wms.ware.task.model.pickTask.PickTask;
import com.deer.wms.ware.task.model.pickTask.PickTaskDto;
import com.deer.wms.ware.task.model.print.SoSkuInfo;
import com.deer.wms.ware.task.model.xhcOrderInfo.Order;
import com.deer.wms.ware.task.model.xhcPrint.xhcSoPrintDataAllRequest;
import com.deer.wms.ware.task.service.PickBatchDetailService;
import com.deer.wms.ware.task.service.PickBatchGroupService;
import com.deer.wms.ware.task.service.PickBatchItemService;
import com.deer.wms.ware.task.service.PickBatchService;
import com.deer.wms.ware.task.service.PickTaskReturnService;
import com.deer.wms.ware.task.service.PickTaskService;
import com.deer.wms.ware.task.service.SendDetailService;
import com.deer.wms.ware.task.service.SoDetailService;
import com.deer.wms.ware.task.service.SoMasterService;
import com.deer.wms.ware.task.service.WaveDetailService;
import com.deer.wms.ware.task.service.WaveMasterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.hutool.core.util.StrUtil;
import jxl.format.CellFormat;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * SO信息api接口
 * 
 * Created by guo on 2020/02/02.
 */
@RestController
@RequestMapping("/so/masters")
@Slf4j
public class SoMasterController {

    @Autowired
    private DataOperationLogTool doLogger;

    @Autowired
    private SoMasterControllerExtension that;

    @Autowired
    private SoMasterService soMasterService;

    @Autowired
    private SoDetailService soDetailService;

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private CaseMasterService caseMasterService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CarrierService carrierService;

    @Autowired
    private WareInfoService wareInfoService;

    @Autowired
    private ReceivingAddressService receivingAddressService;

    @Autowired
    private ItemInfoService itemInfoService;

    @Autowired
    private PackDetailService packDetailService;

    @Autowired
    private FileSetting fileSetting;
    @Autowired
    private FileS3Tool fileS3Tool;

    @Autowired
    private ShippingAddressService shippingAddressService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private BillRecordService billRecordService;

    @Autowired
    private SeedingCellService seedingCellService;

    @Autowired
    private QueueTool queue;

    @Autowired
    private SendDetailService sendDetailService;

    @Autowired
    private PickTaskService pickTaskService;

    @Autowired
    private PickBatchGroupService pickBatchGroupService;

    @Autowired
    private PickBatchService pickBatchService;

    @Autowired
    private PickBatchDetailService pickBatchDetailService;

    @Autowired
    private PickBatchItemService pickBatchItemService;

    @Autowired
    private InventoryTransactService inventoryTransactService;

    /**
     * ERP--》WMS生成出库单
     */
    @PostMapping("/createSO")
    @Authority("so_masters_createSO")
    public Result createSO(@RequestBody CreateSo createSo, @User CurrentUser currentUser) {
        for (int i = 0; i < 500; i++) {
            soMasterService.createSO(createSo, currentUser);
        }
        soMasterService.createSO(createSo, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * PassQuan order -> WMS SO order
     */
    @PostMapping("/getOrder")
    @ResponseBody
    @Transactional
    @Authority("so_masters_getOrder")
    public Result getOrder(@RequestParam Map<String, String> params) throws InterruptedException {
        String orderStr = params.get("order");
        log.info(orderStr);
        Order newoOrder = JSON.parseObject(orderStr, Order.class);
        log.info("接收到订单：" + newoOrder.getId() + ",状态：" + newoOrder.getStatus() + ",店铺：" + newoOrder.getShop_id());
        Integer shop_daily_count = newoOrder.getShop_daily_count();
        int status = newoOrder.getStatus();
        if (shop_daily_count != 0 && (status == 10 || status == 11 || status == -1 || status == 12 || status == 18 || status == 20)) {
            BlockingQueue<Order> xhcOrderBlockingQueue = queue.getBlockingQueue(QueueKey.PQ_WEBHOOK_XHC_ORDER);
            xhcOrderBlockingQueue.add(newoOrder);
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 新增SO信息
     */
    @PostMapping("/insert")
    @Authority("so_masters_insert")
    public Result add(@RequestBody SoData soData, @User CurrentUser currentUser) {
        // for (int i=0;i<20000;i++) {
        // SoMaster soMaster = soData.getSoMaster();
        // soMaster.setSoMasterId(null);
        // soMaster.setBillNo(null);
        // soMaster.setAllotBillNo(null);
        // soData.setSoMaster(soMaster);
        // soMasterService.createSoMaster(soData, currentUser);
        // }
        soMasterService.createSoMaster(soData, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    // @GetMapping("/delete")
    // public Result delete(Integer id) {
    // soMasterService.deleteById(id);
    // return ResultGenerator.genSuccessResult();
    // }

    /**
     * 设置优先级
     */
    @GetMapping("/priority")
    @Authority("so_masters_priority")
    public Result setPriority(Integer soMasterId, @User CurrentUser currentUser) {
        if (soMasterId == null) {
            throw new ServiceException(CommonCode.PARAMETER_ERROR, "缺少soMasterId");
        }
        that.setPriorityLocked(soMasterId);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 审核
     */
    @GetMapping("/review")
    @Authority("so_masters_review")
    public Result review(String billNo, Integer reviewResult, Integer caseDetailId, String memo, @User CurrentUser currentUser) {
        SoMaster soMaster = soMasterService.findBy("billNo", billNo);
        // -1不通过 0-流转中 1-通过 2-初始化
        Integer lastReviewResult = caseMasterService.review(caseDetailId, currentUser.getUserId(), reviewResult, memo);
        if (lastReviewResult == -1) {
            soMaster.setState(-1);
            soMasterService.updateState(soMaster);
            // 修改明细状态为取消
            soDetailService.cancel(billNo);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 24);// SO审核
            billRecord.setResult("退回");
            billRecord.setModifyTime(DateUtils.getNowDateTimeString());
            billRecord.setModifyUserId(currentUser.getUserId());
            billRecord.setModifyUserName(currentUser.getUserName());
            billRecordService.update(billRecord);
        } else if (lastReviewResult == 0) {
            soMaster.setState(0);
            soMasterService.updateState(soMaster);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 24);// SO审核
            billRecord.setResult("流转");
            billRecordService.update(billRecord);
        } else if (lastReviewResult == 2) {
            soMaster.setState(2);
            soMasterService.updateState(soMaster);
        } else if (lastReviewResult == 1) {
            soMaster.setState(1);
            soMasterService.updateState(soMaster);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 24);// SO审核
            billRecord.setResult("通过");
            billRecord.setModifyTime(DateUtils.getNowDateTimeString());
            billRecord.setModifyUserId(currentUser.getUserId());
            billRecord.setModifyUserName(currentUser.getUserName());
            billRecordService.update(billRecord);
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 重新发起审核
     */
    @GetMapping("/reReview")
    @Authority("so_masters_reReview")
    public Result reReview(String billNo, @User CurrentUser currentUser) {
        SoMaster soMaster = soMasterService.findBy("billNo", billNo);

        // 初始化状态 2
        soMaster.setState(2);
        soMasterService.updateState(soMaster);
        caseMasterService.createCase(billNo, 2, currentUser.getUserId(), soMaster.getWareId(), soMaster.getOrganizationId());

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 增删soMaster、soDetail
     */
    @PostMapping("/update")
    @Authority("so_masters_update")
    public Result update(@RequestBody SoData soData, @User CurrentUser currentUser) {
        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(soData.getSoMaster().getBillNo());
        billRecord.setRecordType(27);// SO修改
        billRecord.setCreateTime(DateUtils.getNowDateTimeString());
        billRecord.setCreateUserId(currentUser.getUserId());
        billRecord.setCreateUserName(currentUser.getUserName());
        billRecordService.createBillRecord(billRecord, currentUser);
        SoMaster soMaster1 = soData.getSoMaster();
        soMasterService.update(soMaster1);

        List<SoDetail> soDetails = soData.getSoDetails();

        for (int i = 0; i < soDetails.size(); i++) {
            SoDetail soDetail = soDetails.get(i);
            String detailNo = soData.getSoMaster().getBillNo() + "-" + (i + 1);
            soDetail.setDetailNo(detailNo);
            soDetail.setBillNo(soData.getSoMaster().getBillNo());
            soDetail.setSoMasterId(soData.getSoMaster().getSoMasterId());
            soDetail.setDetailNo(detailNo);
            soDetail.setDetailIndex(i + 1);

            soDetailService.update(soDetail);
        }
        BillRecord billRecord1 = billRecordService.findByBillNoAndType(soData.getSoMaster().getBillNo(), 27);// SO修改
        billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
        billRecord1.setModifyUserId(currentUser.getUserId());
        billRecord1.setModifyUserName(currentUser.getUserName());
        billRecordService.update(billRecord1);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("so_masters_detail")
    public Result detail(@PathVariable Integer id) {
        SoMaster soMaster = soMasterService.findById(id);
        return ResultGenerator.genSuccessResult(soMaster);
    }

    // 应对查询条件超长问题，增设POST+RequestBody查询接口
    @PostMapping("/list")
    @Authority("so_masters_postList")
    public Result listP(@RequestBody SoMasterCriteria criteria) {
        return this.list(criteria);
    }

    @GetMapping("/list")
    @Authority("so_masters_getList")
    public Result list(SoMasterCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SoMasterDto> list = soMasterService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/check")
    @Transactional
    @Authority("so_masters_check")
    public Result check(String billNo, @User CurrentUser currentUser) {

        SoMaster soMaster1 = soMasterService.findBy("billNo", billNo);
        if (soMaster1.getSendState() == SendState.ALL) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "此订单已经发运!");
        }
        soMaster1.setCheckTime(DateUtils.getNowDateTimeString());
        soMaster1.setCheckUserId(currentUser.getUserId());
        soMaster1.setCheckUserName(currentUser.getUserName());
        soMaster1.setState(7);
        soMasterService.update(soMaster1);
        soMasterService.updateState(soMaster1);
        List<SoDetail> list = soDetailService.findByBillNo(billNo);
        for (SoDetail detail : list) {
            detail.setCheckQuantity(detail.getOrderQuantity());
            soDetailService.update(detail);
        }

        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/seedingCheck")
    @Authority("so_masters_seedingCheck")
    public Result seedingCheck(String cellCode, String billNo, @User CurrentUser currentUser) {
        SoMaster soMaster1 = soMasterService.findBy("billNo", billNo);
        if (soMaster1.getState() == 7) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "该订单已经播种！");
        }

        soMaster1.setCheckTime(DateUtils.getNowDateTimeString());
        soMaster1.setCheckUserId(currentUser.getUserId());
        soMaster1.setCheckUserName(currentUser.getUserName());
        soMaster1.setState(7);
        soMasterService.update(soMaster1);
        soMasterService.updateState(soMaster1);
        List<SoDetail> list = soDetailService.findByBillNo(billNo);
        Double quantity = 0.0;
        for (SoDetail detail : list) {
            quantity += detail.getOrderQuantity();
            detail.setCheckQuantity(detail.getOrderQuantity());
            soDetailService.update(detail);
        }

        SeedingCell seedingCell = seedingCellService.findBy("seedingCellCode", cellCode);
        seedingCell.setBillNo(billNo);
        seedingCell.setState(1);
        seedingCellService.update(seedingCell);

        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/downSeeding")
    @Authority("so_masters_downSeeding")
    public Result downSeeding(String data, @User CurrentUser currentUser) {

        SeedingCell seedingCell = seedingCellService.findBy("seedingCellCode", data);
        if (seedingCell == null) {
            SoMaster soMaster = soMasterService.findBy("shipBillCode", data);
            if (soMaster != null) {
                seedingCell = seedingCellService.findBy("billNo", soMaster.getBillNo());
            }
        }
        if (seedingCell != null) {
            seedingCell.setBillNo("");
            seedingCell.setState(-1);
            seedingCell.setQuantity(0.0);
            seedingCellService.update(seedingCell);
        }
        return ResultGenerator.genSuccessResult();
    }

    @Autowired
    private WaveMasterService waveMasterService;

    @GetMapping("/findByWaveCode")
    @Authority("so_masters_findByWaveCode")
    public Result findByWaveCode(Integer id, Integer wareId) {
        WaveMaster waveMaster = waveMasterService.findById(id);
        Integer waveId = null;
        if (waveMaster != null) {
            waveId = waveMaster.getWaveId();
        }
        List<SoMasterDto> list = soMasterService.findByWaveId(waveId, wareId);
        return ResultGenerator.genSuccessResult(list);
    }

    @Autowired
    private WaveDetailService waveDetailService;

    @Autowired
    private PickTaskReturnService pickTaskReturnService;

    /**
     * 取消
     */
    @GetMapping("/cancelSO")
    @Transactional
    @Authority("so_masters_cancelSO")
    public Result cancelSO(String billNo, @User CurrentUser currentUser) {
        SoMaster soMaster = soMasterService.findBy("billNo", billNo);
        soMasterService.cancel(soMaster, currentUser);

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 取消
     */
    @PostMapping("/cancel")
    @Transactional
    @Authority("so_masters_cancel")
    public Result cancel(@RequestBody SoCancelRequest request, @User CurrentUser currentUser) {
        SoMaster soMaster = soMasterService.findBy("billNo", request.getBillNo());
        soMasterService.cancel(soMaster, currentUser);

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 归档SO信息 传billNo
     */
    @PostMapping("/file")
    @Authority("so_masters_file")
    public Result file(@RequestBody FileData fileData, @User CurrentUser currentUser) {
        for (int i = 0; i < fileData.getBillNos().size(); i++) {
            String billNo = fileData.getBillNos().get(i);
            soMasterService.createFile(billNo, currentUser);
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * SO单据导入
     */
    @Transactional
    @PostMapping("/importSoBills")
    @Authority("so_masters_importSoBills")
    public Result importSoBills(SoImport excel, @User CurrentUser currentUser) {


        jxl.Workbook billWorkbook = null;

        String msg = "Excel 内容有问题，无法导入！";

        try {
            billWorkbook = jxl.Workbook.getWorkbook(excel.getModel().getInputStream());
            jxl.Sheet billSheet = billWorkbook.getSheet(0);

            int rows = billSheet.getRows();

            SoMaster soMaster = new SoMaster();

            String tableName = "so_master";
            String nextId = validatorService.getNextIdString(tableName);
            String billNo = "SO" + "-" + nextId;

            String wareName = ExcelUtils.getCellString(billSheet, 0, 2).trim();
            Integer wareId = null;
            WareInfo wareInfo = wareInfoService.findBy("wareName", wareName);
            if (wareInfo != null) {
                wareId = wareInfo.getWareId();

            } else {
                msg = "仓库名称错误！" + wareName;
                throw new Exception();
            }

            String organizationName = ExcelUtils.getCellString(billSheet, 1, 2).trim();
            Integer organizationId = null;
            Organization organization = organizationService.findBy("organizationName", organizationName);
            if (organization != null) {
                organizationId = organization.getOrganizationId();

            } else {
                msg = "货主名称错误！" + organizationName;
                throw new Exception();
            }
            String customerName = ExcelUtils.getCellString(billSheet, 2, 2).trim();
            Integer customerId = null;
            Customer customer = customerService.findBy("customerName", customerName);
            if (customer != null) {
                customerId = customer.getCustomerId();

            } else {
                msg = "客户名称错误！" + customerName;
                throw new Exception();
            }

            String carrierName = ExcelUtils.getCellString(billSheet, 3, 2).trim();
            Integer carrierId = null;
            Carrier carrier = carrierService.findBy("carrierName", carrierName);
            if (carrier != null) {
                carrierId = carrier.getCarrierId();

            } else {
                msg = "承运方名称错误！" + carrierName;
                throw new Exception();
            }

            String linkName = ExcelUtils.getCellString(billSheet, 4, 2).trim();
            Integer receivingAddressId = null;
            ReceivingAddress receivingAddress = receivingAddressService.findBy("linkMan", linkName);
            if (receivingAddress != null) {
                receivingAddressId = receivingAddress.getReceivingAddressId();

            } else {
                msg = "联系人错误！" + linkName;
                throw new Exception();
            }
            String deliveryTime = ExcelUtils.getCellString(billSheet, 5, 2).trim();
            String expectTimeFm = ExcelUtils.getCellString(billSheet, 6, 2).trim();
            String expectTimeTo = ExcelUtils.getCellString(billSheet, 7, 2).trim();
            Integer soType = ExcelUtils.getCellInteger(billSheet, 8, 2);
            String memo = ExcelUtils.getCellString(billSheet, 9, 2);
            String soUdfHs1 = ExcelUtils.getCellString(billSheet, 10, 2);
            String soUdfHs2 = ExcelUtils.getCellString(billSheet, 11, 2);
            String soUdfHs3 = ExcelUtils.getCellString(billSheet, 12, 2);
            soMaster.setWareId(wareId);
            soMaster.setWareName(wareName);
            soMaster.setOrganizationId(organizationId);
            soMaster.setOrganizationName(organizationName);
            soMaster.setCustomerId(customerId);
            soMaster.setCustomerName(customerName);
            soMaster.setCarrierId(carrierId);
            soMaster.setCarrierName(carrierName);
            soMaster.setReceivingAddressId(receivingAddressId);
            soMaster.setLinkMan(linkName);
            soMaster.setState(0);
            String time = DateUtils.getNowDateTimeString();
            soMaster.setCreateUserId(currentUser.getUserId());
            soMaster.setCreateUserName(currentUser.getUserName());
            soMaster.setCreateTime(time);
            soMaster.setDeliveryTime(deliveryTime);
            soMaster.setExpectTimeFm(expectTimeFm);
            soMaster.setExpectTimeTo(expectTimeTo);
            soMaster.setOrderNo(1);
            soMaster.setBillSource(3); // 导入
            soMaster.setInsertType(excel.getInsertType());
            soMaster.setSoUdfHs1(soUdfHs1);
            soMaster.setSoUdfHs2(soUdfHs2);
            soMaster.setSoUdfHs3(soUdfHs3);
            soMaster.setSoType(soType);
            soMaster.setMemo(memo);

            Integer state = caseMasterService.createCase(billNo, 3, currentUser.getUserId(), wareId, organizationId);

            soMaster.setCaseState(state);

            soMasterService.save(soMaster);

            Integer rowCount = 0;
            for (int i = 6; i < rows; i++) {
                String content = ExcelUtils.getCellString(billSheet, 1, i);
                if (content == null || content == "") {
                    break;
                }
                rowCount++;
            }

            for (int i = 6; i < rowCount + 6; i++) {
                String itemName = ExcelUtils.getCellString(billSheet, 1, i).trim();
                if (itemName == null) {
                    msg = "缺少物料名称！" + itemName;
                    throw new Exception();
                }

                String packDescribe = ExcelUtils.getCellString(billSheet, 2, i);
                if (packDescribe == null) {
                    msg = "缺少单位！" + packDescribe;
                    throw new Exception();
                }

                Double orderQuantity = ExcelUtils.getCellDouble(billSheet, 3, i);
                if (orderQuantity == null) {
                    msg = "缺少订货数量！" + orderQuantity;
                    throw new Exception();
                }
                String memo1 = ExcelUtils.getCellString(billSheet, 4, i);
                String soUdfDs1 = ExcelUtils.getCellString(billSheet, 5, i);
                String soUdfDs2 = ExcelUtils.getCellString(billSheet, 6, i);
                String soUdfDs3 = ExcelUtils.getCellString(billSheet, 7, i);
                ItemInfo itemInfo = itemInfoService.findBy("itemName", itemName);
                PackDetail packDetail = packDetailService.findBy("packDescribe", packDescribe);

                SoDetail soDetail = new SoDetail();

                soDetail.setBillNo(billNo);
                soDetail.setSoMasterId(soMaster.getSoMasterId());
                soDetail.setDetailNo(billNo + "-" + (i - 6 + 1));
                soDetail.setDetailIndex(i - 6 + 1);
                soDetail.setItemCode(itemInfo.getItemCode());
                soDetail.setItemName(itemName);
                soDetail.setPackDetailId(packDetail.getPackDetailId());
                soDetail.setPackDescribe(packDescribe);
                soDetail.setOrderQuantity(orderQuantity);
                soDetail.setMemo(memo1);
                soDetail.setSoUdfDs1(soUdfDs1);
                soDetail.setSoUdfDs2(soUdfDs2);
                soDetail.setSoUdfDs3(soUdfDs3);

                soDetailService.save(soDetail);

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(CommonCode.WRONG_EXCEL_CONTENT, msg);
        } finally {
            billWorkbook.close();
        }

        return ResultGenerator.genSuccessResult();
    }

    /**
     * SoMaster导出
     */
    @GetMapping("/exportSoMaster")
    @Authority("so_masters_exportSoMaster")
    public Result exportSoMaster(SoMasterCriteria criteria, @User CurrentUser currentUser) {
        criteria.setPageNum(1);
        criteria.setPageSize(10000);

        List<SoMasterDto> list = soMasterService.findList(criteria);

        String staticPath = fileSetting.getStaticFileDir();
        String downloadPath = fileSetting.getDownloadPath() + "/SoImport" + DateUtils.dateToStr(new Date(), "yyyyMMddHHmmss") + ".xls";
        String modelPath = fileSetting.getModelPath() + "/SoImportModel.xls";
        try {
            String modelFilePath = staticPath + modelPath;
            String filePath = staticPath + downloadPath;

            jxl.Workbook workbook = jxl.Workbook.getWorkbook(new java.io.File(modelFilePath));
            WritableWorkbook copy = jxl.Workbook.createWorkbook(new java.io.File(filePath), workbook);

            WritableSheet wSheet = copy.getSheet(0);
            CellFormat format = wSheet.getCell(1, 1).getCellFormat();

            for (int i = 0; i < list.size(); i++) {

                wSheet.insertRow(i + 2);

                SoMaster soMaster = list.get(i);

                String indexStr = Integer.toString(i + 1);
                String billNo = "-";
                if (soMaster.getBillNo() != null) {
                    billNo = soMaster.getBillNo();
                }
                String mergeNo = "-";
                if (soMaster.getMergeNo() != null) {
                    mergeNo = soMaster.getMergeNo();
                }
                String billSource = "-";
                if (soMaster.getBillSource() != null) {
                    billSource = Integer.toString(soMaster.getBillSource());
                }
                String soType = "-";
                if (soMaster.getSoType() != null) {
                    soType = Integer.toString(soMaster.getSoType());
                }
                String organizationName = "-";
                if (soMaster.getOrganizationName() != null) {
                    organizationName = soMaster.getOrganizationName();
                }

                String customerName = "-";
                if (soMaster.getOrganizationName() != null) {
                    customerName = soMaster.getOrganizationName();
                }
                String linkMan = "-";
                if (soMaster.getLinkMan() != null) {
                    linkMan = soMaster.getLinkMan();
                }
                String carrierName = "-";
                if (soMaster.getCarrierName() != null) {
                    carrierName = soMaster.getCarrierName();
                }

                String deliveryTime = "-";
                if (soMaster.getDeliveryTime() != null) {
                    deliveryTime = soMaster.getDeliveryTime();
                }
                String expectTimeFm = "-";
                if (soMaster.getExpectTimeFm() != null) {
                    expectTimeFm = soMaster.getExpectTimeFm();
                }
                String expectTimeTo = "-";
                if (soMaster.getExpectTimeTo() != null) {
                    expectTimeTo = soMaster.getExpectTimeTo();
                }
                String soUdfHs1 = "-";
                if (soMaster.getSoUdfHs1() != null) {
                    soUdfHs1 = soMaster.getSoUdfHs1();
                }
                String soUdfHs2 = "-";
                if (soMaster.getSoUdfHs2() != null) {
                    soUdfHs2 = soMaster.getSoUdfHs2();
                }
                String soUdfHs3 = "-";
                if (soMaster.getSoUdfHs3() != null) {
                    soUdfHs3 = soMaster.getSoUdfHs3();
                }
                String createTime = "-";
                if (soMaster.getCreateTime() != null) {
                    createTime = soMaster.getCreateTime();
                }
                String createUserName = "-";
                if (soMaster.getCreateUserName() != null) {
                    createUserName = soMaster.getCreateUserName();
                }
                String memo = "-";
                if (soMaster.getMemo() != null) {
                    memo = soMaster.getMemo();
                }

                Label indexLabel = new Label(0, i + 2, indexStr);
                Label billNoLabel = new Label(1, i + 2, billNo);
                Label mergeNoLabel = new Label(2, i + 2, mergeNo);
                Label billSourceLabel = new Label(3, i + 2, billSource);
                Label soTypeLabel = new Label(4, i + 2, soType);
                Label organizationNameLabel = new Label(5, i + 2, organizationName);
                Label customerNameLabel = new Label(6, i + 2, customerName);
                Label linkManLabel = new Label(7, i + 2, linkMan);
                Label carrierNameLabel = new Label(8, i + 2, carrierName);
                Label deliveryTimeLabel = new Label(9, i + 2, deliveryTime);
                Label expectTimeFmLabel = new Label(10, i + 2, expectTimeFm);
                Label expectTimeToLabel = new Label(11, i + 2, expectTimeTo);
                Label soUdfHs1Label = new Label(12, i + 2, soUdfHs1);
                Label soUdfHs2Label = new Label(13, i + 2, soUdfHs2);
                Label soUdfHs3Label = new Label(14, i + 2, soUdfHs3);
                Label createTimeLabel = new Label(15, i + 2, createTime);
                Label createUserNameLabel = new Label(16, i + 2, createUserName);
                Label memoLabel = new Label(17, i + 2, memo);

                indexLabel.setCellFormat(format);
                billNoLabel.setCellFormat(format);
                mergeNoLabel.setCellFormat(format);
                billSourceLabel.setCellFormat(format);
                soTypeLabel.setCellFormat(format);
                organizationNameLabel.setCellFormat(format);
                customerNameLabel.setCellFormat(format);
                linkManLabel.setCellFormat(format);
                carrierNameLabel.setCellFormat(format);
                deliveryTimeLabel.setCellFormat(format);
                expectTimeFmLabel.setCellFormat(format);
                expectTimeToLabel.setCellFormat(format);
                soUdfHs1Label.setCellFormat(format);
                soUdfHs2Label.setCellFormat(format);
                soUdfHs3Label.setCellFormat(format);
                createTimeLabel.setCellFormat(format);
                createUserNameLabel.setCellFormat(format);
                memoLabel.setCellFormat(format);

                wSheet.addCell(indexLabel);
                wSheet.addCell(billNoLabel);
                wSheet.addCell(mergeNoLabel);
                wSheet.addCell(billSourceLabel);
                wSheet.addCell(soTypeLabel);
                wSheet.addCell(organizationNameLabel);
                wSheet.addCell(customerNameLabel);
                wSheet.addCell(linkManLabel);
                wSheet.addCell(carrierNameLabel);
                wSheet.addCell(deliveryTimeLabel);
                wSheet.addCell(expectTimeFmLabel);
                wSheet.addCell(expectTimeToLabel);
                wSheet.addCell(soUdfHs1Label);
                wSheet.addCell(soUdfHs2Label);
                wSheet.addCell(soUdfHs3Label);
                wSheet.addCell(createTimeLabel);
                wSheet.addCell(createUserNameLabel);
                wSheet.addCell(memoLabel);
            }

            String nowStr = "打印日期：" + DateUtils.getNowDateString();
            Label dateLabel = new Label(0, list.size() + 3, nowStr);
            dateLabel.setCellFormat(format);
            wSheet.addCell(dateLabel);

            copy.write();
            copy.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {}
        String fullPath = fileS3Tool.save(FileUseType.EXPORT_SO_MASTER, "SoImport.xls",
                new java.io.File(staticPath + downloadPath));
        return ResultGenerator.genSuccessResult(fileS3Tool.getUrl(fullPath));
    }

    @GetMapping("/setCarrier")
    @Transactional
    @Authority("so_masters_setCarrier")
    public Result setCarrier() {
        soMasterService.setCarriersWithRouteTactic();
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 根据追踪号查询
     * @param trackCode
     * @return
     */
    @GetMapping("/pdaSearchSo")
    @Authority("so_masters_pdaSearchSo")
    public Result pdaSearchSo(String trackCode) {
        List<PdaSearchSoData> dataList = soMasterService.pdaSearchSo(trackCode);
        PageInfo pageInfo = new PageInfo(dataList);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/getYdypByItemCode")
    @Transactional
    @Authority("so_masters_getYdypByItemCode")
    public Result getYdyp(String itemCode, Integer wareId, Integer waveId) {
        SoMasterYdyp soMasterYdyp = soMasterService.getYdyp(itemCode, wareId, waveId);
        if (soMasterYdyp == null) {
            soMasterYdyp = soMasterService.getYdyp2(itemCode, wareId, waveId);
        }

        return ResultGenerator.genSuccessResult(soMasterYdyp);
    }

    @GetMapping("/getSeedingByItemCode")
    @Transactional
    @Authority("so_masters_getSeedingByItemCode")
    public Result getSeedingByItemCode(String itemCode, Integer wareId, Integer waveMasterId, String cellCode) {
        SoMasterDto soMasterDto = soMasterService.getSeedingByItemCode(itemCode, wareId, waveMasterId);
        SeedingCell seedingCell = seedingCellService.findBy("seedingCellCode", cellCode);
        if (soMasterDto == null) {
            return ResultGenerator.genSuccessResult();
        }
        seedingCell.setBillNo(soMasterDto.getBillNo());
        seedingCell.setState(0);
        seedingCellService.update(seedingCell);
        return ResultGenerator.genSuccessResult(soMasterDto);
    }

    @GetMapping("/getSeedingByItemCode2")
    @Transactional
    @Authority("so_masters_getSeedingByItemCode2")
    public Result getSeedingByItemCode2(String itemCode, Integer wareId, Integer waveMasterId, String boxCode) {
        WaveMaster waveMaster = waveMasterService.findByBoxCode(boxCode, wareId);
        if (waveMaster == null) {
            throw new ServiceException(CommonCode.NO_WAVE);

        }
        List<SoDetailDto> list = soDetailService.getSeedingByItemCode(itemCode, wareId, waveMaster.getWaveId());
        if (list.size() == 0) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "SKU/UPC无效（" + itemCode + "）");
        }
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/getSeedingYdYpByBox")
    @Transactional
    @Authority("so_masters_getSeedingYdYpByBox")
    public Result getSeedingYdYpByBox(String boxCode, Integer wareId) {
        // 查找拣货完成和播种完成的一单一品波次
        WaveMaster waveMaster = waveMasterService.findByYdYpByBoxCode(boxCode, wareId);
        if (waveMaster == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到波次");
        }
        List<SoDetailDto> list = soDetailService.getSeedingYdYpByWaveMasterId(wareId, waveMaster.getWaveId());
        Map map = new HashMap();
        map.put("bills", list);
        map.put("wave", waveMaster);
        return ResultGenerator.genSuccessResult(map);
    }

    // 出库最后一步 发运
    @Authority("so_masters_sendOut")
    @GetMapping("/sendOut")
    @Transactional
    public Result sendOut(@RequestParam("billNo") String billNo
            ,@RequestParam(value = "wareId",required = false) Integer wareId
            ,@RequestParam(value = "weight",required = false) Double weight
            ,@RequestParam(value = "autoPrint",required = false) Boolean autoPrint
            , @User CurrentUser currentUser) {
        /**
         * 判断出库单状态是否可以发运 找到分配的拣货单 扣减库存 改变出库单状态
         */
        SoMaster soMaster1 = soMasterService.findBy("billNo", billNo);
        if (soMaster1.getSendState() == SendState.ALL) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "该订单已经发运！");
        }
        // 如果没有在工作台被复核 则不允许出库
        if (!soMaster1.getWareId().equals(wareId)) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "非该仓库发运单！");
        }
        // 如果没有在工作台被复核 则不允许出库
//        if (soMaster1.getState() < 7) {
//            throw new ServiceException(CommonCode.SERVICE_ERROR, "该订单未复核，不可发运！");
//        }
        // 如果没有在工作台被复核 则不允许出库
        if (soMaster1.getSendState() == SendState.ALL) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "该订单已发运，不可重复发运！");
        }
        // inventoryService.sendOut(billNo, wareId);
        soMaster1.setSendState(SendState.ALL);
        soMaster1.setOutTime(DateUtils.getNowDateTimeString());
        soMaster1.setOutUserId(currentUser.getUserId());
        soMaster1.setOutUserName(currentUser.getUserName());
        soMaster1.setWeight(weight);
        soMasterService.update(soMaster1);
        soMasterService.updateState(soMaster1);
        List<String> itemSKU = new ArrayList();
        List<SoDetail> list = soDetailService.findByBillNo(soMaster1.getBillNo());
        for (SoDetail soDetail : list) {
            if (itemSKU == null || !itemSKU.contains(soDetail.getItemCode())) {
                soDetail.setDeliveryQuantity(soDetail.getOrderQuantity());
                soDetail.setState(6); // 完全发运
                soDetailService.update(soDetail);
                itemSKU.add(soDetail.getItemCode());
            }
        }
        List<PickTask> pickTasks = pickTaskService.findBySoMasterId(soMaster1.getSoMasterId());
        for (PickTask pickTask : pickTasks) {
            if (pickTask.getIsParent() == 0) {
                Inventory inventory = inventoryService.findById(pickTask.getInventoryId());
                if (inventory != null) {
                    inventory.setQuantity(inventory.getQuantity() - pickTask.getPickQuantity());
                    inventoryService.update(inventory);
                    pickTask.setState(5);
                    pickTaskService.update(pickTask);
                }

            }

        }
        if (autoPrint != null && autoPrint) {
            return getDzmd(soMaster1.getBillNo());
        }
        return ResultGenerator.genSuccessResult();

    }

    @Autowired
    private PrintTempInfoService printTempInfoService;

    @GetMapping("/getDtoByBoxOrCell")
    @Transactional
    @Authority("so_masters_getDtoByBoxOrCell")
    public Result getDtoByBoxOrCell(String code, Integer wareId) {
        SoMasterDto soMaster = soMasterService.getDtoByBoxOrCell(code, wareId);
        if (soMaster == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到相应的订单！");
        }
        double pickTotalQuantity = 0d;
        if (!CollectionUtils.isEmpty(soMaster.getList())) {
            for (SoDetail soDetail : soMaster.getList()) {
                pickTotalQuantity = pickTotalQuantity + soDetail.getCheckQuantity();
            }
        }
        soMaster.setPickTotalQuantity(pickTotalQuantity);

        return ResultGenerator.genSuccessResult(soMaster);
    }

    /**
     * 发运列表查询
     * @param orderNo
     * @param wareId
     * @return
     */
    @GetMapping("/findBillDetailByOrderNo")
    @Transactional
    @Authority("so_masters_findBillDetailByOrderNo")
    public Result findBillDetailByOrderNo(String orderNo, Integer wareId) {
        SoMasterDto soMaster = soMasterService.findBillDetailByOrderNo(orderNo, wareId);
        if (soMaster == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到相应的订单！");
        }
        // if(soMaster.getState()<7){
        // throw new ServiceException(CommonCode.SERVICE_ERROR, "该订单未复核！");
        // }
        return ResultGenerator.genSuccessResult(soMaster);
    }

    @GetMapping("/getDzmdByBoxOrCell")
    @Transactional
    @Authority("so_masters_getDzmdByBoxOrCell")
    public Result getDzmdByBoxOrCell(String code, Integer wareId) {
        SoMaster soMaster = soMasterService.getByBoxOrCell(code, wareId);
        if (soMaster == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到相应的订单！");

        }

        return ResultGenerator.genSuccessResult(getDzmdStr(soMaster.getBillNo()));
    }

    @GetMapping("/getDzmd")
    @Transactional
    @Authority("so_masters_getDzmd")
    public Result getDzmd(String billNo) {

        return ResultGenerator.genSuccessResult(getDzmdStr(billNo));
    }

    /**
     * 打印单个SO单
     */
    @GetMapping("/getPrintData")
    @Transactional
    @Authority("so_masters_getPrintData")
    public Result getPrintData(Integer soId, @User CurrentUser currentUser) {
        return ResultGenerator.genSuccessResult(getPrintDataStr(soMasterService.findById(soId), null, currentUser));
    }

    /**
     * 打印发票
     * @param soId
     * @param currentUser
     * @return
     * @throws IOException
     */
    @GetMapping("/getPrintInvoice")
    @Transactional
    @Authority("so_masters_getPrintInvoice")
    public Result getPrintInvoice(Integer soId, @User CurrentUser currentUser) throws IOException {
        SoMaster soMaster = soMasterService.findById(soId);

        return ResultGenerator.genSuccessResult(soMasterService.getPdfUrl(soMaster));
    }

    public String getPrintDataStr(SoMaster soMaster, PrintTempInfo printTempInfo, CurrentUser currentUser) {
        return getPrintDataStr(soMaster, printTempInfo, currentUser, "无", "无");
    }

    /**
     * xuesinuo: 打印送货确认单
     */
    public String getPrintDataStr(SoMaster soMaster, PrintTempInfo printTempInfo, CurrentUser currentUser, String driverName, String serialNumber) {
        if (soMaster == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到相应订单！");
        }
        List<SoDetail> list = soDetailService.findByMasterId(soMaster.getSoMasterId());
        // 获取电子面单模板
        if (printTempInfo == null) {
            if (null == driverName || "无".equals(driverName) || driverName.isEmpty()) {
                printTempInfo = printTempInfoService.findByWare(soMaster.getWareId(), "出库单");
            } else {
                printTempInfo = printTempInfoService.findByWare(soMaster.getWareId(), "分司机出库单");
            }
            if (printTempInfo == null) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到匹配的出库单模板");
            }
        }
        if (printTempInfo.getSjType() == 2) {
            ExportManager exportManager = (ExportManager) Utils.getApplicationContext().getBean(ExportManager.BEAN_ID);
            String fileName = "mysql:" + printTempInfo.getFileName() + ".ureport.xml";
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("billNo", soMaster.getBillNo());
            HtmlReport htmlReport = exportManager.exportHtml(fileName, "/wms", parameters);
            return htmlReport.getContent() + "<style>" + htmlReport.getStyle() + "</style>";
        }

        String html = printTempInfo.getTempHtml();
        Document doc = Jsoup.parse(html);
        // 删除不显示的
        doc.select(".dis-none").remove();
        // 直线中间蓝点的问题
        doc.select(".line-handle").remove();

        if (null == driverName || "无".equals(driverName) || driverName.isEmpty()) {
            PrintTempUtils.setTempData2("shop_daily_count", "#" + soMaster.getShopDailyCount(), doc, "text");
        } else {
            PrintTempUtils.setTempData2("order_driverName", driverName, doc, "text");
            PrintTempUtils.setTempData2("order_serialNumber", "#" + serialNumber, doc, "text");
        }

        PrintTempUtils.setTempData2("ORDER_NO", soMaster.getAllotBillNo().equals("999") ? soMaster.getBillNo() : soMaster.getAllotBillNo(), doc, "text");
        PrintTempUtils.setTempData2("ORDER_NO_BAR_CODE", soMaster.getAllotBillNo().equals("999") ? soMaster.getBillNo() : soMaster.getAllotBillNo(), doc, "barCode");
        PrintTempUtils.setTempData2("ORDER_NO_QR_CODE", soMaster.getAllotBillNo().equals("999") ? soMaster.getBillNo() : soMaster.getAllotBillNo(), doc, "qrCode");
        PrintTempUtils.setTempData2("SO_BILL_NO", soMaster.getBillNo(), doc, "text");
        PrintTempUtils.setTempData2("SHIP_BILL_NO_BAR_CODE", soMaster.getShipBillCode(), doc, "barCode");
        PrintTempUtils.setTempData2("SHIP_BILL_NO_QR_CODE", soMaster.getShipBillCode(), doc, "qrCode");
        PrintTempUtils.setTempData2("SHIP_BILL_NO", soMaster.getShipBillCode(), doc, "text");
        PrintTempUtils.setTempData("TRADE_NO", soMaster.getTradeNo(), doc, "text");
        PrintTempUtils.setTempData("SHOP_NAME", soMaster.getShop(), doc, "text");
        PrintTempUtils.setTempData("SENDER_NAME", soMaster.getShipLinkMan(), doc, "text");
        PrintTempUtils.setTempData("SENDER_ADDRESS", soMaster.getShipDetailAddress(), doc, "text");
        PrintTempUtils.setTempData("SENDER_PROVINCE", soMaster.getShipProvince(), doc, "text");
        PrintTempUtils.setTempData("SENDER_CITY", soMaster.getShipCity(), doc, "text");
        PrintTempUtils.setTempData("SENDER_AREA", soMaster.getShipArea(), doc, "text");
        PrintTempUtils.setTempData("SENDER_MOBILE", soMaster.getShipLinkPhone(), doc, "text");
        PrintTempUtils.setTempData("SENDER_COUNTRY", soMaster.getShipCountry(), doc, "text");
        PrintTempUtils.setTempData("SHIP_POST_CODE", soMaster.getShipPostCode(), doc, "text");
        PrintTempUtils.setTempData("CN_DECLARATION_NAME", soMaster.getCnDeclarationName(), doc, "text");
        PrintTempUtils.setTempData("DECLARATION_NAME", soMaster.getDeclarationName(), doc, "text");

        PrintTempUtils.setTempData("RECEIVER_NAME", soMaster.getLinkMan(), doc, "text");
        PrintTempUtils.setTempData("RECEIVER_ADDRESS", soMaster.getDetailAddress(), doc, "text");
        PrintTempUtils.setTempData("RECEIVER_PROVINCE", soMaster.getProvince(), doc, "text");
        PrintTempUtils.setTempData("RECEIVER_CITY", soMaster.getCity(), doc, "text");
        PrintTempUtils.setTempData("RECEIVER_AREA", soMaster.getArea(), doc, "text");
        PrintTempUtils.setTempData("RECEIVER_MOBILE", soMaster.getLinkPhone(), doc, "text");
        PrintTempUtils.setTempData("RECEIVER_COUNTRY", soMaster.getCountry(), doc, "text");
        PrintTempUtils.setTempData("LINK_POST_CODE", soMaster.getLinkPostCode(), doc, "text");

        PrintTempUtils.setTempData("ORDER_TOTAL_PRICE", soMaster.getTotalPrice(), doc, "text");

        PrintTempUtils.setTempData("LMSC", soMaster.getLastMileSortingCode(), doc, "text");
        PrintTempUtils.setTempData("LMD", soMaster.getLastMileDestination(), doc, "text");
        PrintTempUtils.setTempData("LMLC", soMaster.getLastMileLineCode(), doc, "text");
        PrintTempUtils.setTempData("EXT1", soMaster.getSoUdfHs1(), doc, "text");
        PrintTempUtils.setTempData("EXT2", soMaster.getSoUdfHs2(), doc, "text");
        PrintTempUtils.setTempData("EXT3", soMaster.getSoUdfHs3(), doc, "text");
        PrintTempUtils.setTempData("PAYMENT_TYPE", soMaster.getSoUdfHs1(), doc, "text");
        PrintTempUtils.setTempData("DELIVERY_ADDRESS", soMaster.getDetailAddress(), doc, "text");

        // 打印相关
        PrintTempUtils.setTempData("PRINT_TIME", DateUtils.getNowDateTimeString(), doc, "text");
        PrintTempUtils.setTempData("PRINT_DATE", DateUtils.getNowDateString(), doc, "text");
        PrintTempUtils.setTempData("PRINT_USER_NAME", currentUser.getUserName(), doc, "text");

        // DateUtils.strToDate(soMaster.getDeliveryTime(),"MM/DD/YY");
        PrintTempUtils.setTempData("DELIVERY_DATE", DateUtils.dateToStr(new Date(), "MM/dd/YY"), doc, "text");

        if (doc.getElementById("FULL_ITEMS_DETAIL_STR") != null) {
            Element parent = doc.getElementById("FULL_ITEMS_DETAIL_STR2");

            Element newEle = parent.getElementById("FULL_ITEMS_DETAIL_STR");
            doc.select("#FULL_ITEMS_DETAIL_STR").remove();
            for (SoDetail soDetail : list) {
                Element newEle2 = newEle.clone();
                PrintTempUtils.setTempData("FULL_ITEMS_DETAIL_STR", soDetail.getItemCode() + "" + soDetail.getItemName() + " * " + soDetail.getOrderQuantity(), newEle2, "text");
                newEle2.appendTo(parent);
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (SoDetail soDetail : list) {
            stringBuilder = stringBuilder.append(soDetail.getItemCode()).append("  ").append(soDetail.getItemName()).append(" X ")
                    .append(soDetail.getOrderQuantity().intValue()).append("; ");
        }
        PrintTempUtils.setTempData("FULL_SKU_STR", stringBuilder.toString(), doc, "text");

        Double ITEM_LIST_TOTAL_QUANTITY = 0.0;

        if (doc.getElementById("ITEM_LIST_DETAIL") != null) {
            Element FULL_ITEMS_DETAIL_TABLE = doc.getElementById("FULL_ITEMS_DETAIL_TABLE");
            Element tbody = FULL_ITEMS_DETAIL_TABLE.getElementsByTag("tBody").get(0);
            Element ITEM_LIST_DETAIL = tbody.getElementById("ITEM_LIST_DETAIL");
            Integer colspan = 0;
            for (SoDetail soDetail : list) {
                Element newEle = ITEM_LIST_DETAIL.clone();
                colspan += PrintTempUtils.setTempData("DETAIL_INDEX", soDetail.getDetailIndex(), newEle, "text");

                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_SKU", soDetail.getItemCode(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_NAME_CN", soDetail.getItemName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_QUANTITY", soDetail.getPickQuantity(), newEle, "text");

                newEle.appendTo(tbody);
                ITEM_LIST_TOTAL_QUANTITY += soDetail.getPickQuantity();
            }

            // berwin 2022-03-24 04-04update 拣货单
            String thisorderID = soMaster.getAllotBillNo();
            if (thisorderID.startsWith("8000")) {
                thisorderID = "9000" + thisorderID.substring(4);
                SoMaster soMaster2 = (SoMaster) this.soMasterService.findBy("billNo", "SO-" + thisorderID);
                String thisordermasterID = soMaster2.getSoMasterId().toString();

                List<SoDetail> list2 = this.soDetailService.findByMasterId(Integer.parseInt(thisordermasterID));

                for (SoDetail soDetail : list2) {
                    Element newEle = ITEM_LIST_DETAIL.clone();
                    colspan += PrintTempUtils.setTempData("DETAIL_INDEX", soDetail.getDetailIndex(), newEle, "text");
                    colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_SKU", soDetail.getItemCode(), newEle, "text");
                    colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_NAME_CN", soDetail.getItemName(), newEle, "text");
                    colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_QUANTITY", soDetail.getOrderQuantity(), newEle, "text");
                    newEle.appendTo(tbody);
                    ITEM_LIST_TOTAL_QUANTITY = Double.valueOf(ITEM_LIST_TOTAL_QUANTITY.doubleValue() + soDetail.getOrderQuantity().doubleValue());
                }
            } else if (thisorderID.startsWith("9000"))// 肉
            {
                thisorderID = "8000" + thisorderID.substring(4);
                SoMaster soMaster2 = (SoMaster) this.soMasterService.findBy("billNo", "SO-" + thisorderID);
                if (null != soMaster2) {
                    String thisordermasterID = soMaster2.getSoMasterId().toString();

                    List<SoDetail> list2 = this.soDetailService.findByMasterId(Integer.parseInt(thisordermasterID));

                    for (SoDetail soDetail : list2) {
                        Element newEle = ITEM_LIST_DETAIL.clone();
                        colspan += PrintTempUtils.setTempData("DETAIL_INDEX", soDetail.getDetailIndex(), newEle, "text");
                        colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_SKU", soDetail.getItemCode(), newEle, "text");
                        colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_NAME_CN", soDetail.getItemName(), newEle, "text");
                        colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_QUANTITY", soDetail.getOrderQuantity(), newEle, "text");
                        newEle.appendTo(tbody);
                        ITEM_LIST_TOTAL_QUANTITY = Double.valueOf(ITEM_LIST_TOTAL_QUANTITY.doubleValue() + soDetail.getOrderQuantity().doubleValue());
                    }
                }
            }

            ITEM_LIST_DETAIL.remove();
        }

        PrintTempUtils.setTempData("ITEM_LIST_TOTAL_QUANTITY", ITEM_LIST_TOTAL_QUANTITY, doc, "text");

        // doc在创建时里面包含了html head body等节点 不需要
        Element labelContent = doc.getElementsByAttributeValue("class", "label-content").get(0);
        String tempHtml = labelContent.toString();
        return tempHtml;
    }

    public String getPrintDataStrForDriverPickOrder(SoMaster soMaster, PrintTempInfo printTempInfo, CurrentUser currentUser, String driverName, String serialNumber, Integer wareID) {
        if (soMaster == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到相应订单！");
        }
        List<SoDetail> list = soDetailService.findPickUPOrderByMasterId(soMaster.getSoMasterId());
        // 获取电子面单模板
        if (printTempInfo == null) {
            printTempInfo = printTempInfoService.findByWare(wareID, "分司机拣货单");
            if (printTempInfo == null) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到匹配的出库单模板");
            }
        }

        String html = printTempInfo.getTempHtml();
        Document doc = Jsoup.parse(html);
        // 删除不显示的
        doc.select(".dis-none").remove();
        // 直线中间蓝点的问题
        doc.select(".line-handle").remove();

        PrintTempUtils.setTempData2("shop_daily_count", "#" + soMaster.getShopDailyCount(), doc, "text");

        PrintTempUtils.setTempData2("WARE_NAME", soMaster.getWareName(), doc, "text");
        PrintTempUtils.setTempData2("order_driverName", driverName, doc, "text");
        PrintTempUtils.setTempData2("order_serialNumber", serialNumber, doc, "text");

        PrintTempUtils.setTempData2("ORDER_NO", soMaster.getAllotBillNo().equals("999") ? soMaster.getBillNo() : soMaster.getAllotBillNo(), doc, "text");
        PrintTempUtils.setTempData2("ORDER_NO_BAR_CODE", soMaster.getAllotBillNo().equals("999") ? soMaster.getBillNo() : soMaster.getAllotBillNo(), doc, "barCode");
        PrintTempUtils.setTempData2("ORDER_NO_QR_CODE", soMaster.getAllotBillNo().equals("999") ? soMaster.getBillNo() : soMaster.getBillNo(), doc, "qrCode");

        PrintTempUtils.setTempData2("SO_BILL_NO", soMaster.getBillNo(), doc, "text");
        PrintTempUtils.setTempData2("SHIP_BILL_NO_BAR_CODE", soMaster.getShipBillCode(), doc, "barCode");
        PrintTempUtils.setTempData2("SHIP_BILL_NO_QR_CODE", soMaster.getShipBillCode(), doc, "qrCode");
        PrintTempUtils.setTempData2("SHIP_BILL_NO", soMaster.getShipBillCode(), doc, "text");

        // PrintTempUtils.setTempData("TRADE_NO", soMaster.getTradeNo(), doc, "text");
        // PrintTempUtils.setTempData("SHOP_NAME", soMaster.getShop(), doc, "text");
        // PrintTempUtils.setTempData("SENDER_NAME", soMaster.getShipLinkMan(), doc, "text");
        // PrintTempUtils.setTempData("SENDER_ADDRESS", soMaster.getShipDetailAddress(), doc, "text");
        // PrintTempUtils.setTempData("SENDER_PROVINCE", soMaster.getShipProvince(), doc, "text");
        // PrintTempUtils.setTempData("SENDER_CITY", soMaster.getShipCity(), doc, "text");
        // PrintTempUtils.setTempData("SENDER_AREA", soMaster.getShipArea(), doc, "text");
        // PrintTempUtils.setTempData("SENDER_MOBILE", soMaster.getShipLinkPhone(), doc, "text");
        // PrintTempUtils.setTempData("SENDER_COUNTRY", soMaster.getShipCountry(), doc, "text");
        // PrintTempUtils.setTempData("SHIP_POST_CODE", soMaster.getShipPostCode(), doc, "text");
        // PrintTempUtils.setTempData("CN_DECLARATION_NAME", soMaster.getCnDeclarationName(), doc, "text");
        // PrintTempUtils.setTempData("DECLARATION_NAME", soMaster.getDeclarationName(), doc, "text");
//
//
        // PrintTempUtils.setTempData("RECEIVER_NAME", soMaster.getLinkMan(), doc, "text");
        // PrintTempUtils.setTempData("RECEIVER_ADDRESS", soMaster.getDetailAddress(), doc, "text");
        // PrintTempUtils.setTempData("RECEIVER_PROVINCE", soMaster.getProvince(), doc, "text");
        // PrintTempUtils.setTempData("RECEIVER_CITY", soMaster.getCity(), doc, "text");
        // PrintTempUtils.setTempData("RECEIVER_AREA", soMaster.getArea(), doc, "text");
        // PrintTempUtils.setTempData("RECEIVER_MOBILE", soMaster.getLinkPhone(), doc, "text");
        // PrintTempUtils.setTempData("RECEIVER_COUNTRY", soMaster.getCountry(), doc, "text");
        // PrintTempUtils.setTempData("LINK_POST_CODE", soMaster.getLinkPostCode(), doc, "text");
//
        // PrintTempUtils.setTempData("ORDER_TOTAL_PRICE", soMaster.getTotalPrice(), doc, "text");
//
//
        // PrintTempUtils.setTempData("LMSC", soMaster.getLastMileSortingCode(), doc, "text");
        // PrintTempUtils.setTempData("LMD", soMaster.getLastMileDestination(), doc, "text");
        // PrintTempUtils.setTempData("LMLC", soMaster.getLastMileLineCode(), doc, "text");
        // PrintTempUtils.setTempData("EXT1", soMaster.getSoUdfHs1(), doc, "text");
        // PrintTempUtils.setTempData("EXT2", soMaster.getSoUdfHs2(), doc, "text");
        // PrintTempUtils.setTempData("EXT3", soMaster.getSoUdfHs3(), doc, "text");
        // PrintTempUtils.setTempData("PAYMENT_TYPE", soMaster.getSoUdfHs1(), doc, "text");
        // PrintTempUtils.setTempData("DELIVERY_ADDRESS", soMaster.getDetailAddress(), doc, "text");
//
//
        //// 打印相关
        // PrintTempUtils.setTempData("PRINT_TIME", DateUtils.getNowDateTimeString(), doc, "text");
        // PrintTempUtils.setTempData("PRINT_DATE", DateUtils.getNowDateString(), doc, "text");
        // PrintTempUtils.setTempData("PRINT_USER_NAME", currentUser.getUserName(), doc, "text");

        // DateUtils.strToDate(soMaster.getDeliveryTime(),"MM/DD/YY");
        PrintTempUtils.setTempData("DELIVERY_DATE", DateUtils.dateToStr(new Date(), "MM/dd/YY"), doc, "text");

        if (doc.getElementById("FULL_ITEMS_DETAIL_STR") != null) {
            Element parent = doc.getElementById("FULL_ITEMS_DETAIL_STR2");

            Element newEle = parent.getElementById("FULL_ITEMS_DETAIL_STR");
            doc.select("#FULL_ITEMS_DETAIL_STR").remove();
            for (SoDetail soDetail : list) {
                Element newEle2 = newEle.clone();
                PrintTempUtils.setTempData("FULL_ITEMS_DETAIL_STR", soDetail.getItemCode() + "" + soDetail.getItemName() + " * " + soDetail.getOrderQuantity(), newEle2, "text");
                newEle2.appendTo(parent);
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (SoDetail soDetail : list) {
            stringBuilder = stringBuilder.append(soDetail.getItemCode()).append("  ").append(soDetail.getItemName()).append(" X ")
                    .append(soDetail.getOrderQuantity().intValue()).append("; ");
        }
        PrintTempUtils.setTempData("FULL_SKU_STR", stringBuilder.toString(), doc, "text");

        Double ITEM_LIST_TOTAL_QUANTITY = 0.0;

        if (doc.getElementById("ITEM_LIST_DETAIL") != null) {
            Element FULL_ITEMS_DETAIL_TABLE = doc.getElementById("FULL_ITEMS_DETAIL_TABLE");
            Element tbody = FULL_ITEMS_DETAIL_TABLE.getElementsByTag("tBody").get(0);
            Element ITEM_LIST_DETAIL = tbody.getElementById("ITEM_LIST_DETAIL");
            Integer colspan = 0;
            for (SoDetail soDetail : list) {
                Element newEle = ITEM_LIST_DETAIL.clone();
                colspan += PrintTempUtils.setTempData("DETAIL_INDEX", soDetail.getDetailIndex(), newEle, "text");

                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_SKU", soDetail.getItemCode(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_NAME_CN", soDetail.getItemName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_QUANTITY", soDetail.getPickQuantity(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_CELL", soDetail.getSpec(), newEle, "text");

                newEle.appendTo(tbody);
                ITEM_LIST_TOTAL_QUANTITY += soDetail.getPickQuantity();
            }

            // berwin 2022-8 需求又改了
            // //berwin 2022-03-24 04-04update 拣货单
            // String thisorderID = soMaster.getAllotBillNo();
            // if(thisorderID.startsWith("8000"))
            // {
            // thisorderID = "9000"+thisorderID.substring(4);
            // SoMaster soMaster2 = (SoMaster)this.soMasterService.findBy("billNo", "SO-"+thisorderID);
            // String thisordermasterID = soMaster2.getSoMasterId().toString();
            //
            //
            // List<SoDetail> list2 = this.soDetailService.findPickUPOrderByMasterId(Integer.parseInt(thisordermasterID));
            //
            // for (SoDetail soDetail : list2) {
            // Element newEle = ITEM_LIST_DETAIL.clone();
            // colspan += PrintTempUtils.setTempData("DETAIL_INDEX", soDetail.getDetailIndex(), newEle, "text");
            // colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_SKU", soDetail.getItemCode(), newEle, "text");
            // colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_NAME_CN", soDetail.getItemName(), newEle, "text");
            // colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_QUANTITY", soDetail.getOrderQuantity(), newEle, "text");
            // colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_CELL", soDetail.getSpec(), newEle, "text");
            // newEle.appendTo(tbody);
            // ITEM_LIST_TOTAL_QUANTITY = Double.valueOf(ITEM_LIST_TOTAL_QUANTITY.doubleValue() + soDetail.getOrderQuantity().doubleValue());
            // }
            // }else if(thisorderID.startsWith("9000"))//肉
            // {
            // thisorderID = "8000"+thisorderID.substring(4);
            // SoMaster soMaster2 = (SoMaster)this.soMasterService.findBy("billNo", "SO-"+thisorderID);
            // if(null != soMaster2)
            // {
            // String thisordermasterID = soMaster2.getSoMasterId().toString();
            //
            //
            // List<SoDetail> list2 = this.soDetailService.findPickUPOrderByMasterId(Integer.parseInt(thisordermasterID));
            //
            // for (SoDetail soDetail : list2) {
            // Element newEle = ITEM_LIST_DETAIL.clone();
            // colspan += PrintTempUtils.setTempData("DETAIL_INDEX", soDetail.getDetailIndex(), newEle, "text");
            // colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_SKU", soDetail.getItemCode(), newEle, "text");
            // colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_NAME_CN", soDetail.getItemName(), newEle, "text");
            // colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_QUANTITY", soDetail.getOrderQuantity(), newEle, "text");
            // colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_CELL", soDetail.getSpec(), newEle, "text");
            // newEle.appendTo(tbody);
            // ITEM_LIST_TOTAL_QUANTITY = Double.valueOf(ITEM_LIST_TOTAL_QUANTITY.doubleValue() + soDetail.getOrderQuantity().doubleValue());
            // }
            // }
            // }

            ITEM_LIST_DETAIL.remove();
        }

        PrintTempUtils.setTempData("ITEM_LIST_TOTAL_QUANTITY", ITEM_LIST_TOTAL_QUANTITY, doc, "text");

        // doc在创建时里面包含了html head body等节点 不需要
        Element labelContent = doc.getElementsByAttributeValue("class", "label-content").get(0);
        String tempHtml = labelContent.toString();
        return tempHtml;
    }

    public String getDzmdStr(String billNo) {
        // 获取出库单信息
        if (billNo == null || billNo.equals("")) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "单号异常");
        }
        SoMaster soMaster = soMasterService.findBy("billNo", billNo);
        if (soMaster == null) {
            SeedingCell seedingCell = seedingCellService.findBy("seedingCellCode", billNo);
            if (seedingCell != null) {
                soMaster = soMasterService.findBy("billNo", seedingCell.getBillNo());
            }
        }
        if (soMaster == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到相应订单！");
        }
        List<SoDetail> list = soDetailService.findByBillNo(billNo);
        // 获取电子面单模板
        PrintTempInfo printTempInfo = printTempInfoService.findByWareAndShip(soMaster.getWareId(), soMaster.getShipCode());
        if (printTempInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到匹配的电子面单模板");
        }
        String html = printTempInfo.getTempHtml();
        Document doc = Jsoup.parse(html);
        // 删除不显示的
        doc.select(".dis-none").remove();
        // 直线中间蓝点的问题
        doc.select(".line-handle").remove();
        if (soMaster.getTotalPrice() == null) {
            doc.select(".COD").remove();
            doc.select(".COD_IMG").remove();
        }

        if (soMaster.getShipBillCode() == null || soMaster.getShipBillCode().trim().equals("")) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "运单号为空!");
        }

        PrintTempUtils.setTempData2("SHIP_BILL_NO_BAR_CODE", soMaster.getShipBillCode(), doc, "barCode");
        PrintTempUtils.setTempData2("SHIP_BILL_NO_QR_CODE", soMaster.getShipBillCode(), doc, "qrCode");// TODO xuesinuo 打印电子面单 没有配套模版
        PrintTempUtils.setTempData2("SHIP_BILL_NO", soMaster.getShipBillCode(), doc, "text");
        PrintTempUtils.setTempData("TRADE_NO", soMaster.getTradeNo(), doc, "text");
        PrintTempUtils.setTempData("SHOP_NAME", soMaster.getShop(), doc, "text");
        PrintTempUtils.setTempData("SENDER_NAME", soMaster.getShipLinkMan(), doc, "text");
        PrintTempUtils.setTempData("SENDER_ADDRESS", soMaster.getShipDetailAddress(), doc, "text");
        PrintTempUtils.setTempData("SENDER_PROVINCE", soMaster.getShipProvince(), doc, "text");
        PrintTempUtils.setTempData("SENDER_CITY", soMaster.getShipCity(), doc, "text");
        PrintTempUtils.setTempData("SENDER_AREA", soMaster.getShipArea(), doc, "text");
        PrintTempUtils.setTempData("SENDER_MOBILE", soMaster.getShipLinkPhone(), doc, "text");
        PrintTempUtils.setTempData("SENDER_COUNTRY", soMaster.getShipCountry(), doc, "text");
        PrintTempUtils.setTempData("SHIP_POST_CODE", soMaster.getShipPostCode(), doc, "text");
        PrintTempUtils.setTempData("CN_DECLARATION_NAME", soMaster.getCnDeclarationName(), doc, "text");
        PrintTempUtils.setTempData("DECLARATION_NAME", soMaster.getDeclarationName(), doc, "text");

        PrintTempUtils.setTempData("RECEIVER_NAME", soMaster.getLinkMan(), doc, "text");
        PrintTempUtils.setTempData("RECEIVER_ADDRESS", soMaster.getDetailAddress(), doc, "text");
        PrintTempUtils.setTempData("RECEIVER_PROVINCE", soMaster.getProvince(), doc, "text");
        PrintTempUtils.setTempData("RECEIVER_CITY", soMaster.getCity(), doc, "text");
        PrintTempUtils.setTempData("RECEIVER_AREA", soMaster.getArea(), doc, "text");
        PrintTempUtils.setTempData("RECEIVER_MOBILE", soMaster.getLinkPhone(), doc, "text");
        PrintTempUtils.setTempData("RECEIVER_COUNTRY", soMaster.getCountry(), doc, "text");
        PrintTempUtils.setTempData("LINK_POST_CODE", soMaster.getLinkPostCode(), doc, "text");

        PrintTempUtils.setTempData("ORDER_TOTAL_PRICE", soMaster.getTotalPrice(), doc, "text");

        PrintTempUtils.setTempData("LMSC", soMaster.getLastMileSortingCode(), doc, "text");
        PrintTempUtils.setTempData("LMD", soMaster.getLastMileDestination(), doc, "text");
        PrintTempUtils.setTempData("LMLC", soMaster.getLastMileLineCode(), doc, "text");
        PrintTempUtils.setTempData("EXT1", soMaster.getSoUdfHs1(), doc, "text");
        PrintTempUtils.setTempData("EXT2", soMaster.getSoUdfHs2(), doc, "text");
        PrintTempUtils.setTempData("EXT3", soMaster.getSoUdfHs3(), doc, "text");

        if (doc.getElementById("FULL_ITEMS_DETAIL_STR") != null) {
            Element parent = doc.getElementById("FULL_ITEMS_DETAIL_STR2");

            Element newEle = parent.getElementById("FULL_ITEMS_DETAIL_STR");
            doc.select("#FULL_ITEMS_DETAIL_STR").remove();
            for (SoDetail soDetail : list) {
                Element newEle2 = newEle.clone();
                PrintTempUtils.setTempData("FULL_ITEMS_DETAIL_STR", soDetail.getItemCode() + "" + soDetail.getItemName() + " * " + soDetail.getOrderQuantity(), newEle2, "text");
                newEle2.appendTo(parent);
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (SoDetail soDetail : list) {
            stringBuilder = stringBuilder.append(soDetail.getItemCode()).append("  ").append(soDetail.getItemName()).append(" X ")
                    .append(soDetail.getOrderQuantity().intValue()).append("; ");
        }
        PrintTempUtils.setTempData("FULL_SKU_STR", stringBuilder.toString(), doc, "text");
        if (doc.getElementById("ITEM_LIST_DETAIL") != null) {
            Element FULL_ITEMS_DETAIL_TABLE = doc.getElementById("FULL_ITEMS_DETAIL_TABLE");
            Element tbody = FULL_ITEMS_DETAIL_TABLE.getElementsByTag("tBody").get(0);
            Element ITEM_LIST_DETAIL = tbody.getElementById("ITEM_LIST_DETAIL");
            Integer colspan = 0;
            for (SoDetail soDetail : list) {
                Element newEle = ITEM_LIST_DETAIL.clone();
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_SKU", soDetail.getItemCode(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_NAME_CN", soDetail.getItemName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_QUANTITY", soDetail.getOrderQuantity(), newEle, "text");

                newEle.appendTo(tbody);
            }
            ITEM_LIST_DETAIL.remove();
        }

        // doc在创建时里面包含了html head body等节点 不需要
        Element labelContent = doc.getElementsByAttributeValue("class", "label-content").get(0);
        String tempHtml = labelContent.toString();
        return tempHtml;
    }

    @Value("${file.staticFileDir}")
    private String staticFileDir;//

    /**
     * 打印当前波次的所有出库单
     */
    @GetMapping("/getBillPrintData")
    @Transactional
    @Authority("so_masters_getBillPrintData")
    public Result getBillPrintData(Integer waveId, Integer wareId, @User CurrentUser currentUser) throws IOException {
        // 获取电子面单模板
        PrintTempInfo printTempInfo = printTempInfoService.findByWare(wareId, "出库单");
        if (printTempInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到匹配的出库单模板");
        }
        List<Map> dataList = new ArrayList<>();
        List<SoMasterDto> list = soMasterService.findByWaveId(waveId, wareId);
        for (SoMasterDto soMaster : list) {
            String str = getPrintDataStr(soMaster, printTempInfo, currentUser);
            Map<String, String> data = new HashMap<>();
            data.put("html", str);

            data.put("pdfUrl", soMasterService.getPdfUrl(soMaster));
            data.put("soPickTask", soMasterService.soPickTask(soMaster, null, null));

            data.put("index", soMaster.getShopDailyCount() + "");
            dataList.add(data);
        }
        return ResultGenerator.genSuccessResult(dataList);
    }

    /**
     * FIXME xuesinuo 没有在使用
     *
     * 打印当前波次的所有出库单
     */
    @GetMapping("/getSoPrintData")
    @Transactional
    @Authority("so_masters_getSoPrintData")
    public Result getSoPrintData(Integer soMasterId, Integer wareId, @User CurrentUser currentUser) throws IOException {
        // 获取电子面单模板
        PrintTempInfo printTempInfo = printTempInfoService.findByWare(wareId, "出库单");
        if (printTempInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到匹配的出库单模板");
        }
        List<Map> dataList = new ArrayList<>();
        SoMaster soMaster = soMasterService.findById(soMasterId);
        // soMaster.setPrintState(soMaster.getPrintState()+1);
        // soMasterService.updateState(soMaster);
        String str = getPrintDataStr(soMaster, printTempInfo, currentUser);
        Map<String, String> data = new HashMap<>();
        data.put("html", str);
        data.put("pdfUrl", soMasterService.getPdfUrl(soMaster));
        data.put("soPickTask", soMasterService.soPickTask(soMaster, null, null));
        data.put("index", soMaster.getShopDailyCount() + "");
        dataList.add(data);
        return ResultGenerator.genSuccessResult(data);
    }

    public static void sort(Integer[] arr) {
        if (arr.length >= 2) {
            for (int i = 1; i < arr.length; i++) {
                // 挖出一个要用来插入的值,同时位置上留下一个可以存新的值的坑
                int x = arr[i];
                int j = i - 1;
                // 在前面有一个或连续多个值比x大的时候,一直循环往前面找,将x插入到这串值前面
                while (j >= 0 && arr[j] > x) {
                    // 当arr[j]比x大的时候,将j向后移一位,正好填到坑中
                    arr[j + 1] = arr[j];
                    j--;
                }
                // 将x插入到最前面
                arr[j + 1] = x;
            }
        }
    }

    // @ApiOperation(value = "绿色打印：根据输入订单号打印", notes = "绿色打印：根据输入订单号打印")
    // @ApiImplicitParams({
    // @ApiImplicitParam(name = "access-token", value = "token", paramType = "header", dataType = "String", required = true)
    // })
    // @PostMapping("/getSoPrintDataAllForOrderIDS")
    // @Transactional
    // public Result getSoPrintDataAllForOrderIDS(@RequestBody SoPrintDataAllRequest soPrintDataAllRequest, @User CurrentUser currentUser) throws IOException {
    // Integer wareID = soPrintDataAllRequest.getWareId();
    //// 获取电子面单模板
    // PrintTempInfo printTempInfo = printTempInfoService.findByWare(wareID, "出库单");
    // if (printTempInfo == null) {
    // throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到匹配的出库单模板");
    // }
    //
    // List<Map> dataList = new ArrayList<>();
    // String soMasterIds = soPrintDataAllRequest.getSoOrderIds().trim();
    //
    // if (soMasterIds == null || soMasterIds.isEmpty()) {
    // throw new ServiceException(CommonCode.SERVICE_ERROR, "未输入订单号");
    // }
    // soMasterIds = soMasterIds.trim();
    //// Integer orderid;
    // for (String soMasterId:soMasterIds.split(",") ) {
    // if(null == soMasterId || soMasterId.contains("A") || soMasterId.contains("B") ) {
    // throw new ServiceException(CommonCode.SERVICE_ERROR, "订单号组中出现非法订单号");
    // }
    // soMasterId = soMasterId.trim();
    // if(soMasterId.startsWith(this.code1)) {
    // soMasterId = soMasterId.substring(this.code1.length());
    // }else if(soMasterId.startsWith(this.code2)) {
    // soMasterId = soMasterId.substring(this.code2.length());
    // }
    //
    // if( !soMasterId.isEmpty() ) {
    // //orderid = Integer.parseInt(soMasterId);
//	            SoMaster soMaster = soMasterService.findByIdAndWareID(soMasterId,wareID.toString(),orderidLength);
//	            if(soMaster == null) {
//	            	throw new ServiceException(CommonCode.SERVICE_ERROR, soMasterId + "订单号找不到对应订单");
//	            }
//	            
//	            soMaster.setPrintState(soMaster.getPrintState()+1);
//	            soMasterService.updateState(soMaster);
//	            String str = getPrintDataStr(soMaster,printTempInfo,currentUser);
//	            Map<String,String> data= new HashMap<>();
//	            data.put("html",str);
//	            data.put("pdfUrl",soMasterService.getPdfUrl(soMaster));
//	            data.put("soPickTask",soMasterService.soPickTask(soMaster));
//	            data.put("index",soMaster.getShopDailyCount()+"");
//	            dataList.add(data);
    // }
    // }
    // return ResultGenerator.genSuccessResult(dataList);
    // }
    //
    // @ApiOperation(value = "打印当前出库标签", notes = "打印当前所有出库标签")
    // @ApiImplicitParams({
    // @ApiImplicitParam(name = "access-token", value = "token", paramType = "header", dataType = "String", required = true)
    // })
    // @GetMapping("/getPickTaskLabelAllForOrderIDS")
    // @Transactional
    // public Result getPickTaskLabelAllForOrderIDS(String soMasterId,Integer wareId, @User CurrentUser currentUser) throws IOException {
    //// 未找到匹配的拣货标签模板
    // PrintTempInfo printTempInfo = printTempInfoService.findByWare(wareId, "拣货标签");
    // if (printTempInfo == null) {
    // throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到匹配的拣货标签模板");
    // }
    // if (soMasterId == null ) {
    // throw new ServiceException(CommonCode.SERVICE_ERROR, "未输入订单号");
    // }
    //
    // if(soMasterId.startsWith(this.code1)) {
    // soMasterId = soMasterId.substring(this.code1.length());
    // }else if(soMasterId.startsWith(this.code2)) {
    // soMasterId = soMasterId.substring(this.code2.length());
    // }
    //
    // List<Map> dataList = new ArrayList<>();
    // SoMaster soMaster = soMasterService.findByIdAndWareID(soMasterId.toString(),wareId.toString(),orderidLength);//findById(soMasterId);
    // if(soMaster == null) {
    // throw new ServiceException(CommonCode.SERVICE_ERROR, soMasterId + "订单号找不到对应订单");
    // }
    // List<SoDetail> details = soDetailService.findByBillNo(soMaster.getBillNo());
    // Double orderAllQuantity=0.0;
    // for (SoDetail soDetail: details ) {
    // orderAllQuantity = orderAllQuantity+soDetail.getOrderQuantity();
//
    // }
    // for (SoDetail soDetail: details ) {
    // Double quantity = soDetail.getOrderQuantity();
    // for(int i=0;i<quantity.intValue();i++){
    // String str = getPickPrintString(soMaster,soDetail,orderAllQuantity,i+1,printTempInfo,currentUser);
    // Map<String,String> data= new HashMap<>();
    // data.put("html",str);
    // data.put("index",soMaster.getShopDailyCount()+"-"+soDetail.getDetailIndex()+"-"+(i+1));
    // dataList.add(data);
    // }
    // }
    // return ResultGenerator.genSuccessResult(dataList);
    // }

    /**
     * 批量打印单
     */
    @PostMapping("/getSoPrintDataAllForDriver")
    @Transactional
    @Authority("so_masters_getSoPrintDataAllForDriver")
    public Result getSoPrintDataAllForDriver(@RequestBody xhcSoPrintDataAllRequest xhcPrintRequest, @User CurrentUser currentUser) throws IOException {
        Integer wareId = xhcPrintRequest.getWareId();
        List<Map> printInfos = xhcPrintRequest.getPrintInfos();

        // 获取电子面单模板
        PrintTempInfo printTempInfo = printTempInfoService.findByWare(wareId, "分司机出库单");
        if (printTempInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到匹配的出库单模板");
        }

        List<Map> dataList = new ArrayList<>();
        List<String> billNoSet = new ArrayList<>();

        // Integer[] soMasterIds = soPrintDataAllRequest.getSoMasterIds();
        // sort(soMasterIds);
        for (Map soMasterMap : printInfos) {
            Integer soMasterId = Convert.toInt(soMasterMap.get("soMasterId"));
            String driverName = Convert.toStr(soMasterMap.get("driverName"));
            String serialNumber = Convert.toStr(soMasterMap.get("index"));

            String billno = Convert.toStr(soMasterMap.get("billNo"));

            billno = billno.replace("SO-8000", "").replace("SO-9000", "").replace("SO-", "");
            if (billNoSet.contains(billno)) {
                continue;
            } else {
                billNoSet.add(billno);
            }

            SoMaster soMaster = soMasterService.findById(soMasterId);
            // soMaster.setPrintState(soMaster.getPrintState()+1);
            // soMasterService.updateState(soMaster);
            String str = getPrintDataStr(soMaster, printTempInfo, currentUser, driverName, serialNumber);
            Map<String, String> data = new HashMap<>();
            data.put("html", str);
            data.put("pdfUrl", soMasterService.getPdfUrl(soMaster));
            // data.put("soPickTask",strsoPickTask);
            // data.put("soPickTask",soMasterService.soPickTask(soMaster,driverName,serialNumber));
            data.put("index", soMaster.getShopDailyCount() + "");
            dataList.add(data);
        }
        return ResultGenerator.genSuccessResult(dataList);
    }

    /**
     * 打印当前标签
     */
    @GetMapping("/getPickTaskLabelForDriver")
    @Transactional
    @Authority("so_masters_getPickTaskLabelForDriver")
    public Result getPickTaskLabelForDriver(Integer soMasterId, String driverName, String index, Integer wareId, Boolean simplify, @User CurrentUser currentUser) throws IOException {
        Long time1 = System.currentTimeMillis();
        String serialNumber = index;
        // 未找到匹配的拣货标签模板
        PrintTempInfo printTempInfo = printTempInfoService.findByWare(wareId, "分司机拣货标签");
        if (printTempInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到匹配的拣货标签模板");
        }

        List<Map> dataList = new ArrayList<>();
        List<String> dataListSKU = new ArrayList<>();
        SoMaster soMaster = soMasterService.findById(soMasterId);
        // List<SoDetail> details = soDetailService.pickupfindByBillNo(soMaster.getBillNo());
        List<SoDetail> details = soDetailService.findPickUPOrderByMasterId(soMaster.getSoMasterId());
        Double orderAllQuantity = 0.0;
        Map<String, Double> skuToQuantity = new HashMap<>();// 此SKU在此Order中统计的总数
        for (SoDetail soDetail : details) {
//            if (!dataListSKU.contains(soDetail.getItemCode())) {}// 2023-03-29允许SKU重复的情况出现，都打印
            dataListSKU.add(soDetail.getItemCode());
            orderAllQuantity = orderAllQuantity + soDetail.getPickQuantity();
            Double skuQuantity = Np.i(skuToQuantity.get(soDetail.getItemCode())).o(0.0);
            skuToQuantity.put(soDetail.getItemCode(), skuQuantity + soDetail.getPickQuantity());
        }

        int itemcount = 1;
        dataListSKU.clear();
        for (SoDetail soDetail : details) {
            // if(dataListSKU.contains(soDetail.getItemCode())) {
            // continue;
            // }
            dataListSKU.add(soDetail.getItemCode());
            Double quantity = soDetail.getPickQuantity();
            if (simplify == null || !simplify) {
                for (int i = 0; i < quantity.intValue(); i++) {
                    String str = getPickPrintString(soMaster, soDetail, orderAllQuantity, skuToQuantity.get(soDetail.getItemCode()), String.valueOf(i + 1), printTempInfo, currentUser, driverName,
                            serialNumber);
                    Map<String, String> data = new HashMap<>();
                    data.put("html", str);
                    data.put("index", soMaster.getShopDailyCount() + "-" + soDetail.getDetailIndex() + "-" + (i + 1));
                    dataList.add(data);
                }
            } else {
                String str = getPickPrintString(soMaster, soDetail, orderAllQuantity, skuToQuantity.get(soDetail.getItemCode()), "n", printTempInfo, currentUser, driverName, serialNumber);
                Map<String, String> data = new HashMap<>();
                data.put("html", str);
                data.put("index", soMaster.getShopDailyCount() + "-" + soDetail.getDetailIndex() + "-" + "111");
                dataList.add(data);
            }
        }
        // DoLog
        Long time2 = System.currentTimeMillis();
        DataOperationLog<Object> doLog = new DataOperationLogTool.DataOperationLog<>();
        // DoLog必填项
        doLog.setModule(DataOperationLogModule.PRINT);
        doLog.setOperation(DataOperationLogOperation.PRINT_SO4);
        doLog.setOperationUserId(currentUser.getUserId().longValue());
        doLog.setOperationUserName(currentUser.getUserName());
        // DoLog选填项
        SimpleMap befortData = SimpleMap.init("master", soMaster).append("details", details).append("simplify", simplify).append("index", index);
        doLog.setBeforeData(befortData);
        doLog.setAfterData(dataList);
        doLog.setOperationCode(soMaster.getBillNo());
        doLog.setOperationId(soMasterId.longValue());
        doLog.setTimer(time2 - time1);
        doLog.setWareId(wareId.longValue());
        doLogger.save(doLog);
        return ResultGenerator.genSuccessResult(dataList);
    }


    @Data
    private static class PickTaskPrintParam {
        List<PickTaskPrintItemParam> pickTaskPrintParamList;
    }

    @Data
    private static class PickTaskPrintItemParam {
        private Integer soMasterId;
        private String driverName;
        private Integer index;
        private Integer wareId;
    }

    /**
     * 一个订单的两件套打印数据
     *
     * @author xuesinuo
     *
     */
    @Data
    public static class PickTaskPrintInfoResult {
        private PickTaskPrintInfo pickTaskInfo;
        private List<PickTaskPrintDetailInfo> detailList;
        private List<PickTaskPrintItemInfo> itemList;
    }

    @Data
    public static class PickTaskPrintInfo {
        /** 仓库名 */
        private String wareName;
        /** SO-订单号 */
        private String billNo;
        /** PQ-单号 */
        private String allotBillNo;
        /** 订单日序号（页面Serial.) */
        private Integer shopDailCount;
        /** 司机 */
        private String driverName;
        /** 配送顺序（页面Deliver No.） */
        private Integer serial;
        /** sku数 Group(sku) */
        private Integer skuNum;
        /** 产品总数 Sum(Qty) */
        private Double itemNum;
        /** 打印时间 */
        private Date printDate;
    }

    @Data
    public static class PickTaskPrintDetailInfo {
        private String cellCode;
        private String itemName;
        private Double qty;
    }

    @Data
    public static class PickTaskPrintItemInfo {
        /** 产品序号 */
        private Integer detailIndex;
        /** 数量 */
        private Integer strIndex;
        /** 总数 */
        private Double skuQuantity;
        /** 订单中全SKU总数 */
        private Double orderAllQuantity;
        private String itemName;
        private String itemCode;
        private Boolean hasWeight;
        private String qrCode;
        private String cellCode;
    }

    /**
     * 默认打印两件套，原生打印（获取数据）
     */
    @PostMapping("/getPickTaskLabelForDriverData")
    @Transactional
    @Authority("so_masters_getPickTaskLabelForDriverData")
    public Result getPickTaskLabelForDriverData(@RequestBody PickTaskPrintParam param, @User CurrentUser currentUser) throws IOException {

        Date now = new Date();
        List<PickTaskPrintInfoResult> reList = new ArrayList<>();
        if (param == null || param.getPickTaskPrintParamList() == null || param.getPickTaskPrintParamList().isEmpty()) {
            throw new ServiceException(CommonCode.PARAMETER_ERROR, "缺少参数pickTaskPrintParamList");
        }
        for(PickTaskPrintItemParam itemParam : param.getPickTaskPrintParamList()) {
            PickTaskPrintInfoResult pickTaskPrintInfoResult = new PickTaskPrintInfoResult();
            pickTaskPrintInfoResult.setDetailList(new ArrayList<>());
            pickTaskPrintInfoResult.setItemList(new ArrayList<>());

            SoMaster soMaster = soMasterService.findById(itemParam.getSoMasterId());
            List<SoDetail> pickList = soDetailService.findPickUPOrderByMasterId(soMaster.getSoMasterId());
            Double orderAllQuantity = 0.0;
            Map<String, Double> skuToQuantity = new LinkedHashMap<>();// 此SKU在此Order中统计的总数
            for (SoDetail soDetailPick : pickList) {
                orderAllQuantity = orderAllQuantity + soDetailPick.getPickQuantity();
                Double skuQuantity = Np.i(skuToQuantity.get(soDetailPick.getItemCode())).o(0.0);
                skuToQuantity.put(soDetailPick.getItemCode(), skuQuantity + soDetailPick.getPickQuantity());
                // 遇到空货位，进行货位预测
                if (StrUtil.isBlank(soDetailPick.getSpec())) {
                    SoMaster so = soMasterService.findByBillNo(soDetailPick.getBillNo());
                    String historyPickCellCode = inventoryTransactService.findHistoryPickCellBySku(soDetailPick.getItemCode(), so.getWareId());
                    soDetailPick.setSpec(Np.i(historyPickCellCode).x(x -> "*" + x + "*").o());
                }
            }

            // 第一页标签
            PickTaskPrintInfo pickTaskPrintInfo = new PickTaskPrintInfo();
            pickTaskPrintInfo.setWareName(soMaster.getWareName());
            pickTaskPrintInfo.setBillNo(soMaster.getBillNo());
            pickTaskPrintInfo.setSerial(itemParam.getIndex());
            pickTaskPrintInfo.setDriverName(itemParam.getDriverName());
            pickTaskPrintInfo.setShopDailCount(soMaster.getShopDailyCount());
            pickTaskPrintInfo.setSkuNum(skuToQuantity.size());
            pickTaskPrintInfo.setItemNum(orderAllQuantity);
            pickTaskPrintInfo.setPrintDate(now);
            pickTaskPrintInfo.setAllotBillNo(soMaster.getAllotBillNo());
            pickTaskPrintInfoResult.setPickTaskInfo(pickTaskPrintInfo);
            // 表格标签
            for (SoDetail soDetailPick : pickList) {
                PickTaskPrintDetailInfo pickTaskPrintDetailInfo = new PickTaskPrintDetailInfo();
                pickTaskPrintDetailInfo.setCellCode(soDetailPick.getSpec());
                pickTaskPrintDetailInfo.setItemName(soDetailPick.getItemName());
                pickTaskPrintDetailInfo.setQty(soDetailPick.getPickQuantity());
                pickTaskPrintInfoResult.getDetailList().add(pickTaskPrintDetailInfo);
            }
            // SKU Label
            Map<String, Integer> skuToSkuIndex = new HashMap<>();
            for (SoDetail soDetailPick : pickList) {
                Double quantity = soDetailPick.getPickQuantity();
                for (int i = 0; i < quantity.intValue(); i++) {
                    PickTaskPrintItemInfo pickTaskPrintItemInfo = new PickTaskPrintItemInfo();
                    pickTaskPrintItemInfo.setDetailIndex(soDetailPick.getDetailIndex());
                    pickTaskPrintItemInfo.setStrIndex(i + 1);
                    pickTaskPrintItemInfo.setSkuQuantity(skuToQuantity.get(soDetailPick.getItemCode()));
                    pickTaskPrintItemInfo.setOrderAllQuantity(orderAllQuantity);
                    pickTaskPrintItemInfo.setItemName(soDetailPick.getItemName());
                    pickTaskPrintItemInfo.setItemCode(soDetailPick.getItemCode());
                    Boolean hasWeight = Np.i(itemInfoService.findByItemCode(soDetailPick.getItemCode())).x(x -> x.getWeighting()).eq(true);
                    pickTaskPrintItemInfo.setHasWeight(hasWeight);
                    QrEntity qrEntity = new QrEntity();
                    qrEntity.setSo(soMaster.getBillNo());
                    qrEntity.setSku(soDetailPick.getItemCode());
                    if (skuToSkuIndex.get(soDetailPick.getItemCode()) == null) {
                        skuToSkuIndex.put(soDetailPick.getItemCode(), 1);
                    } else {
                        skuToSkuIndex.put(soDetailPick.getItemCode(), skuToSkuIndex.get(soDetailPick.getItemCode()) + 1);
                    }
                    qrEntity.setSkui(skuToSkuIndex.get(soDetailPick.getItemCode()).toString());
                    DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
                    qrEntity.setSkuqty(decimalFormat.format(skuToQuantity.get(soDetailPick.getItemCode())));
                    pickTaskPrintItemInfo.setQrCode(qrEntity.entityToString(QrStringType.SIMPLE_STRING));
                    pickTaskPrintItemInfo.setCellCode(soDetailPick.getSpec());
                    pickTaskPrintInfoResult.getItemList().add(pickTaskPrintItemInfo);
                }
            }

            reList.add(pickTaskPrintInfoResult);
        }
        return ResultGenerator.genSuccessResult(SimpleMap.init("pickTaskList", reList));
    }



    @Data
    public static class BcGroupPrintParam {
        /** 波次组号 */
        private String pickBatchGroupNo;
        /** 仓库id */
        // FIXME 2023-07-20 存入数据库？
        private Integer wareId;
    }

    @Data
    public static class BcPrintInfoResult {
        private BcInfo bcInfo;
        private List<BcDetailInfo> bcDetailList;
        private List<BcItemInfo> bcItemList;
    }

    @Data
    public static class BcInfo {
        /** 波次号 */
        private String pickBatchId;
        /** 波次序号 */
        private String bcNumber;
        /** 仓库名 */
        private String wareName;
        /** 订单总数 */
        private Integer bcOrder;
        /** sku数 Group(sku) */
        private Integer skuNum;
        /** 产品总数 Sum(Qty) */
        private Double itemNum;
        private String driverName;
        private Date printDate;
        /**
         * 拣货容器（笼车还是板车）
         */
        private String containerType;
    }

    @Data
    public static class BcDetailInfo {
        private String cellCode;
        private String itemName;
        private Integer qty;
    }

    @Data
    public static class BcItemInfo {
        /** 配送顺序 */
        private Integer orderIndex;
        /** 单号 */
        private String allotBillNo;
        /** 产品序号 */
        private Integer detailIndex;
        /** 数量 */
        private Integer strIndex;
        /** 总数 */
        private Integer skuQuantity;
        /** 订单中全SKU总数 */
        private Integer orderAllQuantity;
        /** L R */
        private String position;
        private String itemName;
        private String itemCode;
        private Boolean hasWeight;
        private String qrCode;
        private String cellCode;
        /** 波次Item状态 0新增 1完成 */
        private Integer status;
        /** 装车顺序 */
        private Integer loadIndex;
    }

    /**
     * 获取波次打印数据
     */
    @PostMapping("/getBcPrintInfo")
    @Authority("so_masters_getBcPrintInfo")
    public Result getBcPrintInfo(@RequestBody BcGroupPrintParam param, @User CurrentUser currentUser) throws IOException {
        Date now = new Date();
        List<BcPrintInfoResult> reList = new ArrayList<BcPrintInfoResult>();
        PickBatchGroup pickBatchGroup = pickBatchGroupService.getByNo(param.getPickBatchGroupNo());
        List<PickBatch> pickBatchList = pickBatchService.getByGroupNo(param.getPickBatchGroupNo());

        List<PickBatchDetail> pickBatchDetailList = pickBatchDetailService.findByPickBatchGroupNo(param.getPickBatchGroupNo());
        List<PickBatchItem> pickBatchItemList = pickBatchItemService.getByPickBatchGroupNo(param.getPickBatchGroupNo());
        //克隆一个List计算装车顺序
        List<PickBatchItem> cloneItemList = new ArrayList<>(pickBatchItemList);
        Collections.sort(cloneItemList, Comparator
                .comparingInt(PickBatchItem::getDeliverIndex).reversed()
                .thenComparing(PickBatchItem::getQrCode)
        );
        Map<String, Integer> loadIndexMap = new HashMap<>();
        for (int i = 0; i < cloneItemList.size(); i++) {
            loadIndexMap.put(cloneItemList.get(i).getQrCode(), i+1);
        }

        Map<String,Map<String, Integer>> qrCodeSo2Sku2Index = new HashMap<>();
        Integer bcNumber = 1;
        WareInfo wareInfo = wareInfoService.findById(param.getWareId());
        for (PickBatch pickBatch : pickBatchList) {
            List<PickBatchDetail> thisPickBatchDetailList = pickBatchDetailList.stream().filter(x -> x.getPickBatchNo().equals(pickBatch.getPickBatchNo())).collect(Collectors.toList());
            List<PickBatchItem> thisPickBatchItemList = pickBatchItemList.stream().filter(x -> x.getPickBatchNo().equals(pickBatch.getPickBatchNo())).collect(Collectors.toList());
            Set<String> thisPickBatchSkuList = thisPickBatchItemList.stream().map(x -> x.getItemCode()).collect(Collectors.toSet());
            BcPrintInfoResult result = new BcPrintInfoResult();
            BcInfo bcInfo = new BcInfo();
            List<BcDetailInfo> bcDetailList = new ArrayList<>();
            List<BcItemInfo> bcItemList = new ArrayList<>();
            result.setBcInfo(bcInfo);
            result.setBcDetailList(bcDetailList);
            result.setBcItemList(bcItemList);

            // 第一部分：第一页
            bcInfo.setPrintDate(now);
            bcInfo.setPickBatchId(pickBatch.getPickBatchNo());
            bcInfo.setBcNumber((bcNumber++).toString());
            bcInfo.setWareName(Np.i(wareInfo).x(x -> x.getWareName()).o());
            bcInfo.setBcOrder(pickBatchGroup.getTotalOrderCount());
            bcInfo.setSkuNum(thisPickBatchSkuList.size());
            bcInfo.setItemNum((double) thisPickBatchItemList.size());
            bcInfo.setDriverName(pickBatchGroup.getDriverName());
            bcInfo.setContainerType(pickBatch.getContainerType());
            // 第二部分：表格
            for (PickBatchDetail pickBatchDetail : thisPickBatchDetailList) {
                BcDetailInfo bcDetailInfo = new BcDetailInfo();
                bcDetailInfo.setCellCode(pickBatchDetail.getCellCode());
                // 遇到空货位，进行货位预测
                if (StrUtil.isBlank(pickBatchDetail.getCellCode())) {
                    PickBatchItem oneItem = pickBatchItemList.stream().filter(x -> x.getPickBatchDetailId().equals(pickBatchDetail.getId())).findAny().orElse(null);
                    SoMaster so = soMasterService.findByBillNo(oneItem.getSoBillNo());
                    String historyPickCellCode = inventoryTransactService.findHistoryPickCellBySku(pickBatchDetail.getItemCode(), so.getWareId());
                    bcDetailInfo.setCellCode(Np.i(historyPickCellCode).x(x -> "*" + x + "*").o());
                }
                bcDetailInfo.setItemName(pickBatchDetail.getItemName());
                double qty = pickBatchDetail.getItemQty() - pickBatchDetail.getReviewQty();
                bcDetailInfo.setQty((int) qty);
                bcDetailList.add(bcDetailInfo);
            }

            // 第三部分：标签
            for (PickBatchItem pickBatchItem : thisPickBatchItemList) {
                String billNo = pickBatchItem.getSoBillNo();
                String sku = pickBatchItem.getItemCode();
                SoMaster soMaster = soMasterService.findByBillNo(billNo);
                // 根据so 查询详情
                SoDetailCriteria soDetailCriteria = new SoDetailCriteria();
                soDetailCriteria.setBillNo(pickBatchItem.getSoBillNo());
                List<SoDetailDto> soDetailDtos = soDetailService.findList(soDetailCriteria);
                Integer orderAllQuantity = 0;
                Integer orderSkuQuantity = 0;
                for (SoDetailDto soDetailDto : soDetailDtos) {
                    orderAllQuantity = orderAllQuantity + soDetailDto.getOrderQuantity().intValue();
                    if (soDetailDto.getItemCode().equals(sku)) {
                        orderSkuQuantity = orderSkuQuantity + soDetailDto.getOrderQuantity().intValue();
                    }
                }

                BcItemInfo bcItemInfo = new BcItemInfo();
                bcItemInfo.setOrderIndex(pickBatchItem.getDeliverIndex());
                bcItemInfo.setAllotBillNo(soMaster.getAllotBillNo());
                bcItemInfo.setSkuQuantity(orderSkuQuantity);
                bcItemInfo.setOrderAllQuantity(orderAllQuantity);
                bcItemInfo.setItemName(pickBatchItem.getItemName());
                Boolean hasWeight = Np.i(itemInfoService.findByItemCode(pickBatchItem.getItemCode())).x(x -> x.getWeighting()).eq(true);
                bcItemInfo.setHasWeight(hasWeight);
                bcItemInfo.setItemCode(pickBatchItem.getItemCode());
                bcItemInfo.setPosition(pickBatchItem.getPosition());
                bcItemInfo.setCellCode(pickBatchItem.getCellCode());
                {
                    // 此SKU在此单中出现的次数序号
                    Map<String, Integer> soSku2Index = Np.i(qrCodeSo2Sku2Index.get(billNo)).o(new HashMap<>());
                    Integer soSkuIndex = Np.i(soSku2Index.get(pickBatchItem.getItemCode())).o(0);
                    soSkuIndex++;
                    soSku2Index.put(pickBatchItem.getItemCode(), soSkuIndex);
                    qrCodeSo2Sku2Index.put(billNo, soSku2Index);
                    bcItemInfo.setStrIndex(soSkuIndex);
                    bcItemInfo.setDetailIndex(soSkuIndex);
                }
                QrEntity rqEntity = new QrEntity();
                rqEntity.setSo(billNo);
                rqEntity.setSku(sku);
                rqEntity.setSkui(bcItemInfo.getStrIndex() + "");
                rqEntity.setSkuqty(orderSkuQuantity.toString());
                bcItemInfo.setQrCode(rqEntity.entityToString(QrStringType.SIMPLE_STRING));
                bcItemInfo.setStatus(pickBatchItem.getStatus());
                bcItemInfo.setLoadIndex(Np.i(loadIndexMap.get(pickBatchItem.getQrCode())).o(0));
                bcItemList.add(bcItemInfo);
            }
            reList.add(result);
        }
        return ResultGenerator.genSuccessResult(SimpleMap.init("batchList", reList));
    }


    /**
     * 批量打印拣货单
     */
    @PostMapping("/getPickTaskAllForDriver")
    @Transactional
    @Authority("so_masters_getPickTaskAllForDriver")
    public Result getPickTaskAllForDriver(@RequestBody xhcSoPrintDataAllRequest xhcPrintRequest, @User CurrentUser currentUser) throws IOException {
        // Integer wareId = xhcPrintRequest.getWareId();
        List<Map> printInfos = xhcPrintRequest.getPrintInfos();

        List<Map> dataList = new ArrayList<>();
        // Integer[] soMasterIds = soPrintDataAllRequest.getSoMasterIds();

        // 获取电子面单模板
        PrintTempInfo printTempInfo = printTempInfoService.findByWare(xhcPrintRequest.getWareId(), "分司机拣货单");
        if (printTempInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到匹配的出库单模板");
        }

        for (Map soMasterMap : printInfos) {
            Integer soMasterId = Convert.toInt(soMasterMap.get("soMasterId"));
            String driverName = Convert.toStr(soMasterMap.get("driverName"));
            String serialNumber = Convert.toStr(soMasterMap.get("index"));

            SoMaster soMaster = soMasterService.findById(soMasterId);
            // soMaster.setPrintState(soMaster.getPrintState()+1);
            // soMasterService.updateState(soMaster);
            String strsoPickTask = getPrintDataStrForDriverPickOrder(soMaster, printTempInfo, currentUser, driverName, serialNumber, 0);
            Map<String, String> data = new HashMap<>();
            data.put("soPickTask", strsoPickTask);
            data.put("index", soMaster.getShopDailyCount() + "");
            dataList.add(data);
        }
        return ResultGenerator.genSuccessResult(dataList);
    }

    /**
     * 批量打印SO单 不加司机信息
     */
    @PostMapping("/getSoPrintDataAll")
    @Transactional
    @Authority("so_masters_getSoPrintDataAll")
    public Result getSoPrintDataAll(@RequestBody SoPrintDataAllRequest soPrintDataAllRequest, @User CurrentUser currentUser) throws IOException {
        Long time1 = System.currentTimeMillis();
        // 获取电子面单模板
        PrintTempInfo printTempInfo = printTempInfoService.findByWare(soPrintDataAllRequest.getWareId(), "出库单");
        if (printTempInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到匹配的出库单模板");
        }

        List<Map> dataList = new ArrayList<>();
        Integer[] soMasterIds = soPrintDataAllRequest.getSoMasterIds();
        // sort(soMasterIds);
        for (Integer soMasterId : soMasterIds) {
            SoMaster soMaster = soMasterService.findById(soMasterId);
            // soMaster.setPrintState(soMaster.getPrintState()+1);
            // soMasterService.updateState(soMaster);
            String str = getPrintDataStr(soMaster, printTempInfo, currentUser);
            Map<String, String> data = new HashMap<>();
            data.put("html", str);
            data.put("pdfUrl", soMasterService.getPdfUrl(soMaster));
            // data.put("soPickTask",soMasterService.soPickTask(soMaster,null,null));
            data.put("index", soMaster.getShopDailyCount() + "");
            dataList.add(data);
        }
        // DoLog
        Long time2 = System.currentTimeMillis();
        DataOperationLog<Object> doLog = new DataOperationLogTool.DataOperationLog<>();
        // DoLog必填项
        doLog.setModule(DataOperationLogModule.PRINT);
        doLog.setOperation(DataOperationLogOperation.PRINT_SO1);
        doLog.setOperationUserId(currentUser.getUserId().longValue());
        doLog.setOperationUserName(currentUser.getUserName());
        // DoLog选填项
        doLog.setBeforeData(soPrintDataAllRequest);
        doLog.setAfterData(dataList);
        doLog.setTimer(time2 - time1);
        doLog.setWareId(soPrintDataAllRequest.getWareId().longValue());// 尽可能填写
        doLogger.save(doLog);
        return ResultGenerator.genSuccessResult(dataList);
    }

    /**
     * 批量打印当前所有出库单
     */
    @PostMapping("/getPickTaskAll")
    @Transactional
    @Authority("so_masters_getPickTaskAll")
    public Result getPickTaskAll(@RequestBody SoPrintDataAllRequest soPrintDataAllRequest, @User CurrentUser currentUser) throws IOException {
        Long time1 = System.currentTimeMillis();
        // 获取电子面单模板
        PrintTempInfo printTempInfo = printTempInfoService.findByWare(soPrintDataAllRequest.getWareId(), "出库单");
        if (printTempInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到匹配的出库单模板");
        }
        List<Map<String, String>> dataList = new ArrayList<>();
        Integer[] soMasterIds = soPrintDataAllRequest.getSoMasterIds();
        // sort(soMasterIds);
        for (Integer soMasterId : soMasterIds) {
            SoMaster soMaster;
            soMaster = soMasterService.findById(soMasterId);
            Map<String, String> data = new HashMap<>();
            data.put("soPickTask", soMasterService.soPickTask(soMaster, null, null));
            data.put("index", soMaster.getShopDailyCount() + "");
            dataList.add(data);
        }
        // DoLog
        Long time2 = System.currentTimeMillis();
        DataOperationLog<Object> doLog = new DataOperationLogTool.DataOperationLog<>();
        // DoLog必填项
        doLog.setModule(DataOperationLogModule.PRINT);
        doLog.setOperation(DataOperationLogOperation.PRINT_SO3);
        doLog.setOperationUserId(currentUser.getUserId().longValue());
        doLog.setOperationUserName(currentUser.getUserName());
        // DoLog选填项
        doLog.setBeforeData(soPrintDataAllRequest);
        doLog.setAfterData(dataList);
        doLog.setTimer(time2 - time1);
        doLog.setWareId(soPrintDataAllRequest.getWareId().longValue());
        doLogger.save(doLog);
        return ResultGenerator.genSuccessResult(dataList);
    }

    /**
     * 打印当前波次的所有出库单
     */
    @GetMapping("/getPickTaskLabel")
    @Transactional
    @Authority("so_masters_getPickTaskLabel")
    public Result getPickTaskLabel(Integer soMasterId, Integer wareId, @User CurrentUser currentUser) throws IOException {
        Long time1 = System.currentTimeMillis();
        // 未找到匹配的拣货标签模板
        PrintTempInfo printTempInfo = printTempInfoService.findByWare(wareId, "拣货标签");
        if (printTempInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到匹配的拣货标签模板");
        }
        List<Map> dataList = new ArrayList<>();
        SoMaster soMaster = soMasterService.findById(soMasterId);
        List<SoDetail> details = soDetailService.findPickUPOrderByMasterId(soMasterId);
        Double orderAllQuantity = 0.0;
        Map<String, Double> skuToQuantity = new HashMap<>();// 此SKU在此Order中统计的总数
        for (SoDetail soDetail : details) {
            orderAllQuantity = orderAllQuantity + soDetail.getPickQuantity();
            Double skuQuantity = Np.i(skuToQuantity.get(soDetail.getItemCode())).o(0.0);
            skuToQuantity.put(soDetail.getItemCode(), skuQuantity + soDetail.getPickQuantity());
        }
        for (SoDetail soDetail : details) {
            Double quantity = soDetail.getPickQuantity();
            for (int i = 0; i < quantity.intValue(); i++) {
                String str = getPickPrintString(soMaster, soDetail, orderAllQuantity, skuToQuantity.get(soDetail.getItemCode()), String.valueOf(i + 1), printTempInfo, currentUser, "无", "无");
                Map<String, String> data = new HashMap<>();
                data.put("html", str);
                data.put("index", soMaster.getShopDailyCount() + "-" + soDetail.getDetailIndex() + "-" + (i + 1));
                dataList.add(data);
            }
        }
        // DoLog
        Long time2 = System.currentTimeMillis();
        DataOperationLog<Object> doLog = new DataOperationLogTool.DataOperationLog<>();
        // DoLog必填项
        doLog.setModule(DataOperationLogModule.PRINT);
        doLog.setOperation(DataOperationLogOperation.PRINT_SO2);
        doLog.setOperationUserId(currentUser.getUserId().longValue());
        doLog.setOperationUserName(currentUser.getUserName());
        // DoLog选填项
        SimpleMap beforeData = SimpleMap.init("master", soMaster).append("details", details);
        doLog.setBeforeData(beforeData);
        doLog.setAfterData(dataList);
        doLog.setOperationCode(soMaster.getBillNo());
        doLog.setOperationId(soMasterId.longValue());
        doLog.setTimer(time2 - time1);
        doLog.setWareId(wareId.longValue());
        doLogger.save(doLog);
        return ResultGenerator.genSuccessResult(dataList);
    }

    /**
     * 打印拣货单
     */
    @GetMapping("/getSOPickTask")
    @Transactional
    @Authority("so_masters_getSOPickTask")
    public Result getSOPickTask(String billNo, @User CurrentUser currentUser) throws IOException {
        ExportManager exportManager = (ExportManager) Utils.getApplicationContext().getBean(ExportManager.BEAN_ID);
        String fileName = "mysql:soPickTask.ureport.xml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("billNo", billNo);
        HtmlReport htmlReport = exportManager.exportHtml(fileName, "/wms", parameters);
        return ResultGenerator.genSuccessResult(htmlReport.getContent() + "<style>" + htmlReport.getStyle() + "</style>");
    }

    /**
     * 打印当前波次的所有拣货标签
     */
    @GetMapping("/getPickPrintData")
    @Transactional
    @Authority("so_masters_getPickPrintData")
    public Result getPickPrintData(Integer waveId, Integer wareId, @User CurrentUser currentUser) {
        // 获取电子面单模板
        PrintTempInfo printTempInfo = printTempInfoService.findByWare(wareId, "拣货标签");
        if (printTempInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到匹配的拣货标签模板");
        }
        List<Map> dataList = new ArrayList<>();
        List<SoMasterDto> list = soMasterService.findByWaveId(waveId, wareId);
        for (SoMasterDto soMaster : list) {
            List<SoDetail> details = soDetailService.findPickUPOrderByMasterId(soMaster.getSoMasterId());

            Double orderAllQuantity = 0.0;// 订单总件数
            Map<String, Double> skuToQuantity = new HashMap<>();// 此SKU在此Order中统计的总数
            for (SoDetail soDetail : details) {
                orderAllQuantity = orderAllQuantity + soDetail.getPickQuantity();
                Double skuQuantity = Np.i(skuToQuantity.get(soDetail.getItemCode())).o(0.0);
                skuToQuantity.put(soDetail.getItemCode(), skuQuantity + soDetail.getPickQuantity());
            }

            for (SoDetail soDetail : details) {
                Double quantity = soDetail.getPickQuantity();
                for (int i = 0; i < quantity.intValue(); i++) {
                    String str = getPickPrintString(soMaster, soDetail, orderAllQuantity, skuToQuantity.get(soDetail.getItemCode()), String.valueOf(i + 1), printTempInfo, currentUser, "无", "无");
                    Map<String, String> data = new HashMap<>();
                    data.put("html", str);
                    data.put("index", soMaster.getShopDailyCount() + "-" + soDetail.getDetailIndex() + "-" + (i + 1));
                    dataList.add(data);
                }
            }
        }
        return ResultGenerator.genSuccessResult(dataList);
    }

    public String getPickPrintString(SoMaster soMaster,
            SoDetail soDetail,
            Double orderAllQuantity,
            Double skuQuantity,
            String strIndex,
            PrintTempInfo printTempInfo,
            CurrentUser currentUser, String driverName, String serialNumber

    ) {

        String weight = "";
        if (Np.i(itemInfoService.findByItemCode(soDetail.getItemCode())).x(x -> x.getWeighting()).eq(true)) {
            weight = "weight";
        }
        // 获取拣货标签模板
        if (printTempInfo == null) {
            printTempInfo = printTempInfoService.findByWare(soMaster.getWareId(), "拣货标签");
            if (printTempInfo == null) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到匹配的拣货标签模板");
            }
        }
        String itemName = soDetail.getItemName();
        String[] itemNames = soDetail.getItemName().split("\\|");
        if(itemNames.length>1){
            itemName = itemNames[1];
        }

        String html = printTempInfo.getTempHtml();
        Document doc = Jsoup.parse(html);
        // 删除不显示的
        doc.select(".dis-none").remove();
        // 直线中间蓝点的问题
        doc.select(".line-handle").remove();
        PrintTempUtils.setTempData2("weight", weight, doc, "text");
        PrintTempUtils.setTempData2("ORDER_driverName", driverName, doc, "text");
        PrintTempUtils.setTempData2("ORDER_serialNumber", serialNumber, doc, "text");
        PrintTempUtils.setTempData2("ORDER_NO", soMaster.getAllotBillNo(), doc, "text");
        PrintTempUtils.setTempData2("ITEM_NAME", itemName, doc, "text");
        PrintTempUtils.setTempData2("SKU_XH", soDetail.getDetailIndex() + "：" + strIndex + "-" + soDetail.getPickQuantity().intValue() + "-" + orderAllQuantity.intValue(), doc, "text");
        PrintTempUtils.setTempData2("ITEM_CODE", soDetail.getItemCode(), doc, "text");
        PrintTempUtils.setTempData2("ITEM_CODE_BAR_CODE", soDetail.getItemCode(), doc, "barCode");
        // 2023-01-10: Pick-SKU标签中二维码包含单号和SKU编号 @xuesinuo
        QrEntity rqEntity = new QrEntity();
        rqEntity.setSo(soMaster.getBillNo());
        rqEntity.setSku(soDetail.getItemCode());
        rqEntity.setSkui(strIndex);
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        rqEntity.setSkuqty(decimalFormat.format(skuQuantity));
        PrintTempUtils.setTempData2("ITEM_CODE_QR_CODE", rqEntity.entityToString(QrStringType.SIMPLE_STRING), doc, "qrCode");

        // 打印相关
        PrintTempUtils.setTempData("PRINT_TIME", DateUtils.getNowDateTimeString().substring(0, 17), doc, "text");
        PrintTempUtils.setTempData("PRINT_DATE", DateUtils.getNowDateString().substring(0, 9), doc, "text");
        PrintTempUtils.setTempData("PRINT_USER_NAME", currentUser.getUserName(), doc, "text");

        // doc在创建时里面包含了html head body等节点 不需要
        Element labelContent = doc.getElementsByAttributeValue("class", "label-content").get(0);
        String tempHtml = labelContent.toString();
        return tempHtml;

    }

    @Autowired
    private CellInfoService cellInfoService;

    /**
     * 强制处理订单
     */
    @GetMapping("/forcedProcessing")
    @Transactional
    @Authority("so_masters_forcedProcessing")
    public Result forcedProcessing(String billNo, Integer type) {
        Double allotQuantity = 0.0, quantity = 0.0;
        SoMaster soMaster = soMasterService.findBy("billNo", billNo);
        // 4为已分配
        if (soMaster.getState() < 4) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "订单未分配库存，不能强制处理");
        }
        List<SoDetail> soDetails = soDetailService.findByBillNo(billNo);
        List<PickTask> pickTasks = pickTaskService.findBySoMasterId(soMaster.getSoMasterId());
        String cellCode = cellInfoService.getSysJhCell(soMaster.getWareId());

        // 拣货复核
        if (type == 1) {
            soMaster.setState(7);
            soMasterService.updateState(soMaster);
            for (SoDetail soDetail : soDetails) {
                soDetail.setState(5);
                soDetail.setPickQuantity(soDetail.getOrderQuantity());
                soDetail.setPickPackDetailId(soDetail.getPackDetailId());
                soDetail.setPickPackDescribe(soDetail.getPackDescribe());
                soDetail.setCheckQuantity(soDetail.getOrderQuantity());
                soDetailService.update(soDetail);
            }

            for (PickTask pickTask : pickTasks) {
                if (pickTask.getState() < 2 && pickTask.getState() != -1) {
                    Inventory inventory = inventoryService.findById(pickTask.getInventoryId());
                    // double quantity = MyUtils.calculateMultiplication(inventory.getPackDetailId(), pickTaskDto.getPackDetailId(), pickTaskDto.getPickQuantity(), pickTaskDto.getTransRatio());
                    allotQuantity = inventory.getAllotQuantity() - pickTask.getPickQuantity();
                    quantity = inventory.getQuantity() - pickTask.getPickQuantity();
                    // 2022-4 berwin 负数临时处理为0.0 并输出error.log
                    if (allotQuantity >= 0)
                        inventory.setAllotQuantity(allotQuantity);
                    else
                        inventory.setAllotQuantity(0.0);

                    if (quantity >= 0)
                        inventory.setQuantity(quantity);
                    else
                        inventory.setQuantity(0.0);

                    inventoryService.update(inventory);
                    Inventory inventory1 = new Inventory();
                    inventory1.setOrganizationId(inventory.getOrganizationId());
                    inventory1.setOrganizationName(inventory.getOrganizationName());
                    inventory1.setWareId(inventory.getWareId());
                    inventory1.setWareName(inventory.getWareName());
                    inventory1.setCellCode(cellCode);
                    inventory1.setPackDetailId(inventory.getPackDetailId());
                    inventory1.setPackDescribe(inventory.getPackDescribe());
                    inventory1.setTransRatio(inventory.getTransRatio());
                    inventory1.setItemCode(inventory.getItemCode());
                    inventory1.setItemName(inventory.getItemName());
                    inventory1.setBatchId(inventory.getBatchId());
                    inventory1.setBatchName(inventory.getBatchName());
                    inventory1.setAllotQuantity(0.0);
                    inventory1.setQuantity(pickTask.getPickQuantity());
                    inventoryService.save(inventory1);

                    pickTask.setPickTime(DateUtils.getNowDateTimeString());

                    pickTask.setToCellCode(cellCode);

                    pickTask.setInventoryId(inventory1.getInventoryId());
                    pickTask.setReviewQuantity(pickTask.getPickQuantity());
                    pickTask.setState(2);
                    pickTask.setCheckTime(DateUtils.getNowDateTimeString());
                    pickTask.setReviewQuantity(pickTask.getPickQuantity());
                    pickTaskService.update(pickTask);
                }

            }
        }
        // 发运
        else if (type == 2) {
            soMaster.setSendState(SendState.ALL);
            soMasterService.updateState(soMaster);
            for (SoDetail soDetail : soDetails) {
                soDetail.setState(6);
                soDetail.setPickQuantity(soDetail.getOrderQuantity());
                soDetail.setPickPackDetailId(soDetail.getPackDetailId());
                soDetail.setPickPackDescribe(soDetail.getPackDescribe());
                soDetail.setCheckQuantity(soDetail.getOrderQuantity());
                soDetail.setDeliveryQuantity(soDetail.getOrderQuantity());
                soDetailService.update(soDetail);
            }

            for (PickTask pickTask : pickTasks) {
                if (pickTask.getIsParent() == 0) {
                    Inventory inventory = inventoryService.findById(pickTask.getInventoryId());
                    if (inventory != null) {
                        if (pickTask.getState() == 0) {
                            allotQuantity = inventory.getAllotQuantity() - pickTask.getPickQuantity();
                            // 2022-4 berwin 负数临时处理为0.0 并输出error.log
                            if (allotQuantity >= 0)
                                inventory.setAllotQuantity(allotQuantity);
                            else
                                inventory.setAllotQuantity(0.0);
                        }
                        quantity = inventory.getQuantity() - pickTask.getPickQuantity();
                        // 2022-4 berwin 负数临时处理为0.0 并输出error.log
                        if (quantity >= 0)
                            inventory.setQuantity(quantity);
                        else
                            inventory.setQuantity(0.0);

                        inventoryService.update(inventory);
                        pickTask.setState(5);
                        pickTaskService.update(pickTask);
                    }
                }
            }
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 打印机相关日志
     */
    @PostMapping("/printerLog")
    @Authority("so_masters_printerLog")
    public Result printerLog(@RequestBody PrinterLog printerLog) {
        log.info("printer log: {}", JSON.toJSONString(printerLog));
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 当前缺货清单
     * @return
     */
    @PostMapping("/findByShortageStatistics")
    @Authority("so_masters_findByShortageStatistics")
    // FIXME 由于负库存的引入逻辑已经不准
    public Result findByShortageStatistics(@RequestBody SoMasterCriteria queryCriteria) throws IOException, WriteException, BiffException {
        //查询T-1天缺货状态的soDetails
        SoDetailCriteria criteria = new SoDetailCriteria();
        //应该使用详情的部分分配状态-代表sku 的缺货
        criteria.setState(1);
        criteria.setWareId(queryCriteria.getWareId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        String endTime = sdf.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH,-3);
        String beforeThreeDay = sdf.format(calendar.getTime());
        String startTime = beforeThreeDay.substring(0,10) + " 00:00:00";
        criteria.setStartCreateTime(startTime);
        criteria.setEndCreateTime(endTime);
        List<SoDetailDto> SoDetailDtos = soDetailService.findListAndMaster(criteria);
        //相同SKU 进行合并
        List<SoDetailDto> SoDetailDtos2 = SoDetailDtos.stream().collect(Collectors.toMap(SoDetailDto::getItemCode,s->s,(s1,s2)->{
            s1.setOrderQuantity(s1.getOrderQuantity() + s2.getOrderQuantity());
            return s1;
        })).values().stream().collect(Collectors.toList());
        if(SoDetailDtos2==null || SoDetailDtos2.size()<1){
            return ResultGenerator.genSuccessResult();
        }
        List<SoMasterShortageStatistics> shortageStatistics = new ArrayList<>();
        List<Inventory> type1List = new ArrayList<>();
        for(SoDetailDto detail:SoDetailDtos2){
            SoMasterShortageStatistics statistics = new SoMasterShortageStatistics();
            criteria.setItemCode(detail.getItemCode());
            List<SoDetailDto> detailDtos = soDetailService.findListAndMaster(criteria);
            statistics.setItemCode(detail.getItemCode());
            statistics.setItemName(detail.getItemName());
            statistics.setOrderShortageQuantity(detailDtos.size());
            //查询可分配库存数
            InventoryCriteria inventoryCriteria = new InventoryCriteria();
            inventoryCriteria.setItemCode(detail.getItemCode());
            inventoryCriteria.setCellUseType(1); //拣货位
            inventoryCriteria.setWareId(queryCriteria.getWareId());
            List<InventorySelectDto> inventoryDtos = inventoryService.findCanBeOutOfStock(inventoryCriteria);// FIXME 此数可能不太对
            InventoryCriteria ic = new InventoryCriteria();
            ic.setItemCode(detail.getItemCode());
            ic.setWareId(queryCriteria.getWareId());
            List<InventoryDto> invList = inventoryService.findList(ic);
            statistics.setStorageCell("");
            if(invList!=null){
                type1List = invList.stream().filter(inv -> Np.i(inv.getCellUseType()).eq(1)).collect(Collectors.toList());
                if(type1List.size()>0){
                    type1List = type1List.stream().collect(Collectors.collectingAndThen((Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Inventory::getCellCode)))),ArrayList::new));// 拣货位库存.去重
                }
                List<Inventory> type2List = invList.stream().filter(inv -> Np.i(inv.getCellUseType()).eq(2)).collect(Collectors.toList());// 仓储位库存
                statistics.setPickCells(type1List);
                if(type2List.size()>0){
                    statistics.setStorageCell(type2List.get(0).getCellCode());
                }
            }
            // 如果没有可拣库存
            Double totalCanBeQuantity = 0.0;
            for (InventorySelectDto inventorySelectDto : inventoryDtos) {
                totalCanBeQuantity += inventorySelectDto.getQuantity() - inventorySelectDto.getAllotQuantity() - inventorySelectDto.getFreezeQuantity();
            }
            statistics.setItemShortageQuantity((int) (detail.getOrderQuantity()-totalCanBeQuantity));
            shortageStatistics.add(statistics);
        }
        //进行输出excel
        String staticPath = fileSetting.getStaticFileDir();
        String downloadPath = fileSetting.getDownloadPath() + "/Restocking" + DateUtils.dateToStr(new Date(), "yyyyMMddHHmmss") + ".xls";
        String modelPath = fileSetting.getModelPath() + "/Restocking.xls";
        String modelFilePath = staticPath + modelPath;
        String filePath = staticPath + downloadPath;
        jxl.Workbook workbook = jxl.Workbook.getWorkbook(new java.io.File(modelFilePath));
        WritableWorkbook copy = jxl.Workbook.createWorkbook(new java.io.File(filePath), workbook);
        WritableSheet sheet = copy.getSheet(0);
        int i = 0;
        for (SoMasterShortageStatistics statistics : shortageStatistics) {
            i++;
            sheet.addCell(new Label(0, i, i+""));
            sheet.addCell(new Label(1, i, Np.i(statistics).x(x -> x.getItemCode())+""));
            sheet.addCell(new Label(2, i, Np.i(statistics).x(x -> x.getItemName())+""));
            sheet.addCell(new Label(3, i, Np.i(statistics).x(x -> x.getItemShortageQuantity())+""));
            sheet.addCell(new Label(4, i, Np.i(statistics).x(x -> x.getOrderShortageQuantity())+""));
            sheet.addCell(new Label(5, i, Np.i(statistics).x(x -> x.getStorageCell())+""));
            if(statistics.getPickCells()!=null){
                for(int j=0;j<statistics.getPickCells().size();j++) {
                    sheet.addCell(new Label(j+6, i,Np.i(statistics.getPickCells().get(j)).x(x ->x.getCellCode())+""));
                }
            }

        }
        copy.write();
        copy.close();
        workbook.close();
        String fullPath = fileS3Tool.save(FileUseType.EXPORT_RESTOCKING, "Restocking.xls",
                new java.io.File(staticPath + downloadPath));
        return ResultGenerator.genSuccessResult(fileS3Tool.getUrl(fullPath));
    }

    /**
     * 发运-单个SKU 发运操作
     * @param criteria
     * @param currentUser
     * @return
     *
     * 发运数量超过10个之后，数量可以大于1
     *
     */
    @PostMapping("/skuSendOut")
    @Authority("so_masters_skuSendOut")
    public Result skuSendOut(@RequestBody SendOutCriteria criteria , @User CurrentUser currentUser) {
        soMasterService.skuSendOut(criteria,currentUser);
        return ResultGenerator.genSuccessResult();

    }

    /**
     * 根据订单与sku 查询已发运数量
     *
     * @param soBillNo
     * @param itemCode
     * @return
     */
    @GetMapping("/sendQuantity")
    @Authority("so_masters_sendQuantity")
    public Result findByBillNoAndSku(String soBillNo, String itemCode) {
        SendDetail sendDetail = sendDetailService.findByBillNoAndSku(soBillNo, itemCode);
        return ResultGenerator.genSuccessResult(sendDetail);
    }

    @Data
    public static class NewPickupBatchParam {
        // 订单号
        private String billNos;
        // 司机姓名
        private String driverName;
    }

    // FIXME 临时方案，配置的如果是dev，则去访问豆芽的测试接口
    @Value("${spring.profiles.active}")
    private String context;

    /**
     * 从豆芽接口的到波次原始数据
     */
    private PickBatchResponse getBatchSourceDataFromElena(NewPickupBatchParam param, Integer userId) {
        String url = "";
        // FIXME 临时方案，根据环境调用不同接口，长期需要，改到配置文件
        if (context.indexOf("dev") >= 0 || context.indexOf("test") >= 0) {
            url = "https://bi-test.scr.foodsup.com/api/wms/pickup_batch?so_ids=" + param.getBillNos() + "&user_id=" + userId;
        } else {
            url = "https://bi.scr.foodsup.com/api/wms/pickup_batch?so_ids=" + param.getBillNos() + "&user_id=" + userId;
        }
        HttpGet httpGet = new HttpGet(url);
        String token = "tOFVS1Tpp58l3gQQXZ1DpTtI0CTEQXOH62F11GnjbSw";
        httpGet.setHeader("Authorization", "Bearer " + token);
        String res = HttpClientUtil.doGet(url, httpGet);
        JSONObject res1 = JSON.parseObject(res);
        if(!("1").equals(res1.get("status").toString())){
            log.info(url + " " + res);
            throw new ServiceException(CommonCode.SERVICE_ERROR, "暂无此订单的波次信息！");
        }
        return JSON.parseObject(res1.get("data").toString(), PickBatchResponse.class);
    }

    /**
     * 本地随便搞个测试波次
     */
    private PickBatchResponse getBatchSourceDataFromLocalTest(NewPickupBatchParam param) {
        LocalDateTime now = LocalDateTime.now();
        String[] soBillNoArray = param.getBillNos().split(",");
        boolean flag = true;
        PickBatchResponse result = new PickBatchResponse();
        result.setPickBatchGroupNo(now.getDayOfYear() + "-" + now.getHour());
        result.setTotalQty(0D);
        Set<String> pqOrderSet = new HashSet<>();
        result.setTotalOrderCount(soBillNoArray.length);
        result.setPickBatchList(new ArrayList<>());
        int i = 0;
        for (String soBillNo : soBillNoArray) {
            result.setTotalQty(result.getTotalQty() + 1);
            SoMaster so = soMasterService.findByBillNo(soBillNo);
            if (so == null) {
                throw new ServiceException(CommonCode.PARAMETER_ERROR, "BillNos 参数错误");
            }
            pqOrderSet.add(so.getAllotBillNo());
            PickBatchListResponse batch = new PickBatchListResponse();
            result.getPickBatchList().add(batch);
            batch.setPickBatchId(result.getPickBatchGroupNo() + "-" + (++i));
            flag = !flag;
            batch.setContainer_type(flag ? "板车" : "笼车");
            batch.setWholesale(flag);
            batch.setItemList(new ArrayList<>());
            List<PickTaskDto> pickTaskList = pickTaskService.findBySo(soBillNo);
            List<SoDetail> soDetailList = soDetailService.findByBillNo(soBillNo);
            for (SoDetail soDetail : soDetailList) {
                for (double n = soDetail.getOrderQuantity(); n > 0; n--) {
                    PickTaskDto pickTask = pickTaskList.stream()
                            .filter(x -> x.getItemCode().equals(soDetail.getItemCode()) && x.getPickQuantity() > 0)
                            .findAny().orElse(null);
                    if (pickTask != null) {
                        pickTask.setPickQuantity(pickTask.getPickQuantity() - 1D);
                    }
                    SoSkuInfo item = new SoSkuInfo(
                            soBillNo,
                            soDetail.getItemCode(),
                            soDetail.getItemName(),
                            "L",
                            Np.i(pickTask).x(x -> x.getCellCode()).o(),
                            (int) soDetail.getOrderQuantity().doubleValue(),
                            (int) soDetail.getOrderQuantity().doubleValue(),
                            i);
                    batch.getItemList().add(item);
                }
            }
        }
        result.setPqOrderLst(new ArrayList<>(pqOrderSet));
        return result;
    }

    /**
     * 生成波次
     */
    @PostMapping("/pickupBatch")
    @Authority("so_masters_pickupBatch")
    public Result pickupBatch(@RequestBody NewPickupBatchParam param, @User CurrentUser user) {
        if (param.getBillNos() == null || param.getBillNos().equals("")) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "请选择要打印的订单！");
        }
        PickBatchResponse response;
        if (context.indexOf("dev") >= 0) {
            response = this.getBatchSourceDataFromLocalTest(param);
        } else {
            response = this.getBatchSourceDataFromElena(param, user.getUserId());
        }
        // 数据存储
        pickBatchGroupService.saveGroupBatchAndDetail(response, param.getDriverName(), user);
        return ResultGenerator.genSuccessResult(SimpleMap.init()
                .append("pickBatchGroupNo", response.getPickBatchGroupNo()));
    }

    /**
     * 获取当前波次组是否打印过
     */
    @PostMapping("/getPickupBatchGroup")
    @Authority("so_masters_getPickupBatchGroup")
    public Result getPickupBatchGroup(@RequestBody NewPickupBatchParam param, @User CurrentUser user) {
        if(param.getBillNos()==null || param.getBillNos().equals("")){
            throw new ServiceException(CommonCode.SERVICE_ERROR, "请选择要打印的订单！");
        }
        PickBatchResponse response;
        if (context.indexOf("dev") >= 0) {
            response = this.getBatchSourceDataFromLocalTest(param);
        } else {
            response = this.getBatchSourceDataFromElena(param, user.getUserId());
        }
        PickBatchGroup existedPickBatchGroup = pickBatchGroupService.getByNo(response.getPickBatchGroupNo());
        return ResultGenerator.genSuccessResult(SimpleMap.init()
                .append("has", existedPickBatchGroup != null)
                .append("pickBatchGroupNo", response.getPickBatchGroupNo()));
    }

    @PostMapping("/updateMasterAndDetailState")
    public Result updateMasterAndDetailState(@RequestParam String billNo,@RequestParam String sku,@User CurrentUser user){
        soMasterService.updateMasterAndDetailState(billNo,sku,user);
        return ResultGenerator.genSuccessResult();
    }

}

@Service
class SoMasterControllerExtension {
    @Autowired
    private SoMasterService soMasterService;

    @Synchronized(lockName = LockName.SO_SET_PRIORITY, value = "#soMasterId")
    public void setPriorityLocked(Integer soMasterId) {
        Integer priority = soMasterService.getMaxPriority();
        SoMaster soMaster1 = soMasterService.findById(soMasterId);
        soMaster1.setPriority(priority + 1);
        soMasterService.update(soMaster1);
    }
}




