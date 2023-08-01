package com.deer.wms.busine.tactic.dao;

import com.deer.wms.busine.tactic.model.Upper.UpperTacticDetail;
import com.deer.wms.busine.tactic.model.Upper.UpperTacticDetailCriteria;
import com.deer.wms.busine.tactic.model.Upper.UpperTacticDetailDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UpperTacticDetailMapper extends Mapper<UpperTacticDetail> {

    List<UpperTacticDetailDto> findList(UpperTacticDetailCriteria criteria);

    List<UpperTacticDetail> findByAreaCode(@Param("areaCode") String areaCode);

    List<UpperTacticDetailDto> findByUpperTacticCode(@Param("upperTacticCode") String upperTacticCode);

}