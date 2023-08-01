package com.deer.wms.busine.tactic.dao;

import java.util.List;

import com.deer.wms.busine.tactic.model.SoMergeTactic;
import com.deer.wms.busine.tactic.model.SoMergeTacticCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;

public interface SoMergeTacticMapper extends Mapper<SoMergeTactic> {

    List<SoMergeTactic> findList(SoMergeTacticCriteria criteria);
}