package com.deer.wms.base.system.service.impl;

import com.deer.wms.base.system.dao.ItemCombinedMapper;
import com.deer.wms.base.system.model.ItemCombined.ItemCombined;
import com.deer.wms.base.system.model.ItemCombined.ItemCombinedCriteria;
import com.deer.wms.base.system.service.ItemCombinedService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ll on 2020/02/14.
 */
@Service
@Transactional
public class ItemCombinedServiceImpl extends AbstractService<ItemCombined, Integer> implements ItemCombinedService {

    @Autowired
    private ItemCombinedMapper itemCombinedMapper;

    @Override
    public List<ItemCombined> findList(ItemCombinedCriteria criteria) {
        return itemCombinedMapper.findList(criteria);
    }

    @Override
    public ItemCombined findByCombinedId(Integer id) {
        return itemCombinedMapper.findByCombinedId(id);
    }
}
