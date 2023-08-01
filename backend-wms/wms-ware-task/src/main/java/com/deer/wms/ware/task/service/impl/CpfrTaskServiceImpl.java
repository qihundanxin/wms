package com.deer.wms.ware.task.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.base.system.model.Item.InventorySafety;
import com.deer.wms.base.system.model.Item.InventorySafetyCriteria;
import com.deer.wms.base.system.model.Item.InventorySafetyDto;
import com.deer.wms.base.system.service.InventorySafetyService;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.ware.task.dao.CpfrTaskMapper;
import com.deer.wms.ware.task.model.CpfrTask;
import com.deer.wms.ware.task.model.SO.SoDetailQh;
import com.deer.wms.ware.task.service.CpfrTaskService;
import com.deer.wms.ware.task.service.SoDetailService;

/**
 * Created by on 2021/08/07.
 */
@Service
@Transactional
public class CpfrTaskServiceImpl extends AbstractService<CpfrTask, Integer> implements CpfrTaskService {

    @Autowired
    private CpfrTaskMapper cpfrTaskMapper;

    @Autowired
    private InventorySafetyService inventorySafetyService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private SoDetailService soDetailService;

    @Override
    public void create() {
        cpfrTaskMapper.cancel();
        // 缺货的订单
        List<SoDetailQh> soDetailDtos = soDetailService.findQhQuantity();
        for (SoDetailQh soDetailQh : soDetailDtos) {
            InventorySafetyCriteria inventorySafetyCriteria = new InventorySafetyCriteria();
            inventorySafetyCriteria.setWareId(soDetailQh.getWareId());
            inventorySafetyCriteria.setItemCode(soDetailQh.getItemCode());
            List<InventorySafety> inventorySafeties = inventorySafetyService.findList(inventorySafetyCriteria);
            InventorySafety inventorySafety = new InventorySafety();
            Double qhQuantity = 0.0;
            if (inventorySafeties.size() > 0) {
                inventorySafety = inventorySafeties.get(0);
            } else {
                inventorySafety.setCpfrCeiling(0.0);
            }
            qhQuantity += soDetailQh.getQhQuantity();
            qhQuantity += inventorySafety.getCpfrCeiling();

            if (qhQuantity <= 0.0) {
                continue;
            }
            bh(soDetailQh.getItemCode(), soDetailQh.getWareId(), soDetailQh.getOrganizationId(), qhQuantity, 2);

        }
        // 配置
        List<InventorySafetyDto> list = inventorySafetyService.bh();
        for (InventorySafetyDto safe : list) {
            if ((safe.getCanAllotQuantity() + safe.getBhQuantity()) >= safe.getCpfrFloor()) {
                continue;
            }
            Double bhQuantity = safe.getCpfrCeiling() - safe.getCanAllotQuantity() - safe.getBhQuantity();
            if (bhQuantity <= 0.0) {
                continue;
            }
            bh(safe.getItemCode(), safe.getWareId(), safe.getOrganizationId(), bhQuantity, 1);
        }

    }

    private void bh(String itemCode, Integer wareId, Integer orgId, Double bhQuantity, Integer type) {

        List<Inventory> list = inventoryService.canBh(itemCode, wareId, orgId);
        String toCellCode = inventoryService.findPickCellCode(itemCode, wareId, orgId);
        for (Inventory iven : list) {
            Double canBh = iven.getQuantity() - iven.getAllotQuantity() - iven.getFreezeQuantity();
            CpfrTask cpfrTask = cpfrTaskMapper.findSame(
                    wareId, orgId, itemCode, iven.getInventoryId(), toCellCode);
            if (cpfrTask == null) {
                cpfrTask = new CpfrTask();
                cpfrTask.setQuantity(0.0);
                cpfrTask.setImgUrl(iven.getImgUrl());
                cpfrTask.setItemCode(iven.getItemCode());
                cpfrTask.setItemName(iven.getItemName());
                cpfrTask.setWareId(iven.getWareId());
                cpfrTask.setWareName(iven.getWareName());
                cpfrTask.setOrganizationId(iven.getOrganizationId());
                cpfrTask.setOrganizationName(iven.getOrganizationName());
                cpfrTask.setState(0);
                cpfrTask.setType(type);
                cpfrTask.setFromCellCode(iven.getCellCode());
                cpfrTask.setFromInventoryId(iven.getInventoryId());
                cpfrTask.setToCellCode(toCellCode);
                cpfrTask.setVersion(0);
                save(cpfrTask);
            }
            if (canBh >= bhQuantity && bhQuantity > 0) {
                cpfrTask.setQuantity(cpfrTask.getQuantity() + bhQuantity);
                update(cpfrTask);
                bhQuantity = 0.0;
            } else if (canBh < bhQuantity && bhQuantity > 0) {
                cpfrTask.setQuantity(cpfrTask.getQuantity() + canBh);
                update(cpfrTask);
                bhQuantity -= canBh;
            }
        }
    }

    public int update(CpfrTask cpfrTask) {
        int rowCount = cpfrTaskMapper.update(cpfrTask);
        if (rowCount == 0) {
            throw new ServiceException(CommonCode.PICK_TASK_VERSION, "update cpfrTask更新了0条数据：" + cpfrTask.getCpfrTaskId() + "," + cpfrTask.getVersion());
        }
        return rowCount;
    }
}
