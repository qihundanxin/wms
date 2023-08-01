package com.deer.wms.inventory.service.impl;

import com.deer.wms.inventory.dao.InventoryMoveDetailMapper;
import com.deer.wms.inventory.model.move.InventoryMoveDetail;
import com.deer.wms.inventory.model.move.InventoryMoveDetailCriteria;
import com.deer.wms.inventory.model.move.InventoryMoveDetailDto;
import com.deer.wms.inventory.service.InventoryMoveDetailService;

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
public class InventoryMoveDetailServiceImpl extends AbstractService<InventoryMoveDetail, Integer> implements InventoryMoveDetailService {

    @Autowired
    private InventoryMoveDetailMapper inventoryMoveDetailMapper;

    @Override
    public List<InventoryMoveDetailDto> findList(InventoryMoveDetailCriteria criteria) {
        return inventoryMoveDetailMapper.findList(criteria);
    }

    @Override
    public void deleteByBillNo(String billNo) {
        inventoryMoveDetailMapper.deleteByBillNo(billNo);
    }
}
