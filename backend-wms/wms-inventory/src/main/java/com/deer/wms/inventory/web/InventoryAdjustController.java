package com.deer.wms.inventory.web;

import static com.deer.wms.project.root.util.FunctionUtil.nvl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.BatchAdjustDetailCriteria;
import com.deer.wms.inventory.model.InventoryAdjust;
import com.deer.wms.inventory.model.InventoryAdjustCriteria;
import com.deer.wms.inventory.model.InventoryAdjustDetail;
import com.deer.wms.inventory.model.InventoryAdjustDto;
import com.deer.wms.inventory.model.Reason;
import com.deer.wms.inventory.model.ReasonCriteria;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.stockTake.StockTakeMaster;
import com.deer.wms.inventory.model.stockTake.StockTakeMasterCriteria;
import com.deer.wms.inventory.model.stockTake.StockTakeTask;
import com.deer.wms.inventory.model.stockTake.StockTakeTaskCriteria;
import com.deer.wms.inventory.model.stockTake.StockTakeTaskDto;
import com.deer.wms.inventory.service.InventoryAdjustDetailService;
import com.deer.wms.inventory.service.InventoryAdjustService;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.inventory.service.InventoryTransactService;
import com.deer.wms.inventory.service.ReasonService;
import com.deer.wms.inventory.service.StockTakeMasterService;
import com.deer.wms.inventory.service.StockTakeTaskService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.project.root.util.SimpleMap;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.service.BillRecordService;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.deer.wms.system.manage.service.ValidatorService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Created by ll on 2020/02/13.
 *
 * 库存调整master信息表
 */
@RestController
@RequestMapping("/inventory/adjusts")
public class InventoryAdjustController {

    @Autowired
    private InventoryAdjustService inventoryAdjustService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private ValidatorService validatorService;
    @Autowired
    private StockTakeMasterService stockTakeMasterService;
    @Autowired
    private StockTakeTaskService stockTakeTaskService;
    @Autowired
    private InventoryTransactService inventoryTransactService;

    @Autowired
    private CaseMasterService caseMasterService;

    @Autowired
    private BillRecordService billRecordService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private InventoryAdjustDetailService inventoryAdjustDetailService;

    @Autowired
    private CellInfoService cellInfoService;


    @PostMapping("/insert")
    @Authority("inventory_adjusts_insert")
    public Result add(@RequestBody InventoryAdjust inventoryAdjust) {
        inventoryAdjustService.save(inventoryAdjust);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    @Authority("inventory_adjusts_delete")
    public Result delete(@PathVariable Integer id) {
        inventoryAdjustService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping("/update")
    @Authority("inventory_adjusts_update")
    public Result update(@RequestBody InventoryAdjust inventoryAdjust) {
        inventoryAdjustService.update(inventoryAdjust);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("inventory_adjusts_get")
    public Result detail(@PathVariable Integer id) {
        InventoryAdjust inventoryAdjust = inventoryAdjustService.findById(id);
        return ResultGenerator.genSuccessResult(inventoryAdjust);
    }

    @GetMapping("/list")
    @Authority("inventory_adjusts_list")
    public Result list(InventoryAdjustCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<InventoryAdjustDto> list = inventoryAdjustService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/deleteDetailAndMaster")
    @Authority("inventory_adjusts_deleteDetailAndMaster")
    public Result deleteDetailAndMaster(Integer id) {
        inventoryAdjustService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 创建库存调整单
     */
    @PostMapping("/createInventoryAdjust")
    @Transactional
    @Authority("inventory_adjusts_createInventoryAdjust")
    public Result createInventoryAdjust(@RequestBody InventoryAdjustCriteria criteria, @User CurrentUser currentUser) {
        List<InventoryAdjustDetail> InventoryAdjustDetailResult = new ArrayList<>();
        SimpleMap result = SimpleMap.init().append("adjustDetails", InventoryAdjustDetailResult);
        //垃圾货位不可进行库存调整
        for(InventoryAdjustDetail detail:criteria.getAdjustDetails()){
            Integer cellUseType = cellInfoService.getCellType(detail.getCellCode(),criteria.getWareId());
            if(Np.i(cellUseType).eq(-1)){
                throw new ServiceException(CommonCode.SERVICE_ERROR, "包含垃圾货位，不可进行库存调整");
            }
        }
        List<InventoryAdjustDetail> adjustDetails = criteria.getAdjustDetails();
        List<InventoryAdjustDetail> newAdjustDetails = adjustDetails.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(InventoryAdjustDetail::getInventoryId))), ArrayList::new));
        if(adjustDetails.size()>newAdjustDetails.size()){
            throw new ServiceException(CommonCode.SERVICE_ERROR, "包含相同库存记录，不可重复调整");
        }
        List<Reason> reasons = reasonService.findList(new ReasonCriteria(criteria.getReason()));
        if (reasons.size() <= 0) {
            Reason reason = new Reason();
            reason.setReason(criteria.getReason());
            reasonService.save(reason);
        }
        String tableName = "inventory_adjust";
        String nextId = validatorService.getNextIdString(tableName);
        String billNo = "IA" + "-" + nextId;
        Integer caseState = 2;
        //添加调整单跟踪信息
        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(billNo);
        billRecord.setRecordType(36);//调整新增
        billRecord.setCreateTime(DateUtils.getNowDateTimeString());
        billRecord.setCreateUserId(currentUser.getUserId());
        billRecord.setCreateUserName(currentUser.getUserName());
        billRecordService.save(billRecord);
        caseState = caseMasterService.createCase(billNo, 6, currentUser.getUserId(), criteria.getWareId(), criteria.getOrganizationId());
        InventoryAdjust inventoryAdjust = new InventoryAdjust();
        inventoryAdjust.setBillNo(billNo);
        inventoryAdjust.setOrganizationId(criteria.getOrganizationId());
        inventoryAdjust.setOrganizationName(criteria.getOrganizationName());
        inventoryAdjust.setCreateTime(DateUtils.getNowDateTimeString());
        inventoryAdjust.setReason(criteria.getReason());
        inventoryAdjust.setReasonDescription(criteria.getReasonDescription());
        inventoryAdjust.setWareId(criteria.getWareId());
        inventoryAdjust.setWareCode(criteria.getWareCode());
        inventoryAdjust.setWareName(criteria.getWareName());
        inventoryAdjust.setUserId(currentUser.getUserId());
        inventoryAdjust.setMemo(criteria.getMemo());
        inventoryAdjust.setUserName(currentUser.getUserName());
        inventoryAdjust.setInsertType(criteria.getInsertType());

        //BillRecord billRecord = new BillRecord(billNo,DateUtils.getNowDateTimeString(), currentUser.getUserId(),currentUser.getUserName());
//        BillRecord billRecord1 = new BillRecord();
//        billRecord1.setBillNo(billNo);
//        billRecord1.setRecordType(37);//调整审核
//        billRecord1.setCreateTime(DateUtils.getNowDateTimeString());
//        billRecord1.setCreateUserId(currentUser.getUserId());
//        billRecord1.setCreateUserName(currentUser.getUserName());
//        billRecordService.createBillRecord(billRecord1, currentUser);
        if (caseState == 1) {
//            billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
//            billRecord1.setModifyUserId(currentUser.getUserId());
//            billRecord1.setModifyUserName(currentUser.getUserName());
//            billRecordService.update(billRecord1);
        }
        if (criteria.getType().equals(1)) {
            if (criteria.getInsertType().equals(-2)) {
                //billRecord.setRecordType(BillRecordTypeConstants.INVENTORY_CHECK_CREATE_INVENTORY_ADJUST_DRAFT);
                billRecord.setMemo("通过盘点单创建库存调整单草稿");
            } else {
                //billRecord.setRecordType(BillRecordTypeConstants.INVENTORY_CHECK_CREATE_INVENTORY_ADJUST);
                billRecord.setMemo("通过盘点单创建库存调整单");
                inventoryAdjust.setCaseState(caseState);
            }
            inventoryAdjust.setType(1);
            inventoryAdjust.setStockTakeMasterId(criteria.getStockTakeMasterId());
            StockTakeMaster stockTakeMaster = stockTakeMasterService.findList(new StockTakeMasterCriteria(criteria.getStockTakeMasterId())).get(0);
            stockTakeMaster.setState(6);
            stockTakeMasterService.update(stockTakeMaster);
            List<StockTakeTaskDto> stockTakeTaskDtos = stockTakeTaskService.findList(new StockTakeTaskCriteria(criteria.getStockTakeMasterId()));
            for (int i = 0; i < stockTakeTaskDtos.size(); i++) {
                StockTakeTaskDto stockTakeTaskDto = stockTakeTaskDtos.get(i);
                if (!Objects.equals(stockTakeTaskDto.getState(), 2) || Objects.equals(nvl(stockTakeTaskDto.getDifferQuantity()), 0.0d)
                        || !Objects.equals(stockTakeTaskDto.getType(), 21)) {
                    continue;
                }

                InventoryAdjustDetail inventoryAdjustDetail = new InventoryAdjustDetail();
                inventoryAdjustDetail.setBillNo(billNo);
                inventoryAdjustDetail.setDetailNo(billNo + "-" + (i + 1));
                inventoryAdjustDetail.setInventoryId(stockTakeTaskDto.getInventoryId());
                inventoryAdjustDetail.setItemCode(stockTakeTaskDto.getItemCode());
                inventoryAdjustDetail.setItemName(stockTakeTaskDto.getItemName());
                inventoryAdjustDetail.setBatchId(stockTakeTaskDto.getBatchId());
                inventoryAdjustDetail.setBatchName(stockTakeTaskDto.getBatchName());
                inventoryAdjustDetail.setCellCode(stockTakeTaskDto.getCellCode());
                inventoryAdjustDetail.setBoxCode(stockTakeTaskDto.getBoxCode());
                inventoryAdjustDetail.setPackDetailId(stockTakeTaskDto.getPackDetailId());
                inventoryAdjustDetail.setPackDescribe(stockTakeTaskDto.getPackDescribe());
                inventoryAdjustDetail.setLpn(stockTakeTaskDto.getLpn());
                inventoryAdjustDetail.setDifferQuantity(stockTakeTaskDto.getDifferQuantity());
                inventoryAdjustDetail.setAdjustQuantity(stockTakeTaskDto.getTaskQuantity());
                inventoryAdjustDetail.setState(1);
                inventoryAdjustDetail.setCreateTime(DateUtils.getNowDateTimeString());
                inventoryAdjustDetail.setCreateUseId(currentUser.getUserId());
                inventoryAdjustDetail.setCreateUseName(currentUser.getUserName());
                inventoryAdjustDetail.setMemo(criteria.getMemo());

                inventoryAdjustDetailService.save(inventoryAdjustDetail);
                InventoryAdjustDetailResult.add(inventoryAdjustDetail);
            }
        } else {
            inventoryAdjust.setType(2);
            if (criteria.getInsertType().equals(-2)) {
                BillRecord billRecord3 = new BillRecord();
                billRecord3.setBillNo(billNo);
                billRecord3.setRecordType(43);//调整单草稿
                billRecord3.setCreateTime(DateUtils.getNowDateTimeString());
                billRecord3.setCreateUserId(currentUser.getUserId());
                billRecord3.setCreateUserName(currentUser.getUserName());
                billRecordService.save(billRecord3);
                //billRecord.setRecordType(BillRecordTypeConstants.MANUAL_CREATE_INVENTORY_ADJUST_DRAFT);
                billRecord.setMemo("手动选择库存创建库存调整单草稿");
            } else {
                //billRecord.setRecordType(BillRecordTypeConstants.MANUAL_CREATE_INVENTORY_ADJUST);
                billRecord.setMemo("手动选择库存创建库存调整单");
                inventoryAdjust.setCaseState(caseState);
            }
            for (int j = 0; j < criteria.getAdjustDetails().size(); j++) {
                InventoryAdjustDetail inventoryAdjustDetail = criteria.getAdjustDetails().get(j);
                inventoryAdjustDetail.setBillNo(billNo);
                inventoryAdjustDetail.setDetailNo(billNo + "-" + (j + 1));
                inventoryAdjustDetail.setDifferQuantity(inventoryAdjustDetail.getAdjustQuantity() - inventoryAdjustDetail.getQuantity());
                inventoryAdjustDetail.setState(1);
                inventoryAdjustDetail.setCreateTime(DateUtils.getNowDateTimeString());
                inventoryAdjustDetail.setCreateUseId(currentUser.getUserId());
                inventoryAdjustDetail.setCreateUseName(currentUser.getUserName());
                inventoryAdjustDetail.setMemo(criteria.getMemo());
                inventoryAdjustDetailService.save(inventoryAdjustDetail);
            }
        }

        inventoryAdjustService.save(inventoryAdjust);
        result.append("adjust", inventoryAdjust);
        return ResultGenerator.genSuccessResult(result);
    }

    /**
     * 根据盘点单创建
     */
    @GetMapping("/createByPd")
    @Transactional
    @Authority("inventory_adjusts_createByPd")
    public Result createByPd(Integer pdId, @User CurrentUser currentUser) {

        StockTakeMaster stockTakeMaster = stockTakeMasterService.findById(pdId);
        stockTakeMaster.setState(6);
        stockTakeMasterService.update(stockTakeMaster);
        List<StockTakeTask> list = stockTakeTaskService.findByPdId(pdId);

        String tableName = "inventory_adjust";
        String nextId = validatorService.getNextIdString(tableName);
        String billNo = "IA" + "-" + nextId;
        Integer caseState = 2;
        //添加调整单跟踪信息
        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(billNo);
        billRecord.setRecordType(36);//调整新增
        billRecord.setCreateTime(DateUtils.getNowDateTimeString());
        billRecord.setCreateUserId(currentUser.getUserId());
        billRecord.setCreateUserName(currentUser.getUserName());
        billRecordService.save(billRecord);


        InventoryAdjust inventoryAdjust = new InventoryAdjust();
        inventoryAdjust.setBillNo(billNo);
        inventoryAdjust.setCreateTime(DateUtils.getNowDateTimeString());
        inventoryAdjust.setOrganizationId(stockTakeMaster.getOrganizationId());
        inventoryAdjust.setOrganizationName(stockTakeMaster.getOrganizationName());
        inventoryAdjust.setWareId(stockTakeMaster.getWareId());
        inventoryAdjust.setWareName(stockTakeMaster.getWareName());
        inventoryAdjust.setStockTakeMasterId(stockTakeMaster.getStockTakeMasterId());
        inventoryAdjust.setUserId(currentUser.getUserId());
        inventoryAdjust.setUserName(currentUser.getUserName());
        inventoryAdjust.setCaseState(1);
        inventoryAdjust.setType(1);
        inventoryAdjust.setInsertType(0);
        inventoryAdjustService.save(inventoryAdjust);

        for (int i =0;i<list.size();i++){
            StockTakeTask task  =list.get(i);
            if(task.getState()==3 && task.getDifferQuantity2()!=null && !Objects.equals(nvl(task.getDifferQuantity2()), 0.0d)){
                Inventory inventory = inventoryService.findById(task.getInventoryId());
                InventoryAdjustDetail detail = new InventoryAdjustDetail();
                detail.setBillNo(billNo);
                detail.setDetailNo(billNo+"-"+(i+1));
                detail.setInventoryId(list.get(i).getInventoryId());
                detail.setItemCode(inventory.getItemCode());
                detail.setItemName(inventory.getItemName());
                detail.setBoxCode(inventory.getBoxCode());
                detail.setBatchId(inventory.getBatchId());
                detail.setBatchName(inventory.getBatchName());
                detail.setCellCode(inventory.getCellCode());
                detail.setQuantity(inventory.getQuantity());
                detail.setState(1);
                detail.setAdjustQuantity(inventory.getQuantity());
                detail.setDifferQuantity(task.getDifferQuantity2());
                detail.setAdjustQuantity(inventory.getQuantity()+task.getDifferQuantity2());
                detail.setPackDescribe(inventory.getPackDescribe());
                detail.setPackDetailId(inventory.getPackDetailId());
                inventoryAdjustDetailService.save(detail);
            }

        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 修改库存调整单草稿
     */
    @PostMapping("/updateInventoryAdjust")
    @Transactional
    @Authority("inventory_adjusts_updateInventoryAdjust")
    public Result updateInventoryAdjust(@RequestBody InventoryAdjustCriteria criteria, @User CurrentUser currentUser) {
        InventoryAdjust inventoryAdjust = criteria.getInventoryAdjust();
        BillRecord billRecord;

        billRecord = billRecordService.findByBillNoAndType(inventoryAdjust.getBillNo(), 43);

        billRecord.setModifyTime(DateUtils.getNowDateTimeString());
        billRecord.setModifyUserId(currentUser.getUserId());
        billRecord.setModifyUserName(currentUser.getUserName());
        inventoryAdjustDetailService.deleteByBillNo(inventoryAdjust.getBillNo());
        for (int j = 0; j < criteria.getAdjustDetails().size(); j++) {
            InventoryAdjustDetail inventoryAdjustDetail = criteria.getAdjustDetails().get(j);
            inventoryAdjustDetail.setAdjustDetailId(null);
            inventoryAdjustDetail.setBillNo(inventoryAdjust.getBillNo());
            inventoryAdjustDetail.setDetailNo(inventoryAdjust.getBillNo() + "-" + (j + 1));
            inventoryAdjustDetail.setDifferQuantity(inventoryAdjustDetail.getAdjustQuantity() - inventoryAdjustDetail.getQuantity());
            inventoryAdjustDetail.setState(1);
            inventoryAdjustDetail.setCreateTime(DateUtils.getNowDateTimeString());
            inventoryAdjustDetail.setCreateUseId(currentUser.getUserId());
            inventoryAdjustDetail.setCreateUseName(currentUser.getUserName());
            inventoryAdjustDetail.setMemo(inventoryAdjust.getMemo() + "新增或者减少库存");
            inventoryAdjustDetailService.save(inventoryAdjustDetail);
        }
        billRecordService.update(billRecord);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 库存调整单草稿提交
     */
    @PostMapping("/commitDraft")
    @Transactional
    @Authority("inventory_adjusts_commitDraft")
    public Result commitDraft(@RequestBody InventoryAdjustCriteria criteria, @User CurrentUser currentUser) {
        InventoryAdjust inventoryAdjust = criteria.getInventoryAdjust();
        inventoryAdjustService.deleteById(inventoryAdjust.getInventoryAdjustId());
        String tableName = "inventory_adjust";
        String nextId = validatorService.getNextIdString(tableName);
        String billNo = "IA" + "-" + nextId;

        //添加调整单跟踪信息
        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(billNo);
        billRecord.setRecordType(36);//调整新增
        billRecord.setCreateTime(DateUtils.getNowDateTimeString());
        billRecord.setCreateUserId(currentUser.getUserId());
        billRecord.setCreateUserName(currentUser.getUserName());
        billRecordService.save(billRecord);

        int caseState = caseMasterService.createCase(billNo, 6, currentUser.getUserId(), inventoryAdjust.getWareId(), inventoryAdjust.getOrganizationId());

        inventoryAdjust.setInventoryAdjustId(null);
        inventoryAdjust.setBillNo(billNo);
        inventoryAdjust.setCreateTime(DateUtils.getNowDateTimeString());
        inventoryAdjust.setUserId(currentUser.getUserId());
        inventoryAdjust.setUserName(currentUser.getUserName());
        inventoryAdjust.setCaseState(caseState);
        inventoryAdjust.setInsertType(criteria.getInsertType());
        inventoryAdjustService.save(inventoryAdjust);

        BillRecord billRecord1 = new BillRecord();
        billRecord1.setBillNo(billNo);
        billRecord1.setRecordType(37);//调整审核
        billRecord1.setCreateTime(DateUtils.getNowDateTimeString());
        billRecord1.setCreateUserId(currentUser.getUserId());
        billRecord1.setCreateUserName(currentUser.getUserName());
        billRecordService.createBillRecord(billRecord1, currentUser);
        if (caseState == 1) {
            billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
            billRecord1.setModifyUserId(currentUser.getUserId());
            billRecord1.setModifyUserName(currentUser.getUserName());
            billRecordService.update(billRecord1);
        }
        //BillRecord billRecord = new BillRecord(billNo, DateUtils.getNowDateTimeString(), currentUser.getUserId(), currentUser.getUserName());
        if (inventoryAdjust.getType().equals(1)) {
            //billRecord.setRecordType(BillRecordTypeConstants.UPDATE_INVENTORY_CHECK_CREATE_INVENTORY_ADJUST_DRAFT_TO_OFFICIAL);
            billRecord.setMemo("盘点单库存调整单草稿生成库存调整单");
            billRecordService.update(billRecord);
        } else {
            //billRecord.setRecordType(BillRecordTypeConstants.UPDATE_MANUAL_CREATE_INVENTORY_ADJUST_DRAFT_TO_OFFICIAL);
            billRecord.setMemo("手动选择库存调整单草稿生成库存调整单");
            billRecordService.update(billRecord);

        }

        for (int j = 0; j < criteria.getAdjustDetails().size(); j++) {
            InventoryAdjustDetail inventoryAdjustDetail = criteria.getAdjustDetails().get(j);
            inventoryAdjustDetail.setAdjustDetailId(null);
            inventoryAdjustDetail.setBillNo(billNo);
            inventoryAdjustDetail.setDetailNo(billNo + "-" + (j + 1));
            inventoryAdjustDetail.setDifferQuantity(inventoryAdjustDetail.getAdjustQuantity() - inventoryAdjustDetail.getQuantity());
            inventoryAdjustDetail.setState(1);
            inventoryAdjustDetail.setCreateTime(DateUtils.getNowDateTimeString());
            inventoryAdjustDetail.setCreateUseId(currentUser.getUserId());
            inventoryAdjustDetail.setCreateUseName(currentUser.getUserName());
            inventoryAdjustDetail.setMemo(criteria.getMemo());
            inventoryAdjustDetailService.save(inventoryAdjustDetail);
        }
        BillRecord billRecord2 = billRecordService.findByBillNoAndType(billNo, 36);//调整新增
        billRecord2.setModifyTime(DateUtils.getNowDateTimeString());
        billRecord2.setModifyUserId(currentUser.getUserId());
        billRecord2.setModifyUserName(currentUser.getUserName());
        billRecordService.update(billRecord2);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 审核
     */
    @GetMapping("/review")
    @Authority("inventory_adjusts_review")
    public Result review(String billNo, Integer reviewResult, Integer caseDetailId, String memo, @User CurrentUser currentUser) {
        InventoryAdjust inventoryAdjust = inventoryAdjustService.findBy("billNo", billNo);
        //-1不通过   0-流转中  1-通过  2-初始化
        Integer lastReviewResult = caseMasterService.review(caseDetailId, currentUser.getUserId(), reviewResult, memo);

        if (lastReviewResult == -1) {
            inventoryAdjust.setCaseState(-1);
            inventoryAdjustService.update(inventoryAdjust);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 37);//调整审核
            billRecord.setResult("退回");
            billRecord.setModifyTime(DateUtils.getNowDateTimeString());
            billRecord.setModifyUserId(currentUser.getUserId());
            billRecord.setModifyUserName(currentUser.getUserName());
            billRecordService.update(billRecord);
        } else if (lastReviewResult == 0) {
            inventoryAdjust.setCaseState(0);
            inventoryAdjustService.update(inventoryAdjust);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 37);//调整审核
            billRecord.setResult("流转");
            billRecordService.update(billRecord);
        } else if (lastReviewResult == 2) {
            inventoryAdjust.setCaseState(2);
            inventoryAdjustService.update(inventoryAdjust);
        } else if (lastReviewResult == 1) {
            inventoryAdjust.setCaseState(1);
            inventoryAdjustService.update(inventoryAdjust);

            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 37);//调整审核
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
    @Authority("inventory_adjusts_reReview")
    public Result reReview(String billNo, @User CurrentUser currentUser) {
        InventoryAdjust inventoryAdjust = inventoryAdjustService.findBy("billNo", billNo);

        //初始化状态 2
        inventoryAdjust.setCaseState(2);
        inventoryAdjustService.update(inventoryAdjust);
        caseMasterService.createCase(billNo, 2, currentUser.getUserId(), inventoryAdjust.getWareId(), inventoryAdjust.getOrganizationId());

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 创建库存调整单,外部接口调用
     *
     * 接口创建的库存调整单，默认可以进行批量确认
     */
    @PostMapping("/batchCreateInventoryAdjust")
    @Authority("inventory_adjusts_batchCreateInventoryAdjust")
    public Result batchCreateInventoryAdjust(@RequestBody List<BatchAdjustDetailCriteria> criterias) {
        inventoryAdjustService.batchCreateInventoryAdjust(criterias);
        return ResultGenerator.genSuccessResult();
    }

}
