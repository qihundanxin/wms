package com.deer.wms.ware.task.web;

import java.util.List;

import javax.annotation.Resource;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.InventoryAdjustDetail;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.service.InventoryAdjustDetailService;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.ware.task.model.PickTaskEx;
import com.deer.wms.ware.task.model.PickTaskExCriteria;
import com.deer.wms.ware.task.service.PickTaskExService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Created by on 2021/03/20.
 *
 * 拣货异常任务
 */
@RestController
@RequestMapping("/pick/task/exs")
public class PickTaskExController {

    @Autowired
    private PickTaskExService pickTaskExService;

    @Resource
    private InventoryAdjustDetailService inventoryAdjustDetailService;

    @PostMapping("/add")
    @Authority("pick_task_exs_add")
    public Result add(@RequestBody PickTaskEx pickTaskEx) {
        pickTaskExService.save(pickTaskEx);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("pick_task_exs_delete")
    public Result delete(Integer id) {
        pickTaskExService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("pick_task_exs_update")
    public Result update(@RequestBody PickTaskEx pickTaskEx) {
        pickTaskExService.update(pickTaskEx);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("pick_task_exs_get")
    public Result detail(@PathVariable Integer id) {
        PickTaskEx pickTaskEx = pickTaskExService.findById(id);
        return ResultGenerator.genSuccessResult(pickTaskEx);
    }

    @GetMapping("/list")
    @Authority("pick_task_exs_list")
    public Result list(PickTaskExCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<PickTaskEx> list = pickTaskExService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/commit")
    @Transactional
    @Authority("pick_task_exs_commit")
    public Result commit(Integer id, Double commitQuantity, @User CurrentUser currentUser) {
        PickTaskEx pickTaskEx = pickTaskExService.findById(id);
        pickTaskEx.setState(1);
        pickTaskEx.setCommitQuantity(commitQuantity);
        pickTaskEx.setCommitUserId(currentUser.getUserId());
        pickTaskEx.setCommitUserName(currentUser.getUserName());
        pickTaskEx.setCommitTime(DateUtils.getNowDateTimeString());
        pickTaskExService.update(pickTaskEx);
        Inventory inventory = inventoryService.findById(pickTaskEx.getInventoryId());
        if (null == inventory) {
            throw new ServiceException(CommonCode.INVENTORY_NULL);
        }
        Double beforeQuantity = inventory.getQuantity();
        inventory.setFreezeQuantity(inventory.getFreezeQuantity() - pickTaskEx.getFreezeQuantity());
        if (inventory.getFreezeQuantity() < 0.0d) {
            inventory.setFreezeQuantity(0.0d);
        }
        inventory.setQuantity(commitQuantity);
        inventoryService.update(inventory);
        // 生成库存调整记录
        String now = DateUtils.getNowDateString();
        InventoryAdjustDetail adjustDetail = new InventoryAdjustDetail();
        adjustDetail.setQuantity(beforeQuantity);
        adjustDetail.setBillNo("IAEX-" + pickTaskEx.getPickTaskExId());
        adjustDetail.setDetailNo("IAEX-" + pickTaskEx.getPickTaskExId() + "1");
        adjustDetail.setInventoryId(pickTaskEx.getInventoryId());
        adjustDetail.setItemCode(pickTaskEx.getItemCode());
        adjustDetail.setItemName(pickTaskEx.getItemName());
        adjustDetail.setCellCode(pickTaskEx.getCellCode());
        adjustDetail.setBoxCode(pickTaskEx.getBoxCode());
        adjustDetail.setDifferQuantity(commitQuantity - beforeQuantity);
        adjustDetail.setAdjustQuantity(commitQuantity);
        adjustDetail.setState(2);
        adjustDetail.setCreateTime(now);
        adjustDetail.setCreateUseId(currentUser.getUserId());
        adjustDetail.setCreateUseName(currentUser.getUserName());
        adjustDetail.setMemo("pickTaskEx");
        adjustDetail.setAdjustTime(now);
        adjustDetail.setAdjustUserId(currentUser.getUserId());
        adjustDetail.setAdjustUserName(currentUser.getUserName());

        inventoryAdjustDetailService.save(adjustDetail);
        return ResultGenerator.genSuccessResult();
    }

}
