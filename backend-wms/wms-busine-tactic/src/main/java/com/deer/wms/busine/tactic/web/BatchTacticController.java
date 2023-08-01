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

import com.deer.wms.busine.tactic.model.Batch.BatchTactic;
import com.deer.wms.busine.tactic.model.Batch.BatchTacticCriteria;
import com.deer.wms.busine.tactic.model.Batch.BatchTacticDto;
import com.deer.wms.busine.tactic.model.Batch.InsertData;
import com.deer.wms.busine.tactic.service.BatchTacticDetailDetailService;
import com.deer.wms.busine.tactic.service.BatchTacticDetailService;
import com.deer.wms.busine.tactic.service.BatchTacticService;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.constant.CacheKey;
import com.deer.wms.project.root.constant.CacheTime;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.CacheTool;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 批次信息api接口
 * 
 * Created by guo on 2019/11/29.
 */
@RestController
@RequestMapping("/batch/tactics")
public class BatchTacticController {

    @Autowired
    private BatchTacticService batchTacticService;

    @Autowired
    private BatchTacticDetailService batchTacticDetailService;

    @Autowired
    private BatchTacticDetailDetailService batchTacticDetailDetailService;

    @Autowired
    private CacheTool cache;

    /**
     * 添加批次属性信息
     */
    @PostMapping("/insert")
    @Authority("batch_tactics_insert")
    public Result add(@RequestBody InsertData insertData, @User CurrentUser currentUser) {
        batchTacticService.createBatchTactic(insertData, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("batch_tactics_delete")
    public Result delete(Integer id) {
        batchTacticService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 更新批次属性信息
     */
    @PostMapping("/update")
    @Authority("batch_tactics_update")
    public Result update(@RequestBody InsertData insertData, @User CurrentUser currentUser) {

        batchTacticService.updateBatchTactic(insertData, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("batch_tactics_detail")
    public Result detail(Integer id) {
        BatchTactic batchTactic = batchTacticService.findById(id);
        return ResultGenerator.genSuccessResult(batchTactic);
    }

    @Transactional
    @GetMapping("/list")
    @Authority("batch_tactics_list")
    public Result list(BatchTacticCriteria criteria, @User CurrentUser currentUser) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<BatchTactic> list = batchTacticService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据状态查list
     */
    @Transactional
    @GetMapping("/StateList")
    @Authority("batch_tactics_StateList")
    public Result findByState(Integer batchTacticId, Integer state, @User CurrentUser currentUser) {
        List<BatchTacticDto> list = batchTacticService.findByState(batchTacticId, state);
        return ResultGenerator.genSuccessResult(list);
    }

    /**
     * 根据货主或物料查list
     */
    @Transactional
    @GetMapping("/findByOrganizationOrItemInfo")
    @Authority("batch_tactics_findByOrganizationOrItemInfo")
    public Result findByOrganizationOrItemInfo(Integer organizationId, String itemCode, @User CurrentUser currentUser) {
        List<BatchTacticDto> list = batchTacticService.findByOrganizationOrItemInfo(organizationId, itemCode);
        if (list.size() == 0) {
            throw new ServiceException(CommonCode.BATCH_TACTIC_ERROR);
        }
        return ResultGenerator.genSuccessResult(list);
    }

    /**
     * 根据货主或物料查list
     */
    @Transactional
    @GetMapping("/findByOrganizationOrItemInfo2")
    @Authority("batch_tactics_findByOrganizationOrItemInfo2")
    public Result findByOrganizationOrItemInfo2(Integer organizationId, String itemCode, @User CurrentUser currentUser) {
        List<BatchTacticDto> list = batchTacticService.findByOrganizationOrItemInfo2(organizationId, itemCode);
        if (list == null) {
            throw new ServiceException(CommonCode.BATCH_TACTIC_ERROR);
        }
        return ResultGenerator.genSuccessResult(list);
    }

    /**
     * 根据BatchTacticCode查list
     */
    @Transactional
    @GetMapping("/findByBatchTacticCode")
    @Authority("batch_tactics_findByBatchTacticCode")
    public Result findByBatchTacticCode(String batchTacticCode, @User CurrentUser currentUser) {
        if (batchTacticCode == null || batchTacticCode.trim().equals("")) {
            batchTacticCode = "sys_batch_tactic";
        }
        BatchTacticDto list = cache.getObject(CacheKey.BATCH_TACTIC, batchTacticCode);

        if (list == null) {
            list = batchTacticService.findByBatchTacticCode(batchTacticCode);
            cache.setObject(CacheKey.BATCH_TACTIC, batchTacticCode, list, CacheTime.BATCH_TACTIC);
        }
        if (list == null) {
            list = batchTacticService.findByBatchTacticCode("sys_batch_tactic");
            cache.setObject(CacheKey.BATCH_TACTIC, "sys_batch_tactic", list, CacheTime.BATCH_TACTIC);
        }

        if (list == null) {
            throw new ServiceException(CommonCode.BATCH_TACTIC_ERROR);
        }
        return ResultGenerator.genSuccessResult(list);
    }
}
