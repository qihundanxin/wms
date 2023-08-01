package com.deer.wms.base.system.dao;

import com.deer.wms.base.system.model.Item.InventorySafety;
import com.deer.wms.base.system.model.Item.InventorySafetyCriteria;
import com.deer.wms.base.system.model.Item.InventorySafetyDto;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface InventorySafetyMapper extends Mapper<InventorySafety> {

    List<InventorySafety> findList(InventorySafetyCriteria criteria);

    List<InventorySafetyDto> bh();
}
