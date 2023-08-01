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
import com.deer.wms.review.manage.service.CaseMasterService;
import com.deer.wms.ware.task.model.Allot.AllotData;
import com.deer.wms.ware.task.model.Allot.AllotMaster;
import com.deer.wms.ware.task.model.Allot.AllotMasterCriteria;
import com.deer.wms.ware.task.service.AllotDetailService;
import com.deer.wms.ware.task.service.AllotMasterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 调拨信息api接口
 * 
 * Created by ll on 2020/03/04.
 */
@RestController
@RequestMapping("/allot/masters")
public class AllotMasterController {

    @Autowired
    private AllotMasterService allotMasterService;

    @Autowired
    private CaseMasterService caseMasterService;

    @Autowired
    private BillRecordService billRecordService;

    /**
     * 新增调拨信息
     */
    @PostMapping("/insert")
    @Authority("allot_masters_insert")
    public Result add(@RequestBody AllotData allotData, @User CurrentUser currentUser) {
        allotMasterService.createAllotMaster(allotData, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("allot_masters_delete")
    public Result delete(Integer id) {
        allotMasterService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("allot_masters_update")
    public Result update(@RequestBody AllotMaster allotMaster) {
        allotMasterService.update(allotMaster);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("allot_masters_detail")
    public Result detail(Integer id) {
        AllotMaster allotMaster = allotMasterService.findById(id);
        return ResultGenerator.genSuccessResult(allotMaster);
    }

    @GetMapping("/list")
    @Authority("allot_masters_list")
    public Result list(AllotMasterCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<AllotMaster> list = allotMasterService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Autowired
    private AllotDetailService allotDetailService;

    /**
     * 审核
     */
    @GetMapping("/review")
    @Authority("allot_masters_review")
    public Result review(String billNo, Integer reviewResult, Integer caseDetailId, String memo, @User CurrentUser currentUser) {
        AllotMaster allotMaster = allotMasterService.findBy("billNo", billNo);
        //-1不通过   0-流转中  1-通过  2-初始化
        Integer lastReviewResult = caseMasterService.review(caseDetailId, currentUser.getUserId(), reviewResult, memo);
        if (lastReviewResult == -1) {
            allotMaster.setState(-1);
            allotMasterService.update(allotMaster);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 40);//调拨审核
            billRecord.setResult("退回");
            billRecord.setModifyTime(DateUtils.getNowDateTimeString());
            billRecord.setModifyUserId(currentUser.getUserId());
            billRecord.setModifyUserName(currentUser.getUserName());
            billRecordService.update(billRecord);
        } else if (lastReviewResult == 0) {
            allotMaster.setState(0);
            allotMasterService.update(allotMaster);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 40);//调拨审核
            billRecord.setResult("流转");
            billRecordService.update(billRecord);
        } else if (lastReviewResult == 2) {
            allotMaster.setState(2);
            allotMasterService.update(allotMaster);
        } else if (lastReviewResult == 1) {
            allotMaster.setState(1);
            allotMasterService.update(allotMaster);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 40);//调拨审核
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
    @Authority("allot_masters_reReview")
    public Result reReview(String billNo, @User CurrentUser currentUser) {
        AllotMaster allotMaster = allotMasterService.findBy("billNo", billNo);

        //初始化状态 2
        allotMaster.setState(2);
        allotMasterService.update(allotMaster);
        caseMasterService.createCase(billNo, 2, currentUser.getUserId(), allotMaster.getFromWareId(), allotMaster.getOrganizationId());

        return ResultGenerator.genSuccessResult();
    }

}
