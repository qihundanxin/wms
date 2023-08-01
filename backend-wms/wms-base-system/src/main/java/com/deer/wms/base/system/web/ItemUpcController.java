package com.deer.wms.base.system.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.base.system.model.ItemUpc;
import com.deer.wms.base.system.model.ItemUpcCriteria;
import com.deer.wms.base.system.service.ItemUpcService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
* Created by  on 2021/07/08.
 *
*/
@RestController
@RequestMapping("/item/upcs")
public class ItemUpcController {

    @Autowired
    private ItemUpcService itemUpcService;

    @PostMapping("/add")
    @Authority("item_upcs_add")
    public Result add(@RequestBody ItemUpc itemUpc) {
        ItemUpc itemUpc1 = itemUpcService.findBy("upc",itemUpc.getUpc());
        if(itemUpc1!=null){
            throw new ServiceException(CommonCode.SERVICE_ERROR, "此UPC已存在");
        }
        itemUpcService.save(itemUpc);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("item_upcs_delete")
    public Result delete(  Integer id) {
        itemUpcService.delete(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("item_upcs_update")
    public Result update(@RequestBody ItemUpc itemUpc) {
        itemUpcService.update(itemUpc);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("item_upcs_get")
    public Result detail(@PathVariable Integer id) {
        ItemUpc itemUpc = itemUpcService.findById(id);
        return ResultGenerator.genSuccessResult(itemUpc);
    }

    @GetMapping("/list")
    @Authority("item_upcs_list")
    public Result list(ItemUpcCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ItemUpc> list = itemUpcService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
