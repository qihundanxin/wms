package com.deer.wms.base.system.service;

import com.deer.wms.base.system.model.Item.ItemType;
import com.deer.wms.base.system.model.Item.ItemTypeCriteria;
import com.deer.wms.base.system.model.Item.ItemTypeDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2019/11/19.
 */

public interface ItemTypeService extends Service<ItemType, Integer> {

    void deleteByCode(String itemTypeCode);

    List<ItemTypeDto> findList(ItemTypeCriteria criteria);

    List<ItemType> findByCode(String itemTypeCode);

    List<ItemType> findByName(String itemTypeName);

    List<ItemType> findByItemCode(String itemCode);

    List<ItemTypeDto> findParentList(ItemType itemType);

    List<Integer> treeParentList(List<ItemTypeDto> list, ItemType itemType);

    List<Integer> treeMenuList(List<ItemType> list, Integer parentId);

    void deleteByIdAndLowLevel(Integer itemTypeId);

}
