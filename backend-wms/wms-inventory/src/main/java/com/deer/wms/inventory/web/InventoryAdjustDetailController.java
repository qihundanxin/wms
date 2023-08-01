package com.deer.wms.inventory.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.constant.InventoryConstant;
import com.deer.wms.inventory.model.InventoryAdjustDetail;
import com.deer.wms.inventory.model.InventoryAdjustDetailCriteria;
import com.deer.wms.inventory.model.InventoryAdjustDetailDto;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.service.InventoryAdjustDetailService;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.inventory.service.InventoryTransactService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.model.BillRecordTypeConstants;
import com.deer.wms.review.manage.service.BillRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Created by ll on 2020/03/12.
 *
 * 库存调整detail表
 */
@RestController
@RequestMapping("/inventory/adjust/details")
public class InventoryAdjustDetailController {

    @Autowired
    private InventoryAdjustDetailService inventoryAdjustDetailService;


    @PostMapping
    @Authority("inventory_adjust_details_add")
    public Result add(@RequestBody InventoryAdjustDetail inventoryAdjustDetail) {
        inventoryAdjustDetailService.save(inventoryAdjustDetail);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    @Authority("inventory_adjust_details_delete")
    public Result delete(@PathVariable Integer id) {
        inventoryAdjustDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    @Authority("inventory_adjust_details_update")
    public Result update(@RequestBody InventoryAdjustDetail inventoryAdjustDetail) {
        inventoryAdjustDetailService.update(inventoryAdjustDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("inventory_adjust_details_get")
    public Result detail(@PathVariable Integer id) {
        InventoryAdjustDetail inventoryAdjustDetail = inventoryAdjustDetailService.findById(id);
        return ResultGenerator.genSuccessResult(inventoryAdjustDetail);
    }

    @GetMapping
    @Authority("inventory_adjust_details_list")
    public Result list(InventoryAdjustDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<InventoryAdjustDetail> list = inventoryAdjustDetailService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/findList")
    @Authority("inventory_adjust_details_findList")
    public Result findList(InventoryAdjustDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<InventoryAdjustDetailDto> list = inventoryAdjustDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }


    /**
     * 调整确认
     */
    @PostMapping("/adjustAffirm")
    @Transactional
    @Authority("inventory_adjust_details_adjustAffirm")
    public Result adjustAffirm(@RequestBody InventoryAdjustDetailCriteria criteria, @User CurrentUser currentUser) throws ServiceException {
        //具体实现封装在了service ,以确保事务执行
        inventoryAdjustDetailService.adjustAffirm(criteria,currentUser);
        return ResultGenerator.genSuccessResult();
    }

}
