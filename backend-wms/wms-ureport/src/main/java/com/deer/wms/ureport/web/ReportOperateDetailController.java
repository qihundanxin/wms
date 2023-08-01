package com.deer.wms.ureport.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.ureport.model.ReportOperate;
import com.deer.wms.ureport.model.ReportOperateCriteria;
import com.deer.wms.ureport.model.ReportOperateDetail;
import com.deer.wms.ureport.model.ReportOperateDetailCriteria;
import com.deer.wms.ureport.service.ReportOperateDetailService;
import com.deer.wms.ureport.service.ReportOperateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by  on 2021/01/24.
 */
@RestController
@RequestMapping("/report/operate/details")
public class ReportOperateDetailController {

    @Autowired
    private ReportOperateDetailService reportOperateDetailService;

    @PostMapping("/add")
    @Authority("report_operate_details_add")
    public Result add(@RequestBody ReportOperateDetail reportOperateDetail) {
        reportOperateDetailService.save(reportOperateDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("report_operate_details_delete")
    public Result delete(Integer id) {
        reportOperateDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("report_operate_details_update")
    public Result update(@RequestBody ReportOperateDetail reportOperateDetail) {
        reportOperateDetailService.update(reportOperateDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("report_operate_details_get")
    public Result detail(@PathVariable Integer id) {
        ReportOperateDetail reportZx = reportOperateDetailService.findById(id);
        return ResultGenerator.genSuccessResult(reportZx);
    }

    @GetMapping("/list")
    @Authority("report_operate_details_list")
    public Result list(ReportOperateDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ReportOperateDetail> list = reportOperateDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
