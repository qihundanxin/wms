package com.deer.wms.base.system.service.impl;

import com.deer.wms.base.system.dao.ItemTypeMapper;
import com.deer.wms.base.system.model.Item.ItemType;
import com.deer.wms.base.system.model.Item.ItemTypeCriteria;
import com.deer.wms.base.system.model.Item.ItemTypeDto;
import com.deer.wms.base.system.service.ItemTypeService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guo on 2019/11/19.
 */
@Service
@Transactional
public class ItemTypeServiceImpl extends AbstractService<ItemType, Integer> implements ItemTypeService {

    @Autowired
    private ItemTypeMapper itemTypeMapper;

    @Override
    public void deleteByCode(String itemTypeCode) {
        itemTypeMapper.deleteByCode(itemTypeCode);
    }

    @Override
    public void deleteByIdAndLowLevel(Integer itemTypeId) {
        itemTypeMapper.deleteByIdAndLowLevel(itemTypeId);
    }

    @Override
    public List<ItemTypeDto> findList(ItemTypeCriteria criteria) {
        return itemTypeMapper.findList(criteria);
    }

    @Override
    public List<ItemType> findByCode(String itemTypeCode) {
        return itemTypeMapper.findByCode(itemTypeCode);
    }

    @Override
    public List<ItemType> findByName(String itemTypeName) {
        return itemTypeMapper.findByName(itemTypeName);
    }

    @Override
    public List<ItemType> findByItemCode(String itemCode) {
        return itemTypeMapper.findByItemCode(itemCode);
    }

    @Override
    public List<ItemTypeDto> findParentList(ItemType itemType) {
        return itemTypeMapper.findParentList(itemType);
    }

    public List<Integer> treeParentList(List<ItemTypeDto> list, ItemType itemType) {
        List<Integer> newList = new ArrayList<Integer>();
        for (ItemType type : list) {
            //遍历出父id等于参数的id，add进子节点集合
            Integer itemTypeId = type.getItemTypeId();
            //   Integer parentId = type.getParentId();
            if (itemType.getParentId() == itemTypeId) {
                //递归遍历下一级
                // list.remove(itemType);
                treeParentList(list, type);
                newList.add(itemTypeId);
            }
        }
        newList.add(itemType.getItemTypeId());
        return newList;
    }

    public List<Integer> treeMenuList(List<ItemType> list, Integer parentId) {
        List<Integer> newList = new ArrayList<Integer>();
        for (ItemType itemType : list) {
            //遍历出父id等于参数的id，add进子节点集合
            Integer iParentId = itemType.getParentId();
            if (iParentId.equals(parentId)) {
                //递归遍历下一级
                // list.remove(itemType);
                treeMenuList(list, itemType.getItemTypeId());
                newList.add(itemType.getItemTypeId());
            }
        }
        return newList;
    }

}
