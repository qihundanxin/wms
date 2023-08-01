package com.deer.wms.ureport.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.ureport.model.ReportXnc;
import com.deer.wms.ureport.model.ReportXncCriteria;
import com.deer.wms.ureport.service.ReportXncService;
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
@RequestMapping("/report/xncs")
public class ReportXncController {

    @Autowired
    private ReportXncService reportXncService;

    @PostMapping("/add")
    @Authority("report_xncs_add")
    public Result add(@RequestBody ReportXnc reportXnc) {
        reportXncService.save(reportXnc);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("report_xncs_delete")
    public Result delete(Integer id) {
        reportXncService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("report_xncs_update")
    public Result update(@RequestBody ReportXnc reportXnc) {
        reportXncService.update(reportXnc);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("report_xncs_get")
    public Result detail(@PathVariable Integer id) {
        ReportXnc reportXnc = reportXncService.findById(id);
        return ResultGenerator.genSuccessResult(reportXnc);
    }

    @GetMapping("/list")
    @Authority("report_xncs_list")
    public Result list(ReportXncCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ReportXnc> list = reportXncService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
