package com.deer.wms.ware.task.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.ShipBoxDetail;
import com.deer.wms.ware.task.model.ShipBoxDetailCriteria;

import java.util.List;

public interface ShipBoxDetailMapper extends Mapper<ShipBoxDetail> {

    List<ShipBoxDetail> findList(ShipBoxDetailCriteria criteria);
}