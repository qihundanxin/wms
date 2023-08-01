package com.deer.wms.inventory.service;

import com.deer.wms.inventory.model.move.InventoryMoveDetail;
import com.deer.wms.inventory.model.move.InventoryMoveDetailCriteria;
import com.deer.wms.inventory.model.move.InventoryMoveDetailDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2019/12/27.
 */
public interface InventoryMoveDetailService extends Service<InventoryMoveDetail, Integer> {

    List<InventoryMoveDetailDto> findList(InventoryMoveDetailCriteria criteria);

    void deleteByBillNo(String billNo);

}
