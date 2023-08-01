package com.deer.wms.base.system.web;

import com.deer.wms.base.system.model.ItemCombined.ItemCombined;
import com.deer.wms.base.system.model.ItemCombined.ItemCombinedCriteria;
import com.deer.wms.base.system.model.ItemCombined.ItemCombinedInsert;
import com.deer.wms.base.system.model.ItemCombined.ItemComponent;
import com.deer.wms.base.system.service.ItemCombinedService;
import com.deer.wms.base.system.service.ItemComponentService;
import com.deer.wms.intercept.annotation.Authority;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by ll on 2020/02/14.
 *
 * 产品组合
 */
@RestController
@RequestMapping("/item/combined")
public class ItemCombinedController {

    @Autowired
    private ItemCombinedService itemCombinedService;
    @Autowired
    private ItemComponentService itemComponentService;

    @PostMapping("/add")
    @Authority("item_combined_add")
    public Result add(@RequestBody ItemCombined itemCombined) {
        itemCombinedService.save(itemCombined);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("item_combined_delete")
    public Result delete(Integer id) {
        ItemCombined itemCombined = itemCombinedService.findByCombinedId(id);
        itemComponentService.deleteByCombinedCode(itemCombined.getItemCode());
        itemCombinedService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("item_combined_update")
    public Result update(@RequestBody ItemCombined itemCombined) {
        itemCombinedService.update(itemCombined);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("item_combined_get")
    public Result detail(@PathVariable Integer id) {
        ItemCombined itemCombined = itemCombinedService.findById(id);
        return ResultGenerator.genSuccessResult(itemCombined);
    }

    @PostMapping("/list")
    @Authority("item_combined_list")
    public Result list(@RequestBody ItemCombinedCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ItemCombined> list = itemCombinedService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/insert")
    @Authority("item_combined_insert")
    public Result insert(@RequestBody ItemCombinedInsert insert) {
        itemCombinedService.save(insert.getItemCombined());
        for (ItemComponent component : insert.getItemComponents()) {
            itemComponentService.save(component);
        }
        return ResultGenerator.genSuccessResult();
    }
}
