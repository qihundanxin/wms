package com.deer.wms.ware.task.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.ShipBox;
import com.deer.wms.ware.task.model.ShipBoxCriteria;
import com.deer.wms.ware.task.model.ShipBoxDto;

import java.util.List;

public interface ShipBoxMapper extends Mapper<ShipBox> {

    List<ShipBoxDto> findList(ShipBoxCriteria criteria);
}