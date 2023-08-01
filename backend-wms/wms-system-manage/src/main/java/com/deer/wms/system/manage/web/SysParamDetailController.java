package com.deer.wms.system.manage.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.util.RedisUtil;
import com.deer.wms.system.manage.model.SysParamDetail;
import com.deer.wms.system.manage.model.SysParamDetailCriteria;
import com.deer.wms.system.manage.service.SysParamDetailService;
import com.deer.wms.system.manage.service.SysParamService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 系统参数详细信息api接口
 * 
 * Created by guo on 2019/07/12.
 */
@RestController
@RequestMapping("/sys/param/details")
public class SysParamDetailController {

    @Autowired
    private SysParamDetailService sysParamDetailService;

    @Autowired
    private SysParamService sysParamService;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/save")
    @Authority("sys_params_details_save")
    public Result add(@RequestBody SysParamDetail sysParamDetail) {
        sysParamDetailService.save(sysParamDetail);
        sysParamService.reSetRedis();
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("sys_params_details_delete")
    public Result delete(Integer id) {
        sysParamDetailService.deleteById(id);
        sysParamService.reSetRedis();
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("sys_params_details_update")
    public Result update(@RequestBody SysParamDetail sysParamDetail) {
        sysParamDetailService.update(sysParamDetail);
        sysParamService.reSetRedis();
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("sys_params_details_get")
    public Result detail(@PathVariable Integer id) {
        SysParamDetail sysParamDetail = sysParamDetailService.findById(id);
        //sysParamService.reSetRedis();
        return ResultGenerator.genSuccessResult(sysParamDetail);
    }

    @GetMapping("/list")
    @Authority("sys_params_details_list")
    public Result list(SysParamDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SysParamDetail> list = sysParamDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        //sysParamService.reSetRedis();
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
