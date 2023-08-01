package com.deer.wms.ware.task.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.inventory.model.freeze.FreezeDetail;
import com.deer.wms.inventory.service.FreezeConnector;
import com.deer.wms.inventory.service.FreezeDetailService;

@Service
public class FreezeConnectorImpl implements FreezeConnector {
    @Autowired
    private FreezeDetailService freezeDetailService;

    @Transactional
    @Override
    public void freezeAllMove(Integer inventoryId, Integer newInventoryId, String cellCode, Integer wareId) {
        List<FreezeDetail> freezeDetailList = freezeDetailService.findFreezingByInventory(inventoryId);
        for (FreezeDetail item : freezeDetailList) {
            item.setCellCode(cellCode);
            item.setInventoryId(newInventoryId);
            freezeDetailService.update(item);
        }
    }

}
