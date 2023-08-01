package com.deer.wms.ware.task.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.base.system.model.Area.AreaInfo;
import com.deer.wms.base.system.model.Cell.CellInfo;
import com.deer.wms.base.system.model.Shelf.ShelfInfo;
import com.deer.wms.base.system.service.AreaInfoService;
import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.base.system.service.ShelfInfoService;
import com.deer.wms.inventory.service.PickTaskConnector;
import com.deer.wms.ware.task.model.pickTask.PickTask;

@Service
public class PickTaskConnectorImpl implements PickTaskConnector {
    @Autowired
    private CellInfoService cellInfoService;
    @Autowired
    private ShelfInfoService shelfInfoService;
    @Autowired
    private AreaInfoService areaInfoService;
    @Autowired
    private PickTaskServiceImpl pickTaskService;

    @Transactional
    @Override
    public void pickTaskAllMove(Integer inventoryId, Integer newInventoryId, String cellCode, Integer wareId) {
        CellInfo cellInfo = cellInfoService.findByCodeAndWareId(cellCode, wareId);
        ShelfInfo shelfInfo = shelfInfoService.findById(cellInfo.getShelfInfoId());
        AreaInfo areaInfo = areaInfoService.findById(shelfInfo.getAreaId());
        List<PickTask> pickTaskList = pickTaskService.findPickingByInventory(inventoryId);
        for (PickTask item : pickTaskList) {
            item.setCellCode(cellCode);
            item.setCellName(cellInfo.getCellName());
            item.setShelfCode(shelfInfo.getShelfCode());
            item.setShelfName(shelfInfo.getShelfName());
            item.setAreaCode(areaInfo.getAreaCode());
            item.setAreaName(areaInfo.getAreaName());
            item.setInventoryId(newInventoryId);
            pickTaskService.update(item);
        }
    }
}
