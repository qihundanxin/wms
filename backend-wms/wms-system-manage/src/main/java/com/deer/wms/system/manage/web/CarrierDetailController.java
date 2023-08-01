package com.deer.wms.system.manage.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.system.manage.model.carrier.CarrierDetail;
import com.deer.wms.system.manage.model.carrier.CarrierDetailCriteria;
import com.deer.wms.system.manage.service.CarrierDetailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by guo on 2020/01/06.
 *
 * 承运方明细
 */
@RestController
@RequestMapping("/carrier/details")
public class CarrierDetailController {

    @Autowired
    private CarrierDetailService carrierDetailService;

    @PostMapping("/insert")
    @Authority("carriers_details_insert")
    public Result add(@RequestBody CarrierDetail carrierDetail) {
        carrierDetailService.save(carrierDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("carriers_details_delete")
    public Result delete(Integer id) {
        carrierDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("carriers_details_update")
    public Result update(@RequestBody CarrierDetail carrierDetail) {
        carrierDetailService.update(carrierDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("carriers_details_detail")
    public Result detail(@PathVariable Integer id) {
        CarrierDetail carrierDetail = carrierDetailService.findById(id);
        return ResultGenerator.genSuccessResult(carrierDetail);
    }

    @GetMapping("/list")
    @Authority("carriers_details_list")
    public Result list(CarrierDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<CarrierDetail> list = carrierDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
