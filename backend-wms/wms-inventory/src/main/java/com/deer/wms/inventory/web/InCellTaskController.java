package com.deer.wms.inventory.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.inventory.model.InCellTask;
import com.deer.wms.inventory.model.InCellTaskCriteria;
import com.deer.wms.inventory.service.InCellTaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by ll on 2020/03/05.
 *
 * 上架任务 TODO 暂时未增加权限标识
 */
@RestController
@RequestMapping("/in/cell/tasks")
public class InCellTaskController {

    @Autowired
    private InCellTaskService inCellTaskService;

    @PostMapping
    @Authority("in_cell_tasks_delete_add")
    public Result add(@RequestBody InCellTask inCellTask) {
        inCellTaskService.save(inCellTask);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    @Authority("in_cell_tasks_delete_delete")
    public Result delete(@PathVariable Integer id) {
        inCellTaskService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    @Authority("in_cell_tasks_update")
    public Result update(@RequestBody InCellTask inCellTask) {
        inCellTaskService.update(inCellTask);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("in_cell_tasks_get")
    public Result detail(@PathVariable Integer id) {
        InCellTask inCellTask = inCellTaskService.findById(id);
        return ResultGenerator.genSuccessResult(inCellTask);
    }

    @GetMapping
    @Authority("in_cell_tasks_list")
    public Result list(InCellTaskCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<InCellTask> list = inCellTaskService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
