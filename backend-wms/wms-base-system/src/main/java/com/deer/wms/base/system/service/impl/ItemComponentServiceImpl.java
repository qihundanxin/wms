package com.deer.wms.base.system.service.impl;

import com.deer.wms.base.system.dao.ItemComponentMapper;
import com.deer.wms.base.system.model.ItemCombined.ItemComponent;
import com.deer.wms.base.system.model.ItemCombined.ItemComponentCriteria;
import com.deer.wms.base.system.service.ItemComponentService;

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
public class ItemComponentServiceImpl extends AbstractService<ItemComponent, Integer> implements ItemComponentService {

    @Autowired
    private ItemComponentMapper itemComponentMapper;

    @Override
    public List<ItemComponent> findList(ItemComponentCriteria criteria) {
        return itemComponentMapper.findList(criteria);
    }

    @Override
    public void deleteByCombinedCode(String code) {
        itemComponentMapper.deleteByCombinedId(code);
    }
}
