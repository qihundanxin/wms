package com.deer.wms.system.manage.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.system.manage.model.address.ShippingAddress;
import com.deer.wms.system.manage.model.address.ShippingAddressCreate;
import com.deer.wms.system.manage.model.address.ShippingAddressCriteria;
import com.deer.wms.system.manage.model.address.ShippingAddressDto;
import com.deer.wms.system.manage.service.ShippingAddressService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 寄件地址api接口
 * 
 * Created by guo on 2019/11/13.
 */
@RestController
@RequestMapping("/shipping/addresss")
public class ShippingAddressController {

    @Autowired
    private ShippingAddressService shippingAddressService;

    /**
     * 新增寄件地址信息
     */
    @PostMapping("/insert")
    @Authority("shipping_addresss_insert")
    public Result createShippingAddress(@RequestBody ShippingAddressCreate create, @User CurrentUser currentUser) {
        shippingAddressService.createShippingAddress(create, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 删除寄件地址
     */
    @GetMapping("/delete")
    @Authority("shipping_addresss_delete")
    public Result delete(Integer id) {
        shippingAddressService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 更新寄件地址
     */
    @PostMapping("/update")
    @Authority("shipping_addresss_update")
    public Result update(@RequestBody ShippingAddress shippingAddress) {
        if (shippingAddress.getState() == 1) {
            List<ShippingAddressDto> list = shippingAddressService.findByOrganizationIdAndWareId(shippingAddress.getOrganizationId(), shippingAddress.getWareId());
            for (ShippingAddress shippingAddress1 : list) {
                shippingAddress1.setState(0);
                shippingAddressService.update(shippingAddress1);
            }
        }
        shippingAddressService.update(shippingAddress);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 查询单个寄件地址
     */
    @GetMapping("/detail")
    @Authority("shipping_addresss_detail")
    public Result detail(Integer id) {
        ShippingAddress shippingAddress = shippingAddressService.findById(id);
        return ResultGenerator.genSuccessResult(shippingAddress);
    }

    /**
     * 查询寄件地址信息
     */
    @GetMapping("/list")
    @Authority("shipping_addresss_list")
    public Result list(ShippingAddressCriteria criteria, @User CurrentUser currentUser) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ShippingAddressDto> list = shippingAddressService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
