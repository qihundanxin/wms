package com.deer.wms.ureport.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.ureport.model.ReportSj;
import com.deer.wms.ureport.model.ReportSjCriteria;
import com.deer.wms.ureport.service.ReportSjService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by  on 2021/01/24.
 *
 * TODO 暂时未增加权限标识
 */
@RestController
@RequestMapping("/report/sjs")
public class ReportSjController {

    @Autowired
    private ReportSjService reportSjService;

    @PostMapping("/add")
    @Authority("report_sjs_add")
    public Result add(@RequestBody ReportSj reportSj) {
        reportSjService.save(reportSj);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("report_sjs_delete")
    public Result delete(Integer id) {
        reportSjService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("report_sjs_update")
    public Result update(@RequestBody ReportSj reportSj) {
        reportSjService.update(reportSj);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("report_sjs_get")
    public Result detail(@PathVariable Integer id) {
        ReportSj reportSj = reportSjService.findById(id);
        return ResultGenerator.genSuccessResult(reportSj);
    }

    @GetMapping("/list")
    @Authority("report_sjs_list")
    public Result list(ReportSjCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ReportSj> list = reportSjService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
