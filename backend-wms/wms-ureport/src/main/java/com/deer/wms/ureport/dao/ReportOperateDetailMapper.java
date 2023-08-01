package com.deer.wms.ureport.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ureport.model.ReportOperateDetail;
import com.deer.wms.ureport.model.ReportOperateDetailCriteria;

import java.util.List;

public interface ReportOperateDetailMapper extends Mapper<ReportOperateDetail> {

    List<ReportOperateDetail> findList(ReportOperateDetailCriteria criteria);
}