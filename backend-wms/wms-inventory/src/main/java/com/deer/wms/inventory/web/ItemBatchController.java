package com.deer.wms.inventory.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.Inventory.ItemBatch;
import com.deer.wms.inventory.model.Inventory.ItemBatchCriteria;
import com.deer.wms.inventory.model.Inventory.ItemBatchDto;
import com.deer.wms.inventory.service.ItemBatchService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 物料批次信息api接口
 * 
 * Created by guo on 2019/12/02.
 */
@RestController
@RequestMapping("/item/batchs")
public class ItemBatchController {

    @Autowired
    private ItemBatchService itemBatchService;

    /**
     * 添加物料批次信息
     */
    @PostMapping("/insert")
    @Authority("item_batchs_insert")
    public Result add(@RequestBody ItemBatch itemBatch, @User CurrentUser currentUser) {
        itemBatchService.createItemBatch(itemBatch);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("item_batchs_delete")
    public Result delete(Integer id) {
        itemBatchService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("item_batchs_update")
    public Result update(@RequestBody ItemBatch itemBatch) {
        itemBatchService.update(itemBatch);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("item_batchs_detail")
    public Result detail(Integer id) {
        ItemBatch itemBatch = itemBatchService.findById(id);
        return ResultGenerator.genSuccessResult(itemBatch);
    }

    @GetMapping("/list")
    @Authority("item_batchs_list")
    public Result list(ItemBatchCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ItemBatchDto> list = itemBatchService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/findByOrganizationIdAndItemCode")
    @Authority("item_batchs_findByOrganizationIdAndItemCode")
    public Result findByOrganizationIdAndItemCode(Integer organizationId, String itemCode) {
        itemBatchService.findByOrganizationIdAndItemCode(organizationId, itemCode);
        return ResultGenerator.genSuccessResult();
    }

}
