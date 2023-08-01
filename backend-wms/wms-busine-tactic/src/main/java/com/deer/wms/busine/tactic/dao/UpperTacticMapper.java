package com.deer.wms.busine.tactic.dao;

import com.deer.wms.busine.tactic.model.Upper.UpperTactic;
import com.deer.wms.busine.tactic.model.Upper.UpperTacticCriteria;
import com.deer.wms.busine.tactic.model.Upper.UpperTacticDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UpperTacticMapper extends Mapper<UpperTactic> {
    List<UpperTacticDto> findList(UpperTacticCriteria criteria);

    List<UpperTactic> findByWareId(Integer wareId);

    List<UpperTactic> findByUpperTacticCode(@Param("upperTacticCode") String upperTacticCode);

    List<UpperTactic> findByOrganizationAndWare(@Param("organizationId") Integer organizationId, @Param("wareId") Integer wareId);

}