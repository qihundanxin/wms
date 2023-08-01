package com.deer.wms.busine.tactic.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.busine.tactic.model.QC.QcTacticDetail;
import com.deer.wms.busine.tactic.model.QC.QcTacticDetailCriteria;
import com.deer.wms.busine.tactic.service.QcTacticDetailService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 质检规则详情api接口
 * 
 * Created by guo on 2020/01/15.
 */
@RestController
@RequestMapping("/qc/tactic/details")
public class QcTacticDetailController {

    @Autowired
    private QcTacticDetailService qcTacticDetailService;

    @PostMapping("/insert")
    @Authority("qc_tactic_details_insert")
    public Result add(@RequestBody QcTacticDetail qcTacticDetail) {
        qcTacticDetailService.save(qcTacticDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("qc_tactic_details_delete")
    public Result delete(Integer id) {
        qcTacticDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("qc_tactic_details_update")
    public Result update(@RequestBody QcTacticDetail qcTacticDetail) {
        qcTacticDetailService.update(qcTacticDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("qc_tactic_details_detail")
    public Result detail(@PathVariable Integer id) {
        QcTacticDetail qcTacticDetail = qcTacticDetailService.findById(id);
        return ResultGenerator.genSuccessResult(qcTacticDetail);
    }

    @GetMapping("/list")
    @Authority("qc_tactic_details_list")
    public Result list(QcTacticDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<QcTacticDetail> list = qcTacticDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
