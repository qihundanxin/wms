package com.deer.wms.base.system.dao;

import com.deer.wms.base.system.model.ItemCombined.ItemCombined;
import com.deer.wms.base.system.model.ItemCombined.ItemCombinedCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemCombinedMapper extends Mapper<ItemCombined> {
    List<ItemCombined> findList(ItemCombinedCriteria criteria);

    ItemCombined findByCombinedId(@Param("combinedId") Integer id);
}