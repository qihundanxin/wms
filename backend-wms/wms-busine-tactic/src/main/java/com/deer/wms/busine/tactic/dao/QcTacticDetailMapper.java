package com.deer.wms.busine.tactic.dao;

import com.deer.wms.busine.tactic.model.QC.QcTacticDetail;
import com.deer.wms.busine.tactic.model.QC.QcTacticDetailCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QcTacticDetailMapper extends Mapper<QcTacticDetail> {
    List<QcTacticDetail> findList(QcTacticDetailCriteria criteria);

    List<QcTacticDetail> findByQcTacticCode(@Param("qcTacticCode") String qcTacticCode);
}