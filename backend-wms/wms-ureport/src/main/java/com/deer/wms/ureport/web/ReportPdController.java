package com.deer.wms.ureport.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.ureport.model.ReportPd;
import com.deer.wms.ureport.model.ReportPdCriteria;
import com.deer.wms.ureport.service.ReportPdService;
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
@RequestMapping("/report/pds")
public class ReportPdController {

    @Autowired
    private ReportPdService reportPdService;

    @PostMapping("/add")
    @Authority("report_pds_add")
    public Result add(@RequestBody ReportPd reportPd) {
        reportPdService.save(reportPd);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("report_pds_delete")
    public Result delete(Integer id) {
        reportPdService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("report_pds_update")
    public Result update(@RequestBody ReportPd reportPd) {
        reportPdService.update(reportPd);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("report_pds_get")
    public Result detail(@PathVariable Integer id) {
        ReportPd reportPd = reportPdService.findById(id);
        return ResultGenerator.genSuccessResult(reportPd);
    }

    @GetMapping("/list")
    @Authority("report_pds_list")
    public Result list(ReportPdCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ReportPd> list = reportPdService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
