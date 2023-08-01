package com.deer.wms.system.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.SysParam;
import com.deer.wms.system.manage.model.SysParamCriteria;
import com.deer.wms.system.manage.model.SysParamDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysParamMapper extends Mapper<SysParam> {

    List<SysParamDto> findList(SysParamCriteria criteria);

    List<SysParamDto> findByCode(@Param("paramCode") String paramCode);

    List<SysParamDto> findByName(@Param("paramName") String paramName);

    List<SysParam> findByMemo(@Param("memo") String memo);
}