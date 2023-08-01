package com.deer.wms.base.system.service.impl;

import com.deer.wms.base.system.dao.InventorySafetyMapper;
import com.deer.wms.base.system.model.Item.InventorySafety;
import com.deer.wms.base.system.model.Item.InventorySafetyCriteria;
import com.deer.wms.base.system.model.Item.InventorySafetyDto;
import com.deer.wms.base.system.service.InventorySafetyService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ll on 2020/03/03.
 */
@Service
@Transactional
public class InventorySafetyServiceImpl extends AbstractService<InventorySafety, Integer> implements InventorySafetyService {

    @Autowired
    private InventorySafetyMapper inventorySafetyMapper;

    @Override
    public List<InventorySafety> findList(InventorySafetyCriteria criteria) {
        return inventorySafetyMapper.findList(criteria);
    }

    @Override
    public List<InventorySafetyDto> bh() {
        return inventorySafetyMapper.bh();
    }
}
