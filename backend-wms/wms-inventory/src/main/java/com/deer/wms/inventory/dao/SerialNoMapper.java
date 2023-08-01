package com.deer.wms.inventory.dao;

import com.deer.wms.inventory.model.SerialNo;
import com.deer.wms.inventory.model.SerialNoCriteria;
import com.deer.wms.inventory.model.SerialNoDto;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface SerialNoMapper extends Mapper<SerialNo> {

    List<SerialNoDto> findList(SerialNoCriteria criteria);

    List<SerialNoDto> findInventoryList(SerialNoCriteria criteria);
}