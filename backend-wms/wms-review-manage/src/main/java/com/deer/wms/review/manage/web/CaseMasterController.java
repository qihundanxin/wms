package com.deer.wms.review.manage.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.review.manage.model.CaseMaster;
import com.deer.wms.review.manage.model.CaseMasterCriteria;
import com.deer.wms.review.manage.model.CaseMasterDto;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 审核内容信息api接口
 * 
 * Created by guo on 2018/10/24.
 */
@RestController
@RequestMapping("/case/masters")
public class CaseMasterController {

    @Autowired
    private CaseMasterService caseMasterService;

    @PostMapping("/add")
    @Authority("case_masters_add")
    public Result add(@RequestBody CaseMaster caseMaster) {
        caseMasterService.save(caseMaster);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("case_masters_delete")
    public Result delete(Integer caseMasterId) {
        caseMasterService.deleteById(caseMasterId);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("case_masters_update")
    public Result update(@RequestBody CaseMaster caseMaster) {
        caseMasterService.update(caseMaster);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("case_masters_get")
    public Result detail(@PathVariable Integer id) {
        CaseMaster caseMaster = caseMasterService.findById(id);
        return ResultGenerator.genSuccessResult(caseMaster);
    }

    @GetMapping("/list")
    @Authority("case_masters_list")
    public Result list(CaseMasterCriteria criteria, @User CurrentUser currentUser) {

        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<CaseMasterDto> list = caseMasterService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
