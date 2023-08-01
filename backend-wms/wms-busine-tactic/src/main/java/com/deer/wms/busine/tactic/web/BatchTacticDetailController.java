package com.deer.wms.busine.tactic.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.busine.tactic.model.Batch.BatchTacticDetail;
import com.deer.wms.busine.tactic.model.Batch.BatchTacticDetailCriteria;
import com.deer.wms.busine.tactic.model.Batch.BatchTacticDetailDto;
import com.deer.wms.busine.tactic.service.BatchTacticDetailService;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 批次详细信息api接口
 * 
 * Created by guo on 2019/11/29.
 */
@RestController
@RequestMapping("/batch/tactic/details")
public class BatchTacticDetailController {

    @Autowired
    private BatchTacticDetailService batchTacticDetailService;

    @PostMapping("/insert")
    @Authority("batch_tactics_details_insert")
    public Result add(@RequestBody BatchTacticDetail batchTacticDetail) {
        batchTacticDetailService.save(batchTacticDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("batch_tactics_details_delete")
    public Result delete(Integer id) {
        batchTacticDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/deleteByCode")
    @Authority("batch_tactics_details_deleteByCode")
    public Result deleteByCode(String batchTacticCode) {
        batchTacticDetailService.deleteByCode(batchTacticCode);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("batch_tactics_details_update")
    public Result update(@RequestBody BatchTacticDetail batchTacticDetail) {
        batchTacticDetailService.update(batchTacticDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("batch_tactics_details_detail")
    public Result detail(Integer id) {
        BatchTacticDetail batchTacticDetail = batchTacticDetailService.findById(id);
        return ResultGenerator.genSuccessResult(batchTacticDetail);
    }

    @Transactional
    @GetMapping("/list")
    @Authority("batch_tactics_details_list")
    public Result list(BatchTacticDetailCriteria criteria, @User CurrentUser currentUser) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<BatchTacticDetailDto> list = batchTacticDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据状态查list
     */
    @GetMapping("/StateList")
    @Authority("batch_tactics_details_StateList")
    public Result findByState(Integer state, @User CurrentUser currentUser) {
        List<BatchTacticDetailDto> list = batchTacticDetailService.findByState(state);
        return ResultGenerator.genSuccessResult(list);
    }

}
