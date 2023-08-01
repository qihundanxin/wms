package com.deer.wms.base.system.web;

import com.deer.wms.base.system.model.WareShop;
import com.deer.wms.base.system.model.WareShopCriteria;
import com.deer.wms.base.system.service.WareShopService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.system.manage.service.WareInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
* Created by  on 2021/09/08.
 *
*/
@RestController
@RequestMapping("/ware/shops")
public class WareShopController {

    @Autowired
    private WareShopService wareShopService;
    @Autowired
    private WareInfoService wareInfoService;

    @PostMapping("/add")
    @Authority("ware_shops_add")
    public Result add(@RequestBody WareShop wareShop, @User CurrentUser currentUser) {
        WareInfo wareInfo = wareInfoService.findById(wareShop.getWareId());
        wareShop.setWareName(wareInfo.getWareName());
        wareShop.setCreateTime(DateUtils.getNowDateTimeString());
        wareShop.setCreateUserId(currentUser.getUserId());
        wareShop.setCreateUserName(currentUser.getUserName());
        wareShopService.save(wareShop);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("ware_shops_delete")
    public Result delete(  Integer id) {
        wareShopService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("ware_shops_update")
    public Result update(@RequestBody WareShop wareShop) {
        wareShopService.update(wareShop);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("ware_shops_get")
    public Result detail(@PathVariable Integer id) {
        WareShop wareShop = wareShopService.findById(id);
        return ResultGenerator.genSuccessResult(wareShop);
    }

    @GetMapping("/list")
    @Authority("ware_shops_list")
    public Result list(WareShopCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<WareShop> list = wareShopService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
