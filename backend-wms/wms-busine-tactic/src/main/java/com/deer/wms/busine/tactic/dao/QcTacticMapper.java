package com.deer.wms.busine.tactic.dao;

import com.deer.wms.busine.tactic.model.QC.QcTactic;
import com.deer.wms.busine.tactic.model.QC.QcTacticCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface QcTacticMapper extends Mapper<QcTactic> {
    List<QcTactic> findList(QcTacticCriteria criteria);
}