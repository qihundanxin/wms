package com.deer.wms.base.system.dao;

import com.deer.wms.base.system.model.ItemCombined.ItemComponent;
import com.deer.wms.base.system.model.ItemCombined.ItemComponentCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemComponentMapper extends Mapper<ItemComponent> {
    List<ItemComponent> findList(ItemComponentCriteria criteria);

    void deleteByCombinedId(@Param("itemCode") String code);
}