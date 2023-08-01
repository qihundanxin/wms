package com.deer.wms.busine.tactic.dao;

import com.deer.wms.busine.tactic.model.TurnoverTacticDetail;
import com.deer.wms.busine.tactic.model.TurnoverTacticDetailCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TurnoverTacticDetailMapper extends Mapper<TurnoverTacticDetail> {

    List<TurnoverTacticDetail> findList(TurnoverTacticDetailCriteria criteria);

    void deleteByTurnoverCode(@Param("turnoverCode") String turnoverCode);
}