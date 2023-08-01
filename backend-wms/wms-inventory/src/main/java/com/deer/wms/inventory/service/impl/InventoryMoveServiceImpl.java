package com.deer.wms.inventory.service.impl;

import com.deer.wms.inventory.dao.InventoryMoveMapper;
import com.deer.wms.inventory.model.move.InventoryMove;
import com.deer.wms.inventory.model.move.InventoryMoveCriteria;
import com.deer.wms.inventory.model.move.InventoryMoveDto;
import com.deer.wms.inventory.service.InventoryMoveService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2019/12/27.
 */
@Service
@Transactional
public class InventoryMoveServiceImpl extends AbstractService<InventoryMove, Integer> implements InventoryMoveService {

    @Autowired
    private InventoryMoveMapper inventoryMoveMapper;

    @Override
    public List<InventoryMoveDto> findList(InventoryMoveCriteria criteria) {
        return inventoryMoveMapper.findList(criteria);
    }

    @Override
    public void deleteByBillNo(String billNo) {
        inventoryMoveMapper.deleteByBillNo(billNo);
    }
}
