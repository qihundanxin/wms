package com.deer.wms.busine.tactic.dao;

import com.deer.wms.busine.tactic.model.PrintIp;
import com.deer.wms.busine.tactic.model.PrintIpCriteria;
import com.deer.wms.busine.tactic.model.PrintIpDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PrintIpMapper extends Mapper<PrintIp> {

    List<PrintIpDto> findList(PrintIpCriteria criteria);
     PrintIpDto  findByIPId(@Param("id") Integer id);
}
