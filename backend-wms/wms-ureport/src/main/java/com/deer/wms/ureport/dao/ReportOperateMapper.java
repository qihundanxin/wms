package com.deer.wms.ureport.dao;

import com.deer.wms.ureport.model.ReportOperate;
import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ureport.model.ReportOperateCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReportOperateMapper extends Mapper<ReportOperate> {

    ReportOperate findNow(@Param("wareId") Integer wareId, @Param("userId") Integer userId, @Param("type")Integer type);

    List<ReportOperate> findList(ReportOperateCriteria criteria);
}