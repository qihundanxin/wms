package com.deer.wms.inventory.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.inventory.model.ExceptionInfo;
import com.deer.wms.inventory.model.ExceptionInfoCriteria;
import com.deer.wms.inventory.service.ExceptionInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by  on 2020/10/28.
 *
 * 异常信息
 */
@RestController
@RequestMapping("/exception/infos")
public class ExceptionInfoController {

    @Autowired
    private ExceptionInfoService exceptionInfoService;

    @PostMapping("/add")
    @Authority("exception_infos_add")
    public Result add(@RequestBody ExceptionInfo exceptionInfo) {
        exceptionInfoService.save(exceptionInfo);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("exception_infos_delete")
    public Result delete(Integer id) {
        exceptionInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("exception_infos_update")
    public Result update(@RequestBody ExceptionInfo exceptionInfo) {
        exceptionInfoService.update(exceptionInfo);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("exception_infos_get")
    public Result detail(@PathVariable Integer id) {
        ExceptionInfo exceptionInfo = exceptionInfoService.findById(id);
        return ResultGenerator.genSuccessResult(exceptionInfo);
    }

    @GetMapping("/list")
    @Authority("exception_infos_list")
    public Result list(ExceptionInfoCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ExceptionInfo> list = exceptionInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
