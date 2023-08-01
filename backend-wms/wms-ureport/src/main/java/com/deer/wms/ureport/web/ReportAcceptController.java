package com.deer.wms.ureport.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.ureport.model.ReportAccept;
import com.deer.wms.ureport.model.ReportAcceptCriteria;
import com.deer.wms.ureport.service.ReportAcceptService;
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
@RequestMapping("/report/accepts")
public class ReportAcceptController {

    @Autowired
    private ReportAcceptService reportAcceptService;

    @PostMapping("/add")
    @Authority("report_accepts_add")
    public Result add(@RequestBody ReportAccept reportAccept) {
        reportAcceptService.save(reportAccept);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("report_accepts_delete")
    public Result delete(Integer id) {
        reportAcceptService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("report_accepts_update")
    public Result update(@RequestBody ReportAccept reportAccept) {
        reportAcceptService.update(reportAccept);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("report_accepts_get")
    public Result detail(@PathVariable Integer id) {
        ReportAccept reportAccept = reportAcceptService.findById(id);
        return ResultGenerator.genSuccessResult(reportAccept);
    }

    @GetMapping("/list")
    @Authority("report_accepts_list")
    public Result list(ReportAcceptCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ReportAccept> list = reportAcceptService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
