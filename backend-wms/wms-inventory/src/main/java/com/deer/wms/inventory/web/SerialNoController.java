package com.deer.wms.inventory.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.inventory.model.SerialNo;
import com.deer.wms.inventory.model.SerialNoCriteria;
import com.deer.wms.inventory.model.SerialNoDto;
import com.deer.wms.inventory.service.SerialNoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by ll on 2020/02/15.
 *
 * 序列号信息
 */
@RestController
@RequestMapping("/serial/nos")
public class SerialNoController {

    @Autowired
    private SerialNoService serialNoService;

    @PostMapping
    @Authority("serial_nos_add")
    public Result add(@RequestBody SerialNo serialNo) {
        serialNoService.save(serialNo);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    @Authority("serial_nos_delete")
    public Result delete(@PathVariable Integer id) {
        serialNoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    @Authority("serial_nos_update")
    public Result update(@RequestBody SerialNo serialNo) {
        serialNoService.update(serialNo);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("serial_nos_get")
    public Result detail(@PathVariable Integer id) {
        SerialNo serialNo = serialNoService.findById(id);
        return ResultGenerator.genSuccessResult(serialNo);
    }

    @GetMapping("/list")
    @Authority("serial_nos_list")
    public Result list(SerialNoCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SerialNoDto> list = serialNoService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/findInventoryList")
    @Authority("serial_nos_findInventoryList")
    public Result findInventoryList(SerialNoCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SerialNoDto> list = serialNoService.findInventoryList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
