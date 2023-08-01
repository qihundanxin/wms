package com.deer.wms.ware.task.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.ware.task.model.Load.*;
import com.deer.wms.ware.task.model.pickTask.PickTask;
import com.deer.wms.ware.task.service.LoadDetailService;
import com.deer.wms.ware.task.service.LoadMasterService;
import com.deer.wms.ware.task.service.PickTaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by ll on 2020/02/17.
 */
@RestController
@RequestMapping("/load/details")
public class LoadDetailController {

    @Autowired
    private LoadDetailService loadDetailService;
    @Autowired
    private LoadMasterService loadMasterService;
    @Autowired
    private PickTaskService pickTaskService;

    @PostMapping
    @Authority("load_details_add")
    public Result add(@RequestBody LoadDetail loadDetail) {
        loadDetailService.save(loadDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("load_details_delete")
    public Result delete(Integer id) {
        loadDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("load_details_update")
    public Result update(@RequestBody LoadDetail loadDetail) {
        loadDetailService.update(loadDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("load_details_get")
    public Result detail(@PathVariable Integer id) {
        LoadDetail loadDetail = loadDetailService.findById(id);
        return ResultGenerator.genSuccessResult(loadDetail);
    }

    @GetMapping("/list")
    @Authority("load_details_list")
    public Result list(LoadDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<LoadDetailDto> list = loadDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    public void deleteByMasterId(Integer id) {

    }

    /**
     * 装车复核
     *
     * @param insertData
     * @return
     */
    @PostMapping("/addFromPickTask")
    @Authority("load_details_addFromPickTask")
    public Result addFromPickTask(@RequestBody LoadDetailInsertData insertData) {
        Integer loadMasterId = insertData.getLoadMasterId();
        LoadMaster loadMaster = loadMasterService.findById(loadMasterId);
        for (LoadDetail loadDetail : insertData.getLoadDetails()) {
            loadDetail.setLoadMasterCode(loadMaster.getCode());
            loadDetailService.save(loadDetail);

            PickTask pickTask = pickTaskService.findById(loadDetail.getPickTaskId());
            pickTask.setReviewQuantity(pickTask.getReviewQuantity() + loadDetail.getQuantity());
            pickTaskService.update(pickTask);
        }
        return ResultGenerator.genSuccessResult();
    }
}