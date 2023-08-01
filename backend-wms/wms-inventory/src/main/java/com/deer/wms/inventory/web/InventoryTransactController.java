package com.deer.wms.inventory.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.inventory.model.Inventory.InventoryTransact;
import com.deer.wms.inventory.model.Inventory.InventoryTransactCriteria;
import com.deer.wms.inventory.model.Inventory.InventoryTransactDto;
import com.deer.wms.inventory.service.InventoryTransactService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 库存事务api接口
 * 
 * Created by 郭靖勋 on 2019/12/18.
 */
@RestController
@RequestMapping("/inventory/transacts")
public class InventoryTransactController {

    @Autowired
    private InventoryTransactService inventoryTransactService;

    @PostMapping("/insert")
    @Authority("inventory_transacts_insert")
    public Result add(@RequestBody InventoryTransact inventoryTransact) {
        inventoryTransactService.save(inventoryTransact);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("inventory_transacts_delete")
    public Result delete(Integer id) {
        inventoryTransactService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("inventory_transacts_update")
    public Result update(@RequestBody InventoryTransact inventoryTransact) {
        inventoryTransactService.update(inventoryTransact);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("inventory_transacts_detail")
    public Result detail(@PathVariable Integer id) {
        InventoryTransact inventoryTransact = inventoryTransactService.findById(id);
        return ResultGenerator.genSuccessResult(inventoryTransact);
    }

    @GetMapping("/list")
    @Authority("inventory_transacts_list")
    public Result list(InventoryTransactCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<InventoryTransactDto> list = inventoryTransactService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    //调整库存
    @PostMapping("/addTransact")
    @Authority("inventory_transacts_addTransact")
    public Result insert(@RequestBody InventoryTransact inventoryTransact) {
        inventoryTransactService.run(inventoryTransact);
        return ResultGenerator.genSuccessResult();
    }

}
