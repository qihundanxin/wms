package com.deer.wms.inventory.service;

import com.deer.wms.inventory.model.move.InventoryMove;
import com.deer.wms.inventory.model.move.InventoryMoveCriteria;
import com.deer.wms.inventory.model.move.InventoryMoveDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2019/12/27.
 */
public interface InventoryMoveService extends Service<InventoryMove, Integer> {

    List<InventoryMoveDto> findList(InventoryMoveCriteria criteria);

    void deleteByBillNo(String billNo);

}
