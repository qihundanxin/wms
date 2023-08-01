package com.deer.wms.review.manage.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.review.manage.model.CaseDetail;
import com.deer.wms.review.manage.model.CaseDetailCriteria;
import com.deer.wms.review.manage.model.CaseDetailDto;
import com.deer.wms.review.manage.model.CaseMaster;
import com.deer.wms.review.manage.service.CaseDetailService;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.deer.wms.review.manage.service.CaseUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


/**
 * 审核内容详细信息api接口
 * 
 * Created by guo on 2018/10/24.
 */
@RestController
@RequestMapping("/case/details")
public class CaseDetailController {

    @Autowired
    private CaseDetailService caseDetailService;

    @Autowired
    private CaseMasterService caseMasterService;

    @Autowired
    private CaseUserService caseUserService;

    @PostMapping("/add")
    @Authority("case_details_add")
    public Result add(@RequestBody CaseDetail caseDetail) {
        caseDetailService.save(caseDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("case_details_delete")
    public Result delete(Integer caseDetailId) {
        caseDetailService.deleteById(caseDetailId);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("case_details_update")
    public Result update(@RequestBody CaseDetail caseDetail) {
        caseDetailService.update(caseDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("case_details_get")
    public Result detail(@PathVariable Integer id) {
        CaseDetail caseDetail = caseDetailService.findById(id);
        return ResultGenerator.genSuccessResult(caseDetail);
    }

    @GetMapping("/list")
    @Authority("case_details_list")
    public Result list(CaseDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<CaseDetailDto> list = caseDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/nowList")
    @Authority("case_details_nowList")
    public Result nowList(CaseDetailCriteria criteria, @User CurrentUser currentUser) {
        criteria.setUserId(currentUser.getUserId());
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<CaseDetailDto> list = caseDetailService.findNowList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/nowDetails")
    @Authority("case_details_nowDetails")
    public Result nowDetails(String billNo, @User CurrentUser currentUser) {

        /***
         * 查询caseMaster
         */
        CaseMaster caseMaster = caseMasterService.findByBillNo(billNo);
        if (caseMaster == null) {
            throw new ServiceException(CommonCode.CASE_NULL);
        }

        /**
         * 查询caseDetail
         */

        List<CaseDetailDto> list = caseDetailService.findByMasterId(caseMaster.getCaseMasterId());

        /**
         * 遍历details,
         * 查询caseUsers
         * 赋值
         */
//        for(int i=0;i<list.size();i++){
//
//            Integer caseDetailId = list.get(i).getCaseDetailId();
//            List<CaseUser> caseUsers = caseUserService.findByCaseDetailId(caseDetailId);
//            for(int y=0;y<caseUsers.size();y++){
//                CaseUser caseUser = new CaseUser();
//                caseUser.setCaseDetailId(caseUser.getCaseDetailId());
//                caseUser.setUserId(caseUser.getUserId());
//                caseUser.setUserName(caseUser.getUserName());
//                caseUser.setReviewResult(caseUser.getReviewResult());
//                caseUser.setMemo(caseUser.getMemo());
//            }
//
//        }

        return ResultGenerator.genSuccessResult(list);
    }

}
