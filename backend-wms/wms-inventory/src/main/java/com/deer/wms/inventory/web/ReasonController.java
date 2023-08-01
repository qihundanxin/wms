package com.deer.wms.inventory.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.inventory.model.Reason;
import com.deer.wms.inventory.model.ReasonCriteria;
import com.deer.wms.inventory.service.ReasonService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by ll on 2020/02/13.
 *
 * 原因信息
 */
@RestController
@RequestMapping("/reasons")
public class ReasonController {

    @Autowired
    private ReasonService reasonService;

    @PostMapping("/insert")
    @Authority("reasons_insert")
    public Result add(@RequestBody Reason reason) {
        reasonService.save(reason);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("reasons_delete")
    public Result delete(Integer id) {
        reasonService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("reasons_update")
    public Result update(@RequestBody Reason reason) {
        reasonService.update(reason);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("reasons_get")
    public Result detail(@PathVariable Integer id) {
        Reason reason = reasonService.findById(id);
        return ResultGenerator.genSuccessResult(reason);
    }

    @GetMapping("/list")
    @Authority("reasons_list")
    public Result list(ReasonCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<Reason> list = reasonService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
