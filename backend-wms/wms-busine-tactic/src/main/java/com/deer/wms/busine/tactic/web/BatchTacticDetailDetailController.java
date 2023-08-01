package com.deer.wms.busine.tactic.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.busine.tactic.model.Batch.BatchTacticDetailDetail;
import com.deer.wms.busine.tactic.model.Batch.BatchTacticDetailDetailCriteria;
import com.deer.wms.busine.tactic.service.BatchTacticDetailDetailService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 批次格式选择信息api接口
 * 
 * Created by guo on 2019/11/29.
 */
@RestController
@RequestMapping("/batch/tactic/detail/details")
public class BatchTacticDetailDetailController {

    @Autowired
    private BatchTacticDetailDetailService batchTacticDetailDetailService;

    @PostMapping("/insert")
    @Authority("batch_tactics_detail_details_insert")
    public Result add(@RequestBody BatchTacticDetailDetail batchTacticDetailDetail) {
        batchTacticDetailDetailService.save(batchTacticDetailDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("batch_tactics_detail_details_delete")
    public Result delete(Integer id) {
        batchTacticDetailDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("batch_tactics_detail_details_update")
    public Result update(@RequestBody BatchTacticDetailDetail batchTacticDetailDetail) {
        batchTacticDetailDetailService.update(batchTacticDetailDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("batch_tactics_detail_details_detail")
    public Result detail(Integer id) {
        BatchTacticDetailDetail batchTacticDetailDetail = batchTacticDetailDetailService.findById(id);
        return ResultGenerator.genSuccessResult(batchTacticDetailDetail);
    }

    @GetMapping("/list")
    @Authority("batch_tactics_detail_details_list")
    public Result list(BatchTacticDetailDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<BatchTacticDetailDetail> list = batchTacticDetailDetailService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
