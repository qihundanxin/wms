package com.deer.wms.ware.task.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.service.BillRecordService;
import com.deer.wms.review.manage.service.CaseDetailService;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.deer.wms.ware.task.model.Sale.SaleData;
import com.deer.wms.ware.task.model.Sale.SaleMaster;
import com.deer.wms.ware.task.model.Sale.SaleMasterCriteria;
import com.deer.wms.ware.task.service.SaleMasterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 销售信息api接口
 * 
 * Created by ll on 2020/03/13.
 */
@RestController
@RequestMapping("/sale/masters")
public class SaleMasterController {

    @Autowired
    private SaleMasterService saleMasterService;

    @Autowired
    private CaseMasterService caseMasterService;

    @Autowired
    private CaseDetailService caseDetailService;

    @Autowired
    private BillRecordService billRecordService;

    @PostMapping
    @Authority("sale_masters_add")
    public Result add(@RequestBody SaleMaster saleMaster) {
        saleMasterService.save(saleMaster);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 新增销售单信息
     */
    @PostMapping("/insert")
    @Authority("sale_masters_insert")
    public Result insert(@RequestBody SaleData saleData, @User CurrentUser currentUser) {
        saleMasterService.createSale(saleData, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("sale_masters_delete")
    public Result delete(Integer id) {
        saleMasterService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("sale_masters_update")
    public Result update(@RequestBody SaleMaster saleMaster) {
        saleMasterService.update(saleMaster);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("sale_masters_detail")
    public Result detail(Integer id) {
        SaleMaster saleMaster = saleMasterService.findById(id);
        return ResultGenerator.genSuccessResult(saleMaster);
    }

    @GetMapping("/list")
    @Authority("sale_masters_list")
    public Result list(SaleMasterCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SaleMaster> list = saleMasterService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 生成SO单
     */
    @PostMapping("/createSo")
    @Authority("sale_masters_createSo")
    public Result createSo(@RequestBody SaleData saleData, CurrentUser currentUser) {
        saleMasterService.createSo(saleData, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 审核
     */
    @GetMapping("/review")
    @Authority("sale_masters_review")
    public Result review(String billNo, Integer reviewResult, Integer caseDetailId, String memo, @User CurrentUser currentUser) {
        SaleMaster saleMaster = saleMasterService.findBy("billNo", billNo);
        //-1不通过   0-流转中  1-通过  2-初始化
        Integer lastReviewResult = caseMasterService.review(caseDetailId, currentUser.getUserId(), reviewResult, memo);
        if (lastReviewResult == -1) {
            saleMaster.setState(-1);
            saleMasterService.update(saleMaster);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 45);//销售审核
            billRecord.setResult("退回");
            billRecord.setModifyTime(DateUtils.getNowDateTimeString());
            billRecord.setModifyUserId(currentUser.getUserId());
            billRecord.setModifyUserName(currentUser.getUserName());
            billRecordService.update(billRecord);
        } else if (lastReviewResult == 0) {
            saleMaster.setState(0);
            saleMasterService.update(saleMaster);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 45);//销售审核
            billRecord.setResult("流转");
            billRecordService.update(billRecord);
        } else if (lastReviewResult == 2) {
            saleMaster.setState(2);
            saleMasterService.update(saleMaster);
        } else if (lastReviewResult == 1) {
            saleMaster.setState(1);
            saleMasterService.update(saleMaster);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 45);//销售审核
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
    @Authority("sale_masters_reReview")
    public Result reReview(String billNo, @User CurrentUser currentUser) {
        SaleMaster saleMaster = saleMasterService.findBy("billNo", billNo);

        //初始化状态 2
        saleMaster.setState(2);
        saleMasterService.update(saleMaster);
        caseMasterService.createCase(billNo, 8, currentUser.getUserId(), saleMaster.getWareId(), saleMaster.getOrganizationId());

        return ResultGenerator.genSuccessResult();
    }

}
