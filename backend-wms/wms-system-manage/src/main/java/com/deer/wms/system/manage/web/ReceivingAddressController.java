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
import com.deer.wms.system.manage.model.address.ReceivingAddress;
import com.deer.wms.system.manage.model.address.ReceivingAddressCreate;
import com.deer.wms.system.manage.model.address.ReceivingAddressCriteria;
import com.deer.wms.system.manage.model.address.ReceivingAddressDto;
import com.deer.wms.system.manage.service.ReceivingAddressService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 客户收件信息api接口
 * 
 * Created by guo on 2019/11/13.
 */
@RestController
@RequestMapping("/receiving/addresss")
public class ReceivingAddressController {

    @Autowired
    private ReceivingAddressService receivingAddressService;

    /**
     * 新增客户收件地址
     */
    @PostMapping("/insert")
    @Authority("receiving_addresss_insert")
    public Result add(@RequestBody ReceivingAddressCreate create, @User CurrentUser currentUser) {
        receivingAddressService.createReceivingAddress(create, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("receiving_addresss_delete")
    public Result delete(Integer id) {
        receivingAddressService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("receiving_addresss_update")
    public Result update(@RequestBody ReceivingAddress receivingAddress) {

        if (receivingAddress.getState() == 1) {
            List<ReceivingAddressDto> list = receivingAddressService.findByCustomerId(receivingAddress.getCustomerId());
            for (ReceivingAddress receivingAddress1 : list) {
                receivingAddress1.setState(0);
                receivingAddressService.update(receivingAddress1);
            }
        }
        receivingAddressService.update(receivingAddress);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("receiving_addresss_detail")
    public Result detail(Integer id) {
        ReceivingAddress receivingAddress = receivingAddressService.findById(id);
        return ResultGenerator.genSuccessResult(receivingAddress);
    }

    /**
     * 客户收件地址信息
     */
    @GetMapping("/list")
    @Authority("receiving_addresss_list")
    public Result list(ReceivingAddressCriteria criteria, @User CurrentUser currentUser) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ReceivingAddressDto> list = receivingAddressService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
