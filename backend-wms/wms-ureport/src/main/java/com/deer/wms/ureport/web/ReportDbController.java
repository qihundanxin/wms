package com.deer.wms.ureport.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.ureport.model.ReportDb;
import com.deer.wms.ureport.model.ReportDbCriteria;
import com.deer.wms.ureport.service.ReportDbService;
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
@RequestMapping("/report/dbs")
public class ReportDbController {

    @Autowired
    private ReportDbService reportDbService;

    @PostMapping("/add")
    @Authority("report_dbs_add")
    public Result add(@RequestBody ReportDb reportDb) {
        reportDbService.save(reportDb);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("report_dbs_delete")
    public Result delete(Integer id) {
        reportDbService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("report_dbs_update")
    public Result update(@RequestBody ReportDb reportDb) {
        reportDbService.update(reportDb);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("report_dbs_get")
    public Result detail(@PathVariable Integer id) {
        ReportDb reportDb = reportDbService.findById(id);
        return ResultGenerator.genSuccessResult(reportDb);
    }

    @GetMapping("/list")
    @Authority("report_dbs_list")
    public Result list(ReportDbCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ReportDb> list = reportDbService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
