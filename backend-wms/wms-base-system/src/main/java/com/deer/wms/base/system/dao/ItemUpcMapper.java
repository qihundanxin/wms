package com.deer.wms.base.system.dao;

import com.deer.wms.base.system.model.ItemUpc;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemUpcMapper extends Mapper<ItemUpc> {

    void deleteById(Integer itemUpcId);

    List<ItemUpc> findByItemCode(@Param("itemCode") String itemCode);
}
