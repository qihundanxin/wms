package com.deer.wms.base.system.service.impl;

import com.deer.wms.base.system.dao.ItemUpcMapper;
import com.deer.wms.base.system.model.ItemUpc;
import com.deer.wms.base.system.service.ItemUpcService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by  on 2021/07/08.
 */
@Service
@Transactional
public class ItemUpcServiceImpl extends AbstractService<ItemUpc, Integer> implements ItemUpcService {

    @Autowired
    private ItemUpcMapper itemUpcMapper;

    @Override
    public void delete(Integer itemUpcId) {
        itemUpcMapper.deleteById(itemUpcId);
    }

    @Override
    public List<ItemUpc> findByItemCode(String itemCode) {
      return  itemUpcMapper.findByItemCode(itemCode);
    }
}
