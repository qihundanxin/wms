package com.deer.wms.busine.tactic.dao;

import com.deer.wms.busine.tactic.model.TurnoverTactic;
import com.deer.wms.busine.tactic.model.TurnoverTacticCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface TurnoverTacticMapper extends Mapper<TurnoverTactic> {

    List<TurnoverTactic> findList(TurnoverTacticCriteria criteria);
}