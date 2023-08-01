package com.deer.wms.ware.task.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.inventory.model.Inventory.InventoryTransactRunCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.constant.InventoryConstant;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.inventory.service.InventoryTransactService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.service.BillRecordService;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.deer.wms.ware.task.model.Return.ReturnData;
import com.deer.wms.ware.task.model.Return.ReturnDetail;
import com.deer.wms.ware.task.model.Return.ReturnDetailData;
import com.deer.wms.ware.task.model.Return.ReturnMaster;
import com.deer.wms.ware.task.model.Return.ReturnMasterCriteria;
import com.deer.wms.ware.task.service.ReturnDetailService;
import com.deer.wms.ware.task.service.ReturnMasterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 退货单信息api接口
 * 
 * Created by ll on 2020/03/13.
 */
@RestController
@RequestMapping("/return/masters")
public class ReturnMasterController {

    @Autowired
    private ReturnMasterService returnMasterService;

    @Autowired
    private CaseMasterService caseMasterService;

    @Autowired
    private BillRecordService billRecordService;

    @Autowired
    private ReturnDetailService returnDetailService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryTransactService inventoryTransactService;

    @PostMapping
    @Authority("return_masters_add")
    public Result add(@RequestBody ReturnMaster returnMaster) {
        returnMasterService.save(returnMaster);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 新增退货信息
     */
    @PostMapping("/insert")
    @Authority("return_masters_insert")
    public Result insert(@RequestBody ReturnData returnData, @User CurrentUser currentUser) {
        returnMasterService.create(returnData, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("return_masters_delete")
    public Result delete(Integer id) {
        returnMasterService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("return_masters_detail")
    public Result detail(Integer id) {
        ReturnMaster returnMaster = returnMasterService.findById(id);
        return ResultGenerator.genSuccessResult(returnMaster);
    }

    @GetMapping("/list")
    @Authority("return_masters_list")
    public Result list(ReturnMasterCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ReturnMaster> list = returnMasterService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 审核
     */
    @GetMapping("/review")
    @Authority("return_masters_review")
    public Result review(String billNo, Integer reviewResult, Integer caseDetailId, String memo, @User CurrentUser currentUser) {
        ReturnMaster returnMaster = returnMasterService.findBy("billNo", billNo);
        //-1不通过   0-流转中  1-通过  2-初始化
        Integer lastReviewResult = caseMasterService.review(caseDetailId, currentUser.getUserId(), reviewResult, memo);

        if (lastReviewResult == -1) {
            returnMaster.setState(-1);
            returnMasterService.update(returnMaster);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 49);//退货审核
            billRecord.setResult("退回");
            billRecord.setModifyTime(DateUtils.getNowDateTimeString());
            billRecord.setModifyUserId(currentUser.getUserId());
            billRecord.setModifyUserName(currentUser.getUserName());
            billRecordService.update(billRecord);
        } else if (lastReviewResult == 0) {
            returnMaster.setState(0);
            returnMasterService.update(returnMaster);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 49);//退货审核
            billRecord.setResult("流转");
            billRecordService.update(billRecord);
        } else if (lastReviewResult == 2) {
            returnMaster.setState(2);
            returnMasterService.update(returnMaster);
        } else if (lastReviewResult == 1) {
            returnMaster.setState(1);
            returnMasterService.update(returnMaster);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 49);//退货审核
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
    @Authority("return_masters_reReview")
    public Result reReview(String billNo, @User CurrentUser currentUser) {
        ReturnMaster returnMaster = returnMasterService.findBy("billNo", billNo);

        //初始化状态 2
        returnMaster.setState(2);
        returnMasterService.update(returnMaster);
        caseMasterService.createCase(billNo, 8, currentUser.getUserId(), returnMaster.getWareId(), returnMaster.getOrganizationId());

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 增删退货单
     */
    @PostMapping("/update")
    @Authority("return_masters_update")
    public Result update(@RequestBody ReturnData returnData, @User CurrentUser currentUser) {
        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(returnData.getReturnMaster().getBillNo());
        billRecord.setRecordType(50);//退货修改
        billRecord.setCreateTime(DateUtils.getNowDateTimeString());
        billRecord.setCreateUserId(currentUser.getUserId());
        billRecord.setCreateUserName(currentUser.getUserName());
        billRecordService.createBillRecord(billRecord, currentUser);
        ReturnMaster returnMaster = returnData.getReturnMaster();
        returnMasterService.update(returnMaster);

        List<ReturnDetail> returnDetails = returnData.getReturnDetails();

        for (int i = 0; i < returnDetails.size(); i++) {
            ReturnDetail returnDetail = returnDetails.get(i);
            String detailNo = returnData.getReturnMaster().getBillNo() + "-" + (i + 1);
            returnDetail.setDetailNo(detailNo);
            returnDetail.setBillNo(returnData.getReturnMaster().getBillNo());
            returnDetail.setDetailNo(detailNo);

            returnDetailService.update(returnDetail);
        }
        BillRecord billRecord1 = billRecordService.findByBillNoAndType(returnData.getReturnMaster().getBillNo(), 50);//退货修改
        billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
        billRecord1.setModifyUserId(currentUser.getUserId());
        billRecord1.setModifyUserName(currentUser.getUserName());
        billRecordService.update(billRecord1);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 批量退货
     */
    @PostMapping("/return")
    @Authority("return_masters_return")
    public Result delivery(@RequestBody ReturnDetailData returnDetailData, @User CurrentUser user) {
        for (ReturnDetail returnDetail : returnDetailData.getReturnDetails()) {
            Integer inventoryId = returnDetail.getInventoryId();
            String billNo = returnDetail.getBillNo();
            ReturnMaster returnMaster = returnMasterService.findBy("billNo", billNo);

            Inventory fromIv = inventoryService.findById(inventoryId);
            Inventory toIv = new Inventory();
            BeanUtils.copyProperties(fromIv, toIv);
            toIv.setOrganizationId(returnMaster.getOrganizationId());
            toIv.setOrganizationName(returnMaster.getOrganizationName());
            inventoryService.save(toIv);
            InventoryTransactRunCriteria runCriteria = new InventoryTransactRunCriteria();
            runCriteria.setFromInventory(fromIv);
            runCriteria.setToInventory(toIv);
            runCriteria.setTransactType(InventoryConstant.INVENTORY_TRANS_ORG);
            runCriteria.setDetailNo(returnDetail.getDetailNo());
            runCriteria.setCreateUserId(user.getUserId());
            runCriteria.setCreateUserName(user.getUserName());
            inventoryTransactService.run(runCriteria);

        }

        return ResultGenerator.genSuccessResult();
    }

}
