package com.deer.wms.inventory.dao;

import com.deer.wms.inventory.model.Reason;
import com.deer.wms.inventory.model.ReasonCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface ReasonMapper extends Mapper<Reason> {
    List<Reason> findList(ReasonCriteria criteria);
}