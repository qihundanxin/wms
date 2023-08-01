package com.deer.wms.inventory.service;

import com.deer.wms.inventory.model.BatchAdjustDetailCriteria;
import com.deer.wms.inventory.model.InventoryAdjust;
import com.deer.wms.inventory.model.InventoryAdjustCriteria;
import com.deer.wms.inventory.model.InventoryAdjustDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by ll on 2020/02/13.
 */
public interface InventoryAdjustService extends Service<InventoryAdjust, Integer> {
    List<InventoryAdjustDto> findList(InventoryAdjustCriteria inventoryAdjustCriteria);

    void batchCreateInventoryAdjust( List<BatchAdjustDetailCriteria> criterias);
}
