package com.deer.wms.ware.task.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.ware.task.model.CpfrTask;
import com.deer.wms.ware.task.model.CpfrTaskCriteria;
import com.deer.wms.ware.task.service.CpfrTaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
* Created by  on 2021/08/07.
 *
 * 补货任务
*/
@RestController
@RequestMapping("/cpfr/tasks")
public class CpfrTaskController {

    @Autowired
    private CpfrTaskService cpfrTaskService;

    @PostMapping("/add")
    @Authority("cpfr_tasks_add")
    public Result add(@RequestBody CpfrTask cpfrTask) {
        cpfrTaskService.save(cpfrTask);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("cpfr_tasks_delete")
    public Result delete(  Integer id) {
        cpfrTaskService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("cpfr_tasks_update")
    public Result update(@RequestBody CpfrTask cpfrTask) {
        cpfrTaskService.update(cpfrTask);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("cpfr_tasks_get")
    public Result detail(@PathVariable Integer id) {
        CpfrTask cpfrTask = cpfrTaskService.findById(id);
        return ResultGenerator.genSuccessResult(cpfrTask);
    }

    @GetMapping("/list")
    @Authority("cpfr_tasks_list")
    public Result list(CpfrTaskCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<CpfrTask> list = cpfrTaskService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
