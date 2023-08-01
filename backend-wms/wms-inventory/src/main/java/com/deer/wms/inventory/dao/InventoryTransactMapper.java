package com.deer.wms.inventory.dao;

import java.util.List;

import com.deer.wms.inventory.model.Inventory.ClosePickCellTansactResult;
import com.deer.wms.inventory.model.Inventory.InventoryTransact;
import com.deer.wms.inventory.model.Inventory.InventoryTransactCriteria;
import com.deer.wms.inventory.model.Inventory.InventoryTransactDto;
import com.deer.wms.project.root.core.mapper.Mapper;

public interface InventoryTransactMapper extends Mapper<InventoryTransact> {
    List<InventoryTransactDto> findList(InventoryTransactCriteria criteria);

    ClosePickCellTansactResult findClosePickCellTansactBySku(String itemCode, Integer wareId);
}