package com.deer.wms.ware.task.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.ware.task.model.RequestDetail;
import com.deer.wms.ware.task.model.RequestDetailCriteria;
import com.deer.wms.ware.task.service.RequestDetailService;
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
 * 领料单明细
 */
@RestController
@RequestMapping("/request/details")
public class RequestDetailController {

    @Autowired
    private RequestDetailService requestDetailService;

    @PostMapping("/add")
    @Authority("request_details_add")
    public Result add(@RequestBody RequestDetail requestDetail) {
        requestDetailService.save(requestDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("request_details_delete")
    public Result delete(Integer id) {
        requestDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("request_details_update")
    public Result update(@RequestBody RequestDetail requestDetail) {
        requestDetailService.update(requestDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("request_details_get")
    public Result detail(@PathVariable Integer id) {
        RequestDetail requestDetail = requestDetailService.findById(id);
        return ResultGenerator.genSuccessResult(requestDetail);
    }

    @GetMapping("/list")
    @Authority("request_details_list")
    public Result list(RequestDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<RequestDetail> list = requestDetailService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
