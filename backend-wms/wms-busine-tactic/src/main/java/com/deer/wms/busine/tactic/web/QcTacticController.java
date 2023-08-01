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

import com.deer.wms.busine.tactic.model.QC.QcTactic;
import com.deer.wms.busine.tactic.model.QC.QcTacticCriteria;
import com.deer.wms.busine.tactic.service.QcTacticService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 质检规则api接口
 * 
 * Created by guo on 2020/01/15.
 */
@RestController
@RequestMapping("/qc/tactics")
public class QcTacticController {

    @Autowired
    private QcTacticService qcTacticService;

    @PostMapping("/insert")
    @Authority("qc_tactics_insert")
    public Result add(@RequestBody QcTactic qcTactic) {
        qcTacticService.save(qcTactic);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("qc_tactics_delete")
    public Result delete(Integer id) {
        qcTacticService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("qc_tactics_update")
    public Result update(@RequestBody QcTactic qcTactic) {
        qcTacticService.update(qcTactic);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("qc_tactics_detail")
    public Result detail(@PathVariable Integer id) {
        QcTactic qcTactic = qcTacticService.findById(id);
        return ResultGenerator.genSuccessResult(qcTactic);
    }

    @GetMapping("/list")
    @Authority("qc_tactics_list")
    public Result list(QcTacticCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<QcTactic> list = qcTacticService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
