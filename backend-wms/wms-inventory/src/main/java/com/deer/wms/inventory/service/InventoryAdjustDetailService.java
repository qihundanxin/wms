package com.deer.wms.inventory.service;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.InventoryAdjustDetail;
import com.deer.wms.inventory.model.InventoryAdjustDetailCriteria;
import com.deer.wms.inventory.model.InventoryAdjustDetailDto;
import com.deer.wms.inventory.model.InventoryAdjustDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by ll on 2020/03/12.
 */
public interface InventoryAdjustDetailService extends Service<InventoryAdjustDetail, Integer> {
    List<InventoryAdjustDetailDto> findList(InventoryAdjustDetailCriteria inventoryAdjustDetailCriteria);

    void deleteByBillNo(String billNo);

    void adjustAffirm(InventoryAdjustDetailCriteria criteria, CurrentUser currentUser);
}
