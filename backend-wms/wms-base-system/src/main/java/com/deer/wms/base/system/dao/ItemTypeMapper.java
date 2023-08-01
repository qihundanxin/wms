package com.deer.wms.base.system.dao;

import com.deer.wms.base.system.model.Item.ItemType;
import com.deer.wms.base.system.model.Item.ItemTypeCriteria;
import com.deer.wms.base.system.model.Item.ItemTypeDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemTypeMapper extends Mapper<ItemType> {

    void deleteByCode(@Param("itemTypeCode") String itemTypeCode);

    void deleteByIdAndLowLevel(Integer itemTypeId);

    List<ItemTypeDto> findList(ItemTypeCriteria criteria);

    List<ItemType> findByCode(@Param("itemTypeCode") String itemTypeCode);

    List<ItemType> findByName(@Param("itemTypeName") String itemTypeName);

    List<ItemType> findByItemCode(@Param("itemCode") String itemCode);

    List<ItemTypeDto> findParentList(ItemType itemType);
}