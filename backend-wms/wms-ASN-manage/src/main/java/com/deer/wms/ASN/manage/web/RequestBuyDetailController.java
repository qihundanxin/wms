package com.deer.wms.ASN.manage.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.ASN.manage.model.buy.RequestBuyDetail;
import com.deer.wms.ASN.manage.model.buy.RequestBuyDetailCriteria;
import com.deer.wms.ASN.manage.model.buy.RequestBuyDetailDto;
import com.deer.wms.ASN.manage.service.RequestBuyDetailService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 采购详情api接口
 * 
 * Created by guo on 2020/01/02.
 */
@RestController
@RequestMapping("/request/buy/details")
public class RequestBuyDetailController {

    @Autowired
    private RequestBuyDetailService requestBuyDetailService;

    @PostMapping("/insert")
    @Authority("request_buy_details_insert")
    public Result add(@RequestBody RequestBuyDetail requestBuyDetail) {
        requestBuyDetailService.save(requestBuyDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("request_buy_details_delete")
    public Result delete(Integer id) {
        requestBuyDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("request_buy_details_update")
    public Result update(@RequestBody RequestBuyDetail requestBuyDetail) {
        requestBuyDetailService.update(requestBuyDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("request_buy_details_detail")
    public Result detail(@PathVariable Integer id) {
        RequestBuyDetail requestBuyDetail = requestBuyDetailService.findById(id);
        return ResultGenerator.genSuccessResult(requestBuyDetail);
    }

    @GetMapping("/list")
    @Authority("request_buy_details_list")
    public Result list(RequestBuyDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<RequestBuyDetailDto> list = requestBuyDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
