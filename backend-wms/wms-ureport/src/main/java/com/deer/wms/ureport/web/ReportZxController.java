package com.deer.wms.ureport.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.ureport.model.ReportZx;
import com.deer.wms.ureport.model.ReportZxCriteria;
import com.deer.wms.ureport.service.ReportZxService;
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
@RequestMapping("/report/zxs")
public class ReportZxController {

    @Autowired
    private ReportZxService reportZxService;

    @PostMapping("/add")
    @Authority("report_zxs_add")
    public Result add(@RequestBody ReportZx reportZx) {
        reportZxService.save(reportZx);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("report_zxs_delete")
    public Result delete(Integer id) {
        reportZxService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("report_zxs_update")
    public Result update(@RequestBody ReportZx reportZx) {
        reportZxService.update(reportZx);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("report_zxs_get")
    public Result detail(@PathVariable Integer id) {
        ReportZx reportZx = reportZxService.findById(id);
        return ResultGenerator.genSuccessResult(reportZx);
    }

    @GetMapping("/list")
    @Authority("report_zxs_list")
    public Result list(ReportZxCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ReportZx> list = reportZxService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
