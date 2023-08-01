package com.deer.wms.ware.task.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.ware.task.model.Return.ReturnDetail;
import com.deer.wms.ware.task.model.Return.ReturnDetailCriteria;
import com.deer.wms.ware.task.service.ReturnDetailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 退货单详细信息api接口
 * 
 * Created by ll on 2020/03/13.
 */
@RestController
@RequestMapping("/return/details")
public class ReturnDetailController {

    @Autowired
    private ReturnDetailService returnDetailService;

    @PostMapping
    @Authority("request_details_add")
    public Result add(@RequestBody ReturnDetail returnDetail) {
        returnDetailService.save(returnDetail);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/insert")
    @Authority("request_details_insert")
    public Result insert(@RequestBody ReturnDetail returnDetail) {
        returnDetailService.save(returnDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("request_details_delete")
    public Result delete(@PathVariable Integer id) {
        returnDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("request_details_update")
    public Result update(@RequestBody ReturnDetail returnDetail) {
        returnDetailService.update(returnDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("request_details_detail")
    public Result detail(@PathVariable Integer id) {
        ReturnDetail returnDetail = returnDetailService.findById(id);
        return ResultGenerator.genSuccessResult(returnDetail);
    }

    @GetMapping("/list")
    @Authority("request_details_list")
    public Result list(ReturnDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ReturnDetail> list = returnDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
