package com.deer.wms.ware.task.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.Box.BoxDetail;
import com.deer.wms.ware.task.model.Box.BoxDetailCriteria;

import java.util.List;

public interface BoxDetailMapper extends Mapper<BoxDetail> {
    List<BoxDetail> findList(BoxDetailCriteria criteria);
}