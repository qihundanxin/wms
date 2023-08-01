package com.deer.wms.inventory.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.inventory.model.freeze.FreezeDetail;
import com.deer.wms.inventory.model.freeze.FreezeDetailCriteria;
import com.deer.wms.inventory.model.freeze.FreezeDetailDto;
import com.deer.wms.inventory.service.FreezeDetailService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 冻结Detail api接口
 * 
 * Created by guo on 2019/12/25.
 */
@RestController
@RequestMapping("/freeze/details")
public class FreezeDetailController {

    @Autowired
    private FreezeDetailService freezeDetailService;

    @PostMapping("/insert")
    @Authority("freeze_details_insert")
    public Result add(@RequestBody FreezeDetail freezeDetail) {
        freezeDetailService.save(freezeDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("freeze_details_delete")
    public Result delete(Integer id) {
        freezeDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("freeze_details_update")
    public Result update(@RequestBody FreezeDetail freezeDetail) {
        freezeDetailService.update(freezeDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("freeze_details_detail")
    public Result detail(Integer id) {
        FreezeDetail freezeDetail = freezeDetailService.findById(id);
        return ResultGenerator.genSuccessResult(freezeDetail);
    }

    @GetMapping("/list")
    @Authority("freeze_details_list")
    public Result list(FreezeDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<FreezeDetailDto> list = freezeDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/findListAndMaster")
    @Authority("freeze_details_findListAndMaster")
    public Result findListAndMaster(FreezeDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<FreezeDetailDto> list = freezeDetailService.findListAndMaster(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
