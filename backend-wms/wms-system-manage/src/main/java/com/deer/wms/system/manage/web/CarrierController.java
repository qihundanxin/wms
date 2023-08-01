package com.deer.wms.system.manage.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.util.RedisUtil;
import com.deer.wms.system.manage.model.carrier.Carrier;
import com.deer.wms.system.manage.model.carrier.CarrierCriteria;
import com.deer.wms.system.manage.model.carrier.CarrierDto;
import com.deer.wms.system.manage.service.CarrierService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 快递信息api接口--承运商
 * 
 * Created by guo on 2019/11/13.
 */
@RestController
@RequestMapping("/carriers")
public class CarrierController {

    @Autowired
    private CarrierService carrierService;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/insert")
    @Authority("carriers_insert")
    public Result add(@RequestBody Carrier carrier) {
        carrierService.save(carrier);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("carriers_delete")
    public Result delete(Integer carrierId) {
        carrierService.deleteById(carrierId);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("carriers_update")
    public Result update(@RequestBody Carrier carrier) {
        carrierService.update(carrier);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("carriers_detail")
    public Result detail(@PathVariable Integer id) {
        Carrier carrier = carrierService.findById(id);
        return ResultGenerator.genSuccessResult(carrier);
    }

    /**
     * 快递信息
     */
    @GetMapping("/list")
    @Authority("carriers_list")
    public Result list(CarrierCriteria criteria, @User CurrentUser currentUser) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<CarrierDto> list = carrierService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Transactional
    @GetMapping("/allList")
    @Authority("carriers_allList")
    public Result allList(@User CurrentUser currentUser) {

//        List<Carrier> list = (List<Carrier>) redisUtil.get("allCarrier");
//        if(list==null || list.size()==0){
//            list = carrierService.findAll();
//            redisUtil.set("allCarrier",list);
//        }
        List<Carrier> list = carrierService.findAll();

        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
