package com.deer.wms.ware.task.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.ware.task.model.RequestDetail;
import com.deer.wms.ware.task.model.RequestInsert;
import com.deer.wms.ware.task.model.RequestMaster;
import com.deer.wms.ware.task.model.RequestMasterCriteria;
import com.deer.wms.ware.task.service.RequestMasterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by guo on 2020/04/19.
 *
 * 领料单
 */
@RestController
@RequestMapping("/request/masters")
public class RequestMasterController {

    @Autowired
    private RequestMasterService requestMasterService;

    @PostMapping("/add")
    @Authority("request_masters_add")
    public Result add(@RequestBody RequestMaster requestMaster) {
        requestMasterService.save(requestMaster);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/insert")
    @Authority("request_masters_insert")
    public Result insert(@RequestBody RequestInsert requestInsert, @User CurrentUser currentUser) {
        /*if(currentUser == null){
            throw new ServiceException(CommonCode.MISSING_PERMISSION_PARAMTER, null);
        }
        RequestMaster requestMaster = requestInsert.getRequestMaster();
        requestMaster.setCreateTime(DateUtils.getNowDateTimeString());
        requestMaster.setCreateUserId(currentUser.getUserId());
        requestMaster.setCreateUserName(currentUser.getUserName());
        requestMaster.setState(0);
        requestMasterService.save(requestMaster);
        List<RequestDetail> requestDetails = requestInsert.getRequestDetailList();

        for (RequestDetail requestDetail: requestDetails  ) {
            requestDetail.setRequestMasterId(requestMaster.getRequestMasterId());
            requestDetailService.save(requestDetail);
        }*/

        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("request_masters_delete")
    public Result delete(Integer id) {
        requestMasterService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("request_masters_update")
    public Result update(@RequestBody RequestMaster requestMaster) {
        requestMasterService.update(requestMaster);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("request_masters_get")
    public Result detail(@PathVariable Integer id) {
        RequestMaster requestMaster = requestMasterService.findById(id);
        return ResultGenerator.genSuccessResult(requestMaster);
    }

    @GetMapping("/list")
    @Authority("request_masters_list")
    public Result list(RequestMasterCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<RequestMaster> list = requestMasterService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
