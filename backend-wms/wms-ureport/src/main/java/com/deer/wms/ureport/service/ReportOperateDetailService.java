package com.deer.wms.ureport.service;

import com.deer.wms.ureport.model.ReportOperateDetail;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ureport.model.ReportOperateDetailCriteria;

import java.util.List;

/**
 * Created by  on 2021/10/27.
 */
public interface ReportOperateDetailService extends Service<ReportOperateDetail, Integer> {

    List<ReportOperateDetail> findList(ReportOperateDetailCriteria criteria);

}
