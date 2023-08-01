package com.deer.wms.inventory.dao;

import com.deer.wms.inventory.model.InventoryAdjust;
import com.deer.wms.inventory.model.InventoryAdjustCriteria;
import com.deer.wms.inventory.model.InventoryAdjustDto;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface InventoryAdjustMapper extends Mapper<InventoryAdjust> {

    List<InventoryAdjustDto> findList(InventoryAdjustCriteria inventoryAdjustCriteria);
}