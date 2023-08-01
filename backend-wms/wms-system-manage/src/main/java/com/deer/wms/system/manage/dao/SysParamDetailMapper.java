package com.deer.wms.system.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.SysParamDetail;
import com.deer.wms.system.manage.model.SysParamDetailCriteria;

import java.util.List;

public interface SysParamDetailMapper extends Mapper<SysParamDetail> {

    List<SysParamDetail> findList(SysParamDetailCriteria criteria);

    void deleteBySysParamId(Integer SysParamId);
}