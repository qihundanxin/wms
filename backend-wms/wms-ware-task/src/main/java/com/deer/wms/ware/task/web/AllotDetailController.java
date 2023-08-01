package com.deer.wms.ware.task.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.ware.task.model.Allot.AllotDetail;
import com.deer.wms.ware.task.model.Allot.AllotDetailCriteria;
import com.deer.wms.ware.task.service.AllotDetailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 调拨详细信息api接口
 * 
 * Created by ll on 2020/03/04.
 */
@RestController
@RequestMapping("/allot/details")
public class AllotDetailController {

    @Autowired
    private AllotDetailService allotDetailService;

    @PostMapping("/insert")
    @Authority("allot_details_insert")
    public Result add(@RequestBody AllotDetail allotDetail) {
        allotDetailService.save(allotDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("allot_details_delete")
    public Result delete(Integer id) {
        allotDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("allot_details_update")
    public Result update(@RequestBody AllotDetail allotDetail) {
        allotDetailService.update(allotDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("allot_details_detail")
    public Result detail(Integer id) {
        AllotDetail allotDetail = allotDetailService.findById(id);
        return ResultGenerator.genSuccessResult(allotDetail);
    }

    @GetMapping("/list")
    @Authority("allot_details_list")
    public Result list(AllotDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<AllotDetail> list = allotDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
