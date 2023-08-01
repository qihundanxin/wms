package com.deer.wms.ware.task.web;

import com.deer.wms.base.system.model.Cell.CellInfo;
import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.Inventory.InventoryCriteria;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.ware.task.dao.PickTaskMapper;
import com.deer.wms.ware.task.dao.PickTaskReturnMapper;
import com.deer.wms.ware.task.model.PickTaskReturn;
import com.deer.wms.ware.task.model.PickTaskReturnCriteria;
import com.deer.wms.ware.task.model.PickTaskReturnDto;
import com.deer.wms.ware.task.model.SO.SoDetail;
import com.deer.wms.ware.task.model.SO.SoMaster;
import com.deer.wms.ware.task.model.Wave.WaveDetail;
import com.deer.wms.ware.task.model.Wave.WaveMaster;
import com.deer.wms.ware.task.model.pickTask.PickTask;
import com.deer.wms.ware.task.model.pickTask.PickTaskDto;
import com.deer.wms.ware.task.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by  on 2021/03/20.
 *
 * 还货上架
 */
@RestController
@RequestMapping("/pick/task/returns")
public class PickTaskReturnController {

    @Autowired
    private PickTaskReturnService pickTaskReturnService;

    @PostMapping("/add")
    @Authority("pick_task_returns_add")
    public Result add(@RequestBody PickTaskReturn pickTaskReturn) {
        pickTaskReturnService.save(pickTaskReturn);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("pick_task_returns_delete")
    public Result delete(Integer id) {
        pickTaskReturnService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("pick_task_returns_update")
    public Result update(@RequestBody PickTaskReturn pickTaskReturn) {
        pickTaskReturnService.update(pickTaskReturn);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("pick_task_returns_get")
    public Result detail(@PathVariable Integer id) {
        PickTaskReturn pickTaskReturn = pickTaskReturnService.findById(id);
        return ResultGenerator.genSuccessResult(pickTaskReturn);
    }

    @GetMapping("/list")
    @Authority("pick_task_returns_list")
    public Result list(PickTaskReturnCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<PickTaskReturn> list = pickTaskReturnService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/findList")
    @Authority("pick_task_returns_findList")
    public Result findList(PickTaskReturnCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<PickTaskReturn> list = pickTaskReturnService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Autowired
    private WaveMasterService waveMasterService;
    @Autowired
    private PickTaskMapper pickTaskMapper;

    //因为多件或者错捡，要在俄日此分拣台这里进行创建还货任务
    @GetMapping("/create")
    @Transactional
    @Authority("pick_task_returns_create")
    public Result create(Integer waveMasterId, String boxCode, String itemCode, Integer wareId, Double quantity, @User CurrentUser currentUser) {

        WaveMaster waveMaster = waveMasterService.findById(waveMasterId);
        PickTask pickTask = pickTaskMapper.findByWaveIdAndItemCode(waveMasterId, itemCode);
        PickTaskReturn pickTaskReturn = new PickTaskReturn();
        pickTaskReturn.setQuantity(quantity);
        //不需要改变库存
        pickTaskReturn.setType(3);
        pickTaskReturn.setItemCode(itemCode);
        pickTaskReturn.setItemName(pickTask.getItemName());
        pickTaskReturn.setImgUrl(pickTask.getImgUrl());
        //  pickTaskReturn.setFromInventoryId(pickTask.getInventoryId());
        pickTaskReturn.setCreateTime(DateUtils.getNowDateTimeString());
        pickTaskReturn.setCreateUserId(currentUser.getUserId());
        pickTaskReturn.setCreateUserName(currentUser.getUserName());
        pickTaskReturn.setWaveMasterId(waveMaster.getWaveId());
        pickTaskReturn.setBoxCode(waveMaster.getBoxCode());
        pickTaskReturn.setOrgId(pickTask.getOrganizationId());
        pickTaskReturn.setWareId(pickTask.getWareId());
        pickTaskReturn.setToCellCode(pickTask.getCellCode());
        pickTaskReturn.setFromCellCode(pickTask.getToCellCode());
        pickTaskReturnService.save(pickTaskReturn);
        return ResultGenerator.genSuccessResult(pickTaskReturn);
    }

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private CellInfoService cellInfoService;
    @Autowired
    private PickTaskReturnMapper pickTaskReturnMapper;

    //上架
    @GetMapping("/up")
    @Transactional
    @Authority("pick_task_returns_up")
    public Result up(String commitCellCode, Integer pickTaskReturnId, Integer wareId, @User CurrentUser currentUser) {

        CellInfo cellInfo = cellInfoService.findByCodeAndWareId(commitCellCode, wareId);
        if (cellInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "货位无效！");
        }
        if (cellInfo.getCellUseType() != 1) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "目标位置必须是拣货位！");
        }

        PickTaskReturn pickTaskReturn = pickTaskReturnMapper.findById(pickTaskReturnId);
        pickTaskReturn.setState(1);
        pickTaskReturn.setCommitCellCode(commitCellCode);
        pickTaskReturn.setCommitUserId(currentUser.getUserId());
        pickTaskReturn.setCommitUserName(currentUser.getUserName());
        pickTaskReturn.setCommitTime(DateUtils.getNowDateTimeString());
        pickTaskReturnMapper.update(pickTaskReturn);
        if(pickTaskReturn.getType() == 3){
            return ResultGenerator.genSuccessResult(pickTaskReturn);
        }

        String itemCode = pickTaskReturn.getItemCode();

        Integer orgId = pickTaskReturn.getOrgId();
        String itemName = pickTaskReturn.getItemName();
        String imgUrl = pickTaskReturn.getImgUrl();
        Double quantity = pickTaskReturn.getQuantity();

        if (pickTaskReturn.getFromInventoryId() != null) {
            Inventory inventory = inventoryService.findById(pickTaskReturn.getFromInventoryId());
            if (inventory != null) {
                inventory.setQuantity(inventory.getQuantity() - quantity);
                inventoryService.update(inventory);
//                if (inventory.getQuantity() < 0.1) {
////                    inventoryService.deleteByIdVersion(inventory.getInventoryId(), inventory.getVersion());
//                } else {
//                    inventoryService.update(inventory);
//                }
            }

            Inventory oldInventory = inventoryService.findByItemWareOrg(itemCode, pickTaskReturn.getWareId(), orgId, commitCellCode);
            if (oldInventory != null) {
                oldInventory.setQuantity(oldInventory.getQuantity() + quantity);
                // version
                inventoryService.update(oldInventory);

            } else {
                oldInventory = new Inventory();
                //BeanUtils.copyBeanProp(oldInventory,inventory);
                oldInventory.setItemCode(itemCode);
                oldInventory.setItemName(itemName);
                oldInventory.setOrganizationId(pickTaskReturn.getOrgId());
                oldInventory.setWareId(pickTaskReturn.getWareId());
                oldInventory.setImgUrl(imgUrl);
                oldInventory.setTransRatio(1.0);
                oldInventory.setQuantity(quantity);
                oldInventory.setCellCode(commitCellCode);
                oldInventory.setCellUseType(cellInfo.getCellUseType());
                inventoryService.save(oldInventory);

            }

        }
        //只有已发运走再退回的才会走这个流程
        else {
            Inventory oldInventory = inventoryService.findByItemWareOrg(pickTaskReturn.getItemCode(), pickTaskReturn.getWareId(), pickTaskReturn.getOrgId(), commitCellCode);
            if (oldInventory != null) {
                oldInventory.setQuantity(oldInventory.getQuantity() + quantity);
                // version
                inventoryService.update(oldInventory);

            } else {
                oldInventory = new Inventory();
                BeanUtils.copyBeanProp(oldInventory, pickTaskReturn);
                oldInventory.setOrganizationId(pickTaskReturn.getOrgId());
                oldInventory.setInventoryId(null);
                oldInventory.setQuantity(quantity);
                oldInventory.setCellCode(commitCellCode);
                oldInventory.setCellUseType(cellInfo.getCellUseType());
                inventoryService.save(oldInventory);

            }

        }

        return ResultGenerator.genSuccessResult(pickTaskReturn);
    }

    @Autowired
    private SoMasterService soMasterService;
    @Autowired
    private SoDetailService soDetailService;
    @Autowired
    private WaveDetailService waveDetailService;
    @Autowired
    private PickTaskService pickTaskService;

    //因为少拣错捡等原因造成的订单无法播种的订单进行取消
    @GetMapping("/cancelSo")
    @Authority("pick_task_returns_cancelSo")
    public Result cancelSo(Integer waveMasterId, Integer soMasterId, Integer wareId, @User CurrentUser currentUser) {

        WaveMaster waveMaster = waveMasterService.findById(waveMasterId);
        SoMaster soMaster = soMasterService.findById(soMasterId);
        if (soMaster.getState() != 6 && soMaster.getState() != 7) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "拣货完成或复核订单,不可取消");
        }
        if (!soMaster.getWareId().equals(wareId)) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "仓库异常");
        }
        List<PickTask> list = pickTaskMapper.findBySoMasterId(soMasterId);
        for (PickTask pickTask : list) {
            pickTask.setState(-1);
            pickTaskService.update(pickTask);
            PickTaskReturn pickTaskReturn = new PickTaskReturn();
            pickTaskReturn.setQuantity(pickTask.getPickQuantity());
            pickTaskReturn.setType(1);
            pickTaskReturn.setItemCode(pickTask.getItemCode());
            pickTaskReturn.setItemName(pickTask.getItemName());
            pickTaskReturn.setImgUrl(pickTask.getImgUrl());
            pickTaskReturn.setCreateTime(DateUtils.getNowDateTimeString());
            pickTaskReturn.setCreateUserId(currentUser.getUserId());
            pickTaskReturn.setCreateUserName(currentUser.getUserName());
            pickTaskReturn.setWaveMasterId(waveMaster.getWaveId());
            pickTaskReturn.setBoxCode(waveMaster.getBoxCode());
            pickTaskReturn.setOrgId(pickTask.getOrganizationId());
            pickTaskReturn.setWareId(pickTask.getWareId());
            pickTaskReturn.setFromInventoryId(pickTask.getInventoryId());
            pickTaskReturn.setToCellCode(pickTask.getCellCode());
            pickTaskReturn.setFromCellCode(pickTask.getToCellCode());
            pickTaskReturnService.save(pickTaskReturn);
        }
        soMaster.setState(1);
        soMasterService.updateState(soMaster);
        soDetailService.cancel(soMaster.getBillNo());
        WaveDetail waveDetail = waveDetailService.findBy("soMasterId",soMaster.getSoMasterId());
        waveDetailService.deleteById(waveDetail.getWaveDetailId());
//        List<SoDetail> soDetails = soDetailService.findByMasterId(soMasterId);
//        for (SoDetail soDetail:soDetails   ) {
//            soDetail.setPickQuantity(0.0);
//            soDetail.setCheckQuantity(0.0);
//            soDetail.setState(1);
//            soDetailService.update(soDetail);
//        }
        return ResultGenerator.genSuccessResult(list);
    }

    //根据追踪号查询
    @GetMapping("/getByBox")
    @Authority("pick_task_returns_getByBox")
    public Result getByBox(String boxCode, Integer wareId) {

        List<PickTaskReturnDto> list = pickTaskReturnService.findByBoxCode(boxCode, wareId);
        return ResultGenerator.genSuccessResult(list);
    }

}
