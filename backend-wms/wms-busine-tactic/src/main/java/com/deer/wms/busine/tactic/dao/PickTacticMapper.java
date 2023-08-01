package com.deer.wms.busine.tactic.dao;

import com.deer.wms.busine.tactic.model.pickTactic.PickTactic;
import com.deer.wms.busine.tactic.model.pickTactic.PickTacticCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface PickTacticMapper extends Mapper<PickTactic> {
    List<PickTactic> findList(PickTacticCriteria criteria);

    List<PickTactic> verify(PickTacticCriteria criteria);
}