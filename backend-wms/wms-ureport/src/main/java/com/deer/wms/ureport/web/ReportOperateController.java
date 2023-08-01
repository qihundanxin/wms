package com.deer.wms.ureport.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.ureport.model.ReportOperate;
import com.deer.wms.ureport.model.ReportOperateCriteria;
import com.deer.wms.ureport.service.ReportOperateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by  on 2021/01/24.
 */
@RestController
@RequestMapping("/report/operates")
public class ReportOperateController {

    @Autowired
    private ReportOperateService reportOperateService;

    @PostMapping("/add")
    @Authority("report_operates_add")
    public Result add(@RequestBody ReportOperate reportOperate) {
        reportOperateService.save(reportOperate);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("report_operates_delete")
    public Result delete(Integer id) {
        reportOperateService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("report_operates_update")
    public Result update(@RequestBody ReportOperate reportZx) {
        reportOperateService.update(reportZx);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("report_operates_get")
    public Result detail(@PathVariable Integer id) {
        ReportOperate reportZx = reportOperateService.findById(id);
        return ResultGenerator.genSuccessResult(reportZx);
    }

    @GetMapping("/list")
    @Authority("report_operates_list")
    public Result list(ReportOperateCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ReportOperate> list = reportOperateService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
