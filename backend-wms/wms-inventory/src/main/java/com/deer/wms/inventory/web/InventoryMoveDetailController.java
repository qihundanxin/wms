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

import com.deer.wms.inventory.model.move.InventoryMoveDetail;
import com.deer.wms.inventory.model.move.InventoryMoveDetailCriteria;
import com.deer.wms.inventory.model.move.InventoryMoveDetailDto;
import com.deer.wms.inventory.service.InventoryMoveDetailService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

/**
 * 移库detail api接口
 * 
 * Created by guo on 2019/12/27.
 */
@RestController
@RequestMapping("/inventory/move/details")
public class InventoryMoveDetailController {

    @Autowired
    private InventoryMoveDetailService inventoryMoveDetailService;

    @PostMapping("/insert")
    @Authority("inventory_moves_details_insert")
    public Result add(@RequestBody InventoryMoveDetail inventoryMoveDetail) {
        inventoryMoveDetailService.save(inventoryMoveDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("inventory_moves_details_delete")
    public Result delete(@PathVariable Integer id) {
        inventoryMoveDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("inventory_moves_details_update")
    public Result update(@RequestBody InventoryMoveDetail inventoryMoveDetail) {
        inventoryMoveDetailService.update(inventoryMoveDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("inventory_moves_details_detail")
    public Result detail(@PathVariable Integer id) {
        InventoryMoveDetail inventoryMoveDetail = inventoryMoveDetailService.findById(id);
        return ResultGenerator.genSuccessResult(inventoryMoveDetail);
    }

    @GetMapping("/list")
    @Authority("inventory_moves_details_list")
    public Result list(InventoryMoveDetailCriteria criteria) {
        List<InventoryMoveDetailDto> list = inventoryMoveDetailService.findList(criteria);
        return ResultGenerator.genSuccessResult(list);
    }

}
