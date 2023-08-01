package com.deer.wms.inventory.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.inventory.model.combinePallet.CombinePalletTaskDetail;
import com.deer.wms.inventory.model.combinePallet.CombinePalletTaskDetailCriteria;
import com.deer.wms.inventory.service.CombinePalletTaskDetailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by  on 2020/06/14.
 *
 * 合托任务明细
 */
@RestController
@RequestMapping("/combine/pallet/task/details")
public class CombinePalletTaskDetailController {

    @Autowired
    private CombinePalletTaskDetailService combinePalletTaskDetailService;

    @PostMapping("/add")
    @Authority("combine_pallet_task_details_add")
    public Result add(@RequestBody CombinePalletTaskDetail combinePalletTaskDetail) {
        combinePalletTaskDetailService.save(combinePalletTaskDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("combine_pallet_task_details_delete")
    public Result delete(Integer id) {
        combinePalletTaskDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("combine_pallet_task_details_update")
    public Result update(@RequestBody CombinePalletTaskDetail combinePalletTaskDetail) {
        combinePalletTaskDetailService.update(combinePalletTaskDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("combine_pallet_task_details_get")
    public Result detail(@PathVariable Integer id) {
        CombinePalletTaskDetail combinePalletTaskDetail = combinePalletTaskDetailService.findById(id);
        return ResultGenerator.genSuccessResult(combinePalletTaskDetail);
    }

    @GetMapping("/list")
    @Authority("combine_pallet_task_details_list")
    public Result list(CombinePalletTaskDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<CombinePalletTaskDetail> list = combinePalletTaskDetailService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
