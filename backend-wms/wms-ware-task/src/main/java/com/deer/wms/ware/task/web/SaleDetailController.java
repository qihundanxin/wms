package com.deer.wms.ware.task.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.ware.task.model.Sale.SaleDetail;
import com.deer.wms.ware.task.model.Sale.SaleDetailCriteria;
import com.deer.wms.ware.task.service.SaleDetailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 销售信息api接口
 * 
 * Created by ll on 2020/03/13.
 */
@RestController
@RequestMapping("/sale/details")
public class SaleDetailController {

    @Autowired
    private SaleDetailService saleDetailService;

    @PostMapping("/insert")
    @Authority("sale_details_insert")
    public Result insert(@RequestBody SaleDetail saleDetail) {
        saleDetailService.save(saleDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("sale_details_delete")
    public Result delete(Integer id) {
        saleDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("sale_details_update")
    public Result update(@RequestBody SaleDetail saleDetail) {
        saleDetailService.update(saleDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("sale_details_detail")
    public Result detail(Integer id) {
        SaleDetail saleDetail = saleDetailService.findById(id);
        return ResultGenerator.genSuccessResult(saleDetail);
    }

    @GetMapping("/list")
    @Authority("sale_details_list")
    public Result list(SaleDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SaleDetail> list = saleDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
