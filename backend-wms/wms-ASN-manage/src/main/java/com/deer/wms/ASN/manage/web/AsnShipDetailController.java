package com.deer.wms.ASN.manage.web;

import com.deer.wms.ASN.manage.model.asn.AsnShipDetail;
import com.deer.wms.ASN.manage.model.asn.AsnShipDetailCriteria;
import com.deer.wms.ASN.manage.model.asn.AsnShipDetailDto;
import com.deer.wms.ASN.manage.service.AsnShipDetailService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.project.root.util.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by  on 2020/12/15.
 */
@RestController
@RequestMapping("/asn/ship/details")
public class AsnShipDetailController {

    @Autowired
    private AsnShipDetailService asnShipDetailService;

    @PostMapping("/add")
    @Authority("asn_ship_details_add")
    public Result add(@RequestBody AsnShipDetail asnShipDetail) {
        asnShipDetailService.save(asnShipDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("asn_ship_details_delete")
    public Result delete(Integer id) {
        asnShipDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("asn_ship_details_update")
    public Result update(@RequestBody AsnShipDetail asnShipDetail) {
        asnShipDetailService.update(asnShipDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("asn_ship_details_get")
    public Result detail(@PathVariable Integer id) {
        AsnShipDetail asnShipDetail = asnShipDetailService.findById(id);
        return ResultGenerator.genSuccessResult(asnShipDetail);
    }

    @GetMapping("/list")
    @Authority("asn_ship_details_list")
    public Result list(AsnShipDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<AsnShipDetail> list = asnShipDetailService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/findByBillNo")
    @Authority("asn_ship_details_findByBillNo")
    public Result findByBillNo(String billNo) {

        List<AsnShipDetailDto> list = asnShipDetailService.findByBillNo(billNo);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/accept")
    @Authority("asn_ship_details_accept")
    public Result accept(AsnShipDetail asnShipDetail) {
        AsnShipDetail asnShipDetail1 = asnShipDetailService.findBy("shipBillCode", asnShipDetail.getShipBillCode());
        if(asnShipDetail1!=null){
            asnShipDetail1.setAcceptTime(DateUtils.getNowDateTimeString());
            asnShipDetail1.setLength(asnShipDetail.getLength());
            asnShipDetail1.setWeight(asnShipDetail.getWeight());
            asnShipDetail1.setHeight(asnShipDetail.getHeight());
            asnShipDetail1.setWidth(asnShipDetail.getWidth());
            asnShipDetailService.update(asnShipDetail1);
        }

        return ResultGenerator.genSuccessResult();
    }

}
