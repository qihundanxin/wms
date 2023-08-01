package com.deer.wms.ASN.manage.web;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.ASN.manage.model.SjcUpperTask;
import com.deer.wms.ASN.manage.model.SjcUpperTaskCriteria;
import com.deer.wms.ASN.manage.service.SjcUpperTaskService;
import com.deer.wms.base.system.model.Cell.CellInfo;
import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.constant.InventoryConstant;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.Inventory.InventoryCriteria;
import com.deer.wms.inventory.model.Inventory.InventoryTransactRunCriteria;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.inventory.service.InventoryTransactService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.ureport.service.ReportOperateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Created by on 2021/01/07.
 */
@Transactional
@RestController
@RequestMapping("/sjc/upper/tasks")
public class SjcUpperTaskController {

    @Autowired
    private SjcUpperTaskService sjcUpperTaskService;

    @Autowired
    private CellInfoService cellInfoService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryTransactService inventoryTransactService;

    @Autowired
    private ReportOperateService reportOperateService;

    @PostMapping("/add")
    @Transactional
    @Authority("sjc_upper_tasks_add")
    public Result add(@RequestBody SjcUpperTask sjcUpperTask, @User CurrentUser currentUser) {

        Inventory inventory = inventoryService.findById(sjcUpperTask.getAcceptInventoryId());
        if (inventory == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "该库存码无效！");
        }
//        if(inventory.getQuantity()<sjcUpperTask.getQuantity()){
//            throw  new ServiceException(CommonCode.SERVICE_ERROR,"输入数量和实际数量不符，请校验数量！");
//        }
//        SjcUpperTask sjcUpperTask1 = sjcUpperTaskService.findBy("inventoryId",inventory.getInventoryId());
//        if(sjcUpperTask1 != null){
//            throw  new ServiceException(CommonCode.SERVICE_ERROR,"该库存已经到上架车！");
//        }
        if (inventory.getAllotQuantity() > 0 || inventory.getBaoKuan() > 0) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "该产品是爆款，请将" + inventory.getAllotQuantity() + "件产品送到打包台后，剩余的再上架！");
        }
        String fromCellCode = inventory.getCellCode();
        CellInfo fromCellInfo = cellInfoService.findByCodeAndWareId(fromCellCode, sjcUpperTask.getWareId());
        if (fromCellInfo == null) {
            // 原始货位有问题、
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到原始货位");
        }
        if (fromCellInfo.getCellUseType() != 0) {
            // 原始货位的货不在验收位上面
            throw new ServiceException(CommonCode.SERVICE_ERROR, "该产品不在验收位置，不能上架");
        }

        String sjcCellCode = sjcUpperTask.getSjcCellCode();
        CellInfo cellInfo = cellInfoService.findByCodeAndWareId(sjcCellCode, sjcUpperTask.getWareId());
        if (cellInfo == null) {
            // 上架车货位号有问题
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到上架车货位");
        }
        if (cellInfo.getCellUseType() != 4) {
            // 此货位的使用类型不是上架车
            throw new ServiceException(CommonCode.SERVICE_ERROR, cellInfo.getCellCode() + "：不是上架车位置！");
        }
        InventoryCriteria inventoryCriteria = new InventoryCriteria();
        inventoryCriteria.setWareId(sjcUpperTask.getWareId());
        inventoryCriteria.setCellCode(sjcCellCode);
        List<Inventory> inventorys = inventoryService.findByCellCode(inventoryCriteria);
        for (Inventory inven : inventorys) {
            if (!inven.getItemCode().equals(inventory.getItemCode())) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "该上架车位置已经有其他库存");
            }
        }
        sjcUpperTask.setCreateTime(DateUtils.getNowDateTimeString());
        sjcUpperTask.setCreateUserId(currentUser.getUserId());
        sjcUpperTask.setCreateUserName(currentUser.getUserName());

        sjcUpperTask.setItemCode(inventory.getItemCode());
        sjcUpperTask.setItemName(inventory.getItemName());
        sjcUpperTask.setSpec(inventory.getSpec());
        sjcUpperTask.setModel(inventory.getModel());
        sjcUpperTask.setImgUrl(inventory.getImgUrl());
        sjcUpperTask.setFromCellCode(inventory.getCellCode());
        // sjcUpperTask.setQuantity(inventory.getQuantity());
        sjcUpperTask.setWareId(inventory.getWareId());

        // 如果之前入过相同的产品则放在相同货位或者相邻货位
        String itemCode = sjcUpperTask.getItemCode();
        String cellCode = sjcUpperTaskService.gerCellCode(itemCode, sjcUpperTask.getWareId(), sjcUpperTask.getOrgId());

        sjcUpperTask.setToCellCode(cellCode);

        // 看一下这个上架车这个位置 是否有相同的sku
        SjcUpperTask oldTask = sjcUpperTaskService.find0ByCellCode(sjcUpperTask.getSjcCellCode(), sjcUpperTask.getWareId());

        Inventory newInventory = new Inventory();
        // 把库存转移到上架车上面
        if (oldTask != null && oldTask.getInventoryId() != null) {
            newInventory = inventoryService.findById(oldTask.getInventoryId());
            newInventory.setQuantity(newInventory.getQuantity() + sjcUpperTask.getQuantity());
            // version
            inventoryService.update(newInventory);
            oldTask.setQuantity(sjcUpperTask.getQuantity() + oldTask.getQuantity());
            sjcUpperTaskService.update(oldTask);
        } else {
            BeanUtils.copyBeanProp(newInventory, inventory);
            newInventory.setInventoryId(null);
            newInventory.setCellCode(sjcUpperTask.getSjcCellCode());
            newInventory.setCellUseType(cellInfo.getCellUseType());
            newInventory.setQuantity(sjcUpperTask.getQuantity());

            inventoryService.save(newInventory);

            sjcUpperTask.setInventoryId(newInventory.getInventoryId());
            sjcUpperTaskService.save(sjcUpperTask);
        }

        if (inventory.getQuantity() - sjcUpperTask.getQuantity() < 0.0d) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "验收位数量小于需要上到上架车上的数量，请核对！");
        }
        inventory.setQuantity(inventory.getQuantity() - sjcUpperTask.getQuantity());
        inventoryService.update(inventory);
//        if (inventory.getQuantity() < 0.1) {
////            inventoryService.deleteByIdVersion(inventory.getInventoryId(), inventory.getVersion());
//        } else {
//            // version
//            inventoryService.update(inventory);
//        }

        newInventory.setQuantity(sjcUpperTask.getQuantity());
        InventoryTransactRunCriteria runCriteria = new InventoryTransactRunCriteria();
        runCriteria.setFromInventory(inventory);
        runCriteria.setToInventory(newInventory);
        runCriteria.setTransactType(InventoryConstant.SJC1);
        runCriteria.setCreateUserId(currentUser.getUserId());
        runCriteria.setCreateUserName(currentUser.getUserName());
        inventoryTransactService.run(runCriteria);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/check")
    @Authority("sjc_upper_tasks_check")
    public Result check(Integer sjcUpperTaskId, String toCellCode, Integer wareId, @User CurrentUser currentUser) {
        CellInfo cellInfo = cellInfoService.findByCodeAndWareId(toCellCode, wareId);
        if (cellInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "货位无效！");
        }
        if (cellInfo.getCellUseType() != 2 && cellInfo.getCellUseType() != 1) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "目标位置必须是仓储位或拣货位！");
        }
        SjcUpperTask sjcUpperTask = sjcUpperTaskService.findById(sjcUpperTaskId);
        if (sjcUpperTask.getState() == 1) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "该任务已经完成！");
        }
        InventoryCriteria inventoryCriteria = new InventoryCriteria();
        inventoryCriteria.setCellCode(toCellCode);
        Integer skuLimit = 0;
        List<Inventory> list = inventoryService.findByCellCode(inventoryCriteria);
        if (list.size() > 0) {
            for (Inventory inventory : list) {
                if (!inventory.getItemCode().equals(sjcUpperTask.getItemCode())) {
                    skuLimit += 1;
                    if (cellInfo.getSkuLimit() == 1) {
                        throw new ServiceException(CommonCode.SERVICE_ERROR, "有其他上架车任务即将要放到该位置！");
                    }
                }
            }
        }

        SjcUpperTask sjcUpperTask1 = sjcUpperTaskService.find0ByToCellCode(toCellCode, wareId);
        if (sjcUpperTask1 != null && !sjcUpperTask.getToCellCode().equals(toCellCode) && sjcUpperTask1.getSjcUpperTaskId() != sjcUpperTaskId) {
            skuLimit += 1;
            if (cellInfo.getSkuLimit() == 1) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "有其他上架车任务即将要放到该位置！");
            }
        }
        if (cellInfo.getSkuLimit() < skuLimit) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "货位：" + cellInfo.getCellCode() + " 已达到sku限制最大值-" + cellInfo.getSkuLimit() + "!");
        }
//        List<Inventory> list = inventoryService.findByCellCode(inventoryCriteria);
//        if(list.size()>0){
//            for (Inventory inventory:list
//            ) {
//                if(!inventory.getItemCode().equals(sjcUpperTask.getItemCode())){
//                    throw  new ServiceException(CommonCode.SERVICE_ERROR,"该货位已经有其他货物！");
//                }
//            }
//        }
//
//        SjcUpperTask sjcUpperTask1 = sjcUpperTaskService.find0ByToCellCode(toCellCode,wareId);
//        if(sjcUpperTask1 !=null && !sjcUpperTask.getToCellCode().equals(toCellCode) && sjcUpperTask1.getSjcUpperTaskId() != sjcUpperTaskId){
//            throw  new ServiceException(CommonCode.SERVICE_ERROR,"有其他上架车任务即将要放到该位置！");
//        }

        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/commit")
    @Transactional
    @Authority("sjc_upper_tasks_commit")
    public Result commit(Integer sjcUpperTaskId, String toCellCode, Integer wareId, @User CurrentUser currentUser) {
        CellInfo cellInfo = cellInfoService.findByCodeAndWareId(toCellCode, wareId);
        if (cellInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "货位无效！");
        }
        if (cellInfo.getCellUseType() != 2 && cellInfo.getCellUseType() != 1) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "目标位置必须是仓储位或拣货位！");
        }
        SjcUpperTask sjcUpperTask = sjcUpperTaskService.findById(sjcUpperTaskId);
        if (sjcUpperTask.getState() == 1) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "该任务已经完成！");
        }
        InventoryCriteria inventoryCriteria = new InventoryCriteria();
        inventoryCriteria.setCellCode(toCellCode);
        inventoryCriteria.setWareId(wareId);
        Integer skuLimit = 0;
        List<Inventory> list = inventoryService.findByCellCode(inventoryCriteria);
        if (list.size() > 0) {
            for (Inventory inventory : list) {
                if (!inventory.getItemCode().equals(sjcUpperTask.getItemCode())) {
                    skuLimit += 1;
                    if (cellInfo.getSkuLimit() == 1) {
                        throw new ServiceException(CommonCode.SERVICE_ERROR, "有其他上架车任务即将要放到该位置！");
                    }
                }
            }
        }

        SjcUpperTask sjcUpperTask1 = sjcUpperTaskService.find0ByToCellCode(toCellCode, wareId);
        if (sjcUpperTask1 != null && !sjcUpperTask.getToCellCode().equals(toCellCode) && sjcUpperTask1.getSjcUpperTaskId() != sjcUpperTaskId) {
            skuLimit += 1;
            if (cellInfo.getSkuLimit() == 1) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "有其他上架车任务即将要放到该位置！");
            }
        }
        if (cellInfo.getSkuLimit() < skuLimit) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "货位：" + cellInfo.getCellCode() + " 已达到sku限制最大值：" + cellInfo.getSkuLimit().intValue() + "!");
        }
//        List<Inventory> list = inventoryService.findByCellCode(inventoryCriteria);
//        if(list.size()>0){
//            for (Inventory inventory:list
//                 ) {
//                if(!inventory.getItemCode().equals(sjcUpperTask.getItemCode())){
//                    throw  new ServiceException(CommonCode.SERVICE_ERROR,"该货位已经有其他货物！");
//                }
//            }
//        }
//
//        SjcUpperTask sjcUpperTask1 = sjcUpperTaskService.find0ByToCellCode(toCellCode,wareId);
//        if(sjcUpperTask1 !=null && !sjcUpperTask.getToCellCode().equals(toCellCode) && sjcUpperTask1.getSjcUpperTaskId() != sjcUpperTaskId){
//            throw  new ServiceException(CommonCode.SERVICE_ERROR,"有其他上架车任务即将要放到该位置！");
//        }

        Inventory inventory = inventoryService.findById(sjcUpperTask.getInventoryId());
        Inventory oldInventory = inventoryService.findByItemWareOrg(inventory.getItemCode(), inventory.getWareId(), inventory.getOrganizationId(), toCellCode);
        if (oldInventory != null) {
            oldInventory.setQuantity(oldInventory.getQuantity() + sjcUpperTask.getQuantity());
            // version
            inventoryService.update(oldInventory);

            // inventoryService.deleteById(inventory.getInventoryId());
        } else {
            oldInventory = new Inventory();
            BeanUtils.copyBeanProp(oldInventory, inventory);
            oldInventory.setInventoryId(null);
            oldInventory.setQuantity(sjcUpperTask.getQuantity());
            oldInventory.setCellCode(toCellCode);
            oldInventory.setCellUseType(cellInfo.getCellUseType());
            inventoryService.save(oldInventory);

        }

        inventory.setQuantity(inventory.getQuantity() - sjcUpperTask.getQuantity());
        inventoryService.update(inventory);
        if (inventory.getQuantity() < 0.1) {
            reportOperateService.report(wareId, currentUser.getUserId(), 2, 1, 1, null, inventory.getItemCode(), inventory.getItemName(), inventory.getImgUrl());
        }
        sjcUpperTask.setState(1);
        sjcUpperTask.setUpTime(DateUtils.getNowDateTimeString());
        sjcUpperTask.setUpUserId(currentUser.getUserId());
        sjcUpperTask.setUpUserName(currentUser.getUserName());
        sjcUpperTask.setToCellCode2(toCellCode);
        sjcUpperTaskService.update(sjcUpperTask);

        oldInventory.setQuantity(sjcUpperTask.getQuantity());
        InventoryTransactRunCriteria runCriteria = new InventoryTransactRunCriteria();
        runCriteria.setFromInventory(inventory);
        runCriteria.setToInventory(oldInventory);
        runCriteria.setTransactType(InventoryConstant.SJC2);
        runCriteria.setCreateUserId(currentUser.getUserId());
        runCriteria.setCreateUserName(currentUser.getUserName());
        inventoryTransactService.run(runCriteria);
        return ResultGenerator.genSuccessResult();
    }

    // 直接上架
    @GetMapping("/zjUp")
    @Transactional
    @Authority("sjc_upper_tasks_zjUp")
    public Result zjUp(Integer InventoryId, Double quantity, String toCellCode, Integer wareId, @User CurrentUser currentUser) {
        CellInfo cellInfo = cellInfoService.findByCodeAndWareId(toCellCode, wareId);
        if (cellInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "所选仓库无此货位！");
        }
        if (cellInfo.getCellUseType() != 2 && cellInfo.getCellUseType() != 1) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "目标位置必须是仓储位或拣货位！");
        }
        toCellCode = cellInfo.getCellCode();
        Inventory inventory = inventoryService.findById(InventoryId);
        String fromCellCode = inventory.getCellCode();
        inventory.setItemCode(inventory.getItemCode());
        if (inventory.getQuantity() < quantity) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "请校验数量！");
        }

        Integer skuLimit = 0;
        SjcUpperTask sjcUpperTask1 = sjcUpperTaskService.find0ByToCellCode(toCellCode, wareId);
        if (sjcUpperTask1 != null) {
            skuLimit += 1;
            if (cellInfo.getSkuLimit() == 1) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "有其他上架车任务即将要放到该位置！");
            }
        }
        InventoryCriteria inventoryCriteria = new InventoryCriteria();
        inventoryCriteria.setCellCode(toCellCode);
        List<Inventory> list = inventoryService.findByCellCode(inventoryCriteria);
        list = list.stream().collect(Collectors.collectingAndThen((Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Inventory::getItemCode)))), ArrayList::new));// 拣货位库存.去重
        if (list.size() > 0) {
            for (Inventory inven : list) {
                if (!inven.getItemCode().equals(inventory.getItemCode())) {
                    skuLimit += 1;
                    if (cellInfo.getSkuLimit() == 1) {
                        throw new ServiceException(CommonCode.SERVICE_ERROR, "货位：" + cellInfo.getCellCode() + " 已达到sku限制最大值：" + cellInfo.getSkuLimit().intValue() + "!");
                    }
                }
            }
        }
        if (cellInfo.getSkuLimit() < skuLimit) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "货位：" + cellInfo.getCellCode() + " 已达到sku限制最大值：" + cellInfo.getSkuLimit().intValue() + "!");
        }

//        SjcUpperTask sjcUpperTask1 = sjcUpperTaskService.find0ByToCellCode(toCellCode,wareId);
//        if(sjcUpperTask1 !=null ){
//            throw  new ServiceException(CommonCode.SERVICE_ERROR,"有其他上架车任务即将要放到该位置！");
//        }
//        InventoryCriteria inventoryCriteria = new InventoryCriteria();
//        inventoryCriteria.setCellCode(toCellCode);
//        List<Inventory> list = inventoryService.findByCellCode(inventoryCriteria);
//        if(list.size()>0){
//            for (Inventory inven:list
//            ) {
//                if(!inven.getItemCode().equals(inventory.getItemCode())){
//                    throw  new ServiceException(CommonCode.SERVICE_ERROR,"该货位已经有其他货物！");
//                }
//            }
//        }
        Inventory oldInventory = inventoryService.findByItemWareOrg(inventory.getItemCode(), inventory.getWareId(), inventory.getOrganizationId(), toCellCode);
        if (oldInventory != null) {
            if (oldInventory.getBatchName().equals(inventory.getBatchName())){
                oldInventory.setQuantity(oldInventory.getQuantity() + quantity);
                // version
                inventoryService.update(oldInventory);
            }else {
                oldInventory = new Inventory();
                BeanUtils.copyBeanProp(oldInventory, inventory);
                oldInventory.setInventoryId(null);
                oldInventory.setQuantity(quantity);
                oldInventory.setCellCode(toCellCode);
                oldInventory.setCellUseType(cellInfo.getCellUseType());
                inventoryService.save(oldInventory);
            }
            // inventoryService.deleteById(inventory.getInventoryId());
        } else {
            oldInventory = new Inventory();
            BeanUtils.copyBeanProp(oldInventory, inventory);
            oldInventory.setInventoryId(null);
            oldInventory.setQuantity(quantity);
            oldInventory.setCellCode(toCellCode);
            oldInventory.setCellUseType(cellInfo.getCellUseType());
            inventoryService.save(oldInventory);

        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryService.update(inventory);
//        if (inventory.getQuantity() < 0.1) {
////            inventoryService.deleteByIdVersion(inventory.getInventoryId(), inventory.getVersion());
//        } else {
//            inventoryService.update(inventory);
//        }

        SjcUpperTask sjcUpperTask = new SjcUpperTask();
        sjcUpperTask.setQuantity(quantity);
        sjcUpperTask.setOrgId(inventory.getOrganizationId());
        sjcUpperTask.setWareId(inventory.getWareId());
        sjcUpperTask.setFromCellCode(fromCellCode);
        sjcUpperTask.setToCellCode2(toCellCode);
        sjcUpperTask.setUpUserName(currentUser.getUserName());
        sjcUpperTask.setUpUserId(currentUser.getUserId());
        sjcUpperTask.setUpTime(DateUtils.getNowDateTimeString());
        sjcUpperTask.setType(1);
        sjcUpperTask.setState(1);
        sjcUpperTask.setItemName(inventory.getItemName());
        sjcUpperTask.setItemCode(inventory.getItemCode());
        sjcUpperTask.setImgUrl(inventory.getImgUrl());
        sjcUpperTask.setCreateTime(DateUtils.getNowDateTimeString());
        sjcUpperTaskService.save(sjcUpperTask);

        oldInventory.setQuantity(quantity);
        InventoryTransactRunCriteria runCriteria = new InventoryTransactRunCriteria();
        runCriteria.setFromInventory(inventory);
        runCriteria.setToInventory(oldInventory);
        runCriteria.setTransactType(InventoryConstant.SERIALNO_TRANSFER);
        runCriteria.setCreateUserId(currentUser.getUserId());
        runCriteria.setCreateUserName(currentUser.getUserName());
        inventoryTransactService.run(runCriteria);
        // 记录绩效
        reportOperateService.report(wareId, currentUser.getUserId(), 2, 1, 1, null, inventory.getItemCode(), inventory.getItemName(), inventory.getImgUrl());

        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("sjc_upper_tasks_delete")
    public Result delete(Integer id) {
        sjcUpperTaskService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("sjc_upper_tasks_update")
    public Result update(@RequestBody SjcUpperTask sjcUpperTask) {
        sjcUpperTaskService.update(sjcUpperTask);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("sjc_upper_tasks_get")
    public Result detail(@PathVariable Integer id) {
        SjcUpperTask sjcUpperTask = sjcUpperTaskService.findById(id);
        return ResultGenerator.genSuccessResult(sjcUpperTask);
    }

    @GetMapping("/list")
    @Authority("sjc_upper_tasks_list")
    public Result list(SjcUpperTaskCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SjcUpperTask> list = sjcUpperTaskService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/findBySjcShellCode")
    @Authority("sjc_upper_tasks_findBySjcShellCode")
    public Result findBySjcShellCode(SjcUpperTaskCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SjcUpperTask> list = sjcUpperTaskService.findBySjcShellCode(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(list);
    }

}
