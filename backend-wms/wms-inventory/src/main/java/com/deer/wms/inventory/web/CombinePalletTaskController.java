package com.deer.wms.inventory.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.combinePallet.CombinePalletTask;
import com.deer.wms.inventory.model.combinePallet.CombinePalletTaskCriteria;
import com.deer.wms.inventory.model.combinePallet.CombinePalletTaskDetail;
import com.deer.wms.inventory.model.combinePallet.CombinePalletTaskInsertData;
import com.deer.wms.inventory.service.CombinePalletTaskDetailService;
import com.deer.wms.inventory.service.CombinePalletTaskService;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.BillNoUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Created by  on 2020/06/14.
 *
 * 合托任务
 */
@RestController
@RequestMapping("/combine/pallet/tasks")
public class CombinePalletTaskController {

    @Autowired
    private CombinePalletTaskService combinePalletTaskService;
    @Autowired
    private CombinePalletTaskDetailService combinePalletTaskDetailService;
    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/insert")
    @Authority("combine_pallet_tasks_insert")
    public Result add(@RequestBody CombinePalletTaskInsertData insertData, @User CurrentUser currentUser) {
//        if (currentUser == null) {
//            throw new ServiceException(CommonCode.MISSING_PERMISSION_PARAMTER);
//        }
        List<CombinePalletTaskDetail> details = insertData.getDetails();
        if (details.size() > 0) {
            CombinePalletTask task = new CombinePalletTask();
            task.setTaskNo("CPT" + BillNoUtils.generateCPTNo());
            task.setCreateTime(DateUtils.getNowDateTimeString());
            combinePalletTaskService.save(task);

            for (CombinePalletTaskDetail detail : details) {
                detail.setTaskNo(task.getTaskNo());
                detail.setState(0);
                combinePalletTaskDetailService.save(detail);
            }
        } else {
            throw new ServiceException(CommonCode.NEED_DETAILS);
        }
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("combine_pallet_tasks_delete")
    public Result delete(Integer id) {
        combinePalletTaskService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("combine_pallet_tasks_update")
    public Result update(@RequestBody CombinePalletTask combinePalletTask) {
        combinePalletTaskService.update(combinePalletTask);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("combine_pallet_tasks_get")
    public Result detail(@PathVariable Integer id) {
        CombinePalletTask combinePalletTask = combinePalletTaskService.findById(id);
        return ResultGenerator.genSuccessResult(combinePalletTask);
    }

    @GetMapping("/list")
    @Authority("combine_pallet_tasks_list")
    public Result list(CombinePalletTaskCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<CombinePalletTask> list = combinePalletTaskService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/getTaskByBoxCode")
    @Authority("combine_pallet_tasks_getTaskByBoxCode")
    public Result list(String boxCode) {
        List<CombinePalletTaskDetail> details = combinePalletTaskDetailService.detailsWithBoxCode(boxCode);
        PageInfo pageInfo = new PageInfo(details);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
