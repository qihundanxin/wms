package com.deer.wms.ASN.manage.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.ASN.manage.model.asn.AsnDetail;
import com.deer.wms.ASN.manage.model.asn.AsnDetailCriteria;
import com.deer.wms.ASN.manage.model.asn.AsnDetailDto;
import com.deer.wms.ASN.manage.service.AsnDetailService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * ASN详情api接口
 * 
 * Created by guo on 2020/01/03.
 */
@RestController
@RequestMapping("/asn/details")
public class AsnDetailController {

    @Autowired
    private AsnDetailService asnDetailService;

    @PostMapping("/insert")
    @Authority("asn_details_insert")
    public Result add(@RequestBody AsnDetail asnDetail) {
        asnDetailService.save(asnDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("asn_details_delete")
    public Result delete(Integer id) {
        asnDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

//    @PostMapping("/update")
//    public Result update(@RequestBody AsnDetail asnDetail) {
//        asnDetailService.update(asnDetail);
//        return ResultGenerator.genSuccessResult();
//    }

    @GetMapping("/detail")
    @Authority("asn_details_detail")
    public Result detail(Integer id) {
        AsnDetail asnDetail = asnDetailService.findById(id);
        return ResultGenerator.genSuccessResult(asnDetail);
    }

    @GetMapping("/list")
    @Authority("asn_details_list")
    public Result list(AsnDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<AsnDetailDto> list = asnDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/findByDetailNo")
    @Authority("asn_details_findByDetailNo")
    public Result findByDetailNo(AsnDetailCriteria criteria) {
//        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<AsnDetailDto> list = asnDetailService.findByDetailNo(criteria.getDetailNo());
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/findByBillNo")
    @Authority("asn_details_findByBillNo")
    public Result findByBillNo(AsnDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<AsnDetail> list = asnDetailService.findByBillNo(criteria.getBillNo());
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
