package com.deer.wms.busine.tactic.dao;

import com.deer.wms.busine.tactic.model.RouteTactic;
import com.deer.wms.busine.tactic.model.RouteTacticCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface RouteTacticMapper extends Mapper<RouteTactic> {
    List<RouteTactic> findList(RouteTacticCriteria criteria);
}