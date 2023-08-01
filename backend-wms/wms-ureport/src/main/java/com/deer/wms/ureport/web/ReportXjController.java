package com.deer.wms.ureport.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.ureport.model.ReportXj;
import com.deer.wms.ureport.model.ReportXjCriteria;
import com.deer.wms.ureport.service.ReportXjService;
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
@RequestMapping("/report/xjs")
public class ReportXjController {

    @Autowired
    private ReportXjService reportXjService;

    @PostMapping("/add")
    @Authority("report_xjs_add")
    public Result add(@RequestBody ReportXj reportXj) {
        reportXjService.save(reportXj);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("report_xjs_delete")
    public Result delete(Integer id) {
        reportXjService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("report_xjs_update")
    public Result update(@RequestBody ReportXj reportXj) {
        reportXjService.update(reportXj);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("report_xjs_get")
    public Result detail(@PathVariable Integer id) {
        ReportXj reportXj = reportXjService.findById(id);
        return ResultGenerator.genSuccessResult(reportXj);
    }

    @GetMapping("/list")
    @Authority("report_xjs_list")
    public Result list(ReportXjCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ReportXj> list = reportXjService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
