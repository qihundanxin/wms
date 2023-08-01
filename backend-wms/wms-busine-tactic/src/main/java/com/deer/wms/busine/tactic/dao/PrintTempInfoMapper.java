package com.deer.wms.busine.tactic.dao;

import com.deer.wms.busine.tactic.model.PrintTempInfo;
import com.deer.wms.busine.tactic.model.PrintTempInfoCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PrintTempInfoMapper extends Mapper<PrintTempInfo> {

    PrintTempInfo findByWare(@Param("wareId") Integer wareId, @Param("type") String type);

    PrintTempInfo findByWareAndShip(@Param("wareId") Integer wareId, @Param("shipCode") String shipCode);

    List<PrintTempInfo> findList(PrintTempInfoCriteria criteria);
}