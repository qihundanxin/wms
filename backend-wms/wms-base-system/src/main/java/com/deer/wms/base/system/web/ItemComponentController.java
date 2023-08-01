package com.deer.wms.base.system.web;

import com.deer.wms.base.system.model.ItemCombined.ItemComponent;
import com.deer.wms.base.system.model.ItemCombined.ItemComponentCriteria;
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
 * 产品组件
 */
@RestController
@RequestMapping("/item/components")
public class ItemComponentController {

    @Autowired
    private ItemComponentService itemComponentService;

    @PostMapping("/add")
    @Authority("item_components_add")
    public Result add(@RequestBody ItemComponent itemComponent) {
        itemComponentService.save(itemComponent);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("item_components_delete")
    public Result delete(Integer id) {
        itemComponentService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("item_components_update")
    public Result update(@RequestBody ItemComponent itemComponent) {
        itemComponentService.update(itemComponent);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("item_components_get")
    public Result detail(@PathVariable Integer id) {
        ItemComponent itemComponent = itemComponentService.findById(id);
        return ResultGenerator.genSuccessResult(itemComponent);
    }

    @PostMapping("/list")
    @Authority("item_components_list")
    public Result list(@RequestBody ItemComponentCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ItemComponent> list = itemComponentService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
