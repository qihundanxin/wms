package com.deer.wms.ureport.service.impl;

import com.deer.wms.ureport.dao.ReportOperateDetailMapper;
import com.deer.wms.ureport.model.ReportOperateDetail;
import com.deer.wms.ureport.model.ReportOperateDetailCriteria;
import com.deer.wms.ureport.service.ReportOperateDetailService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by  on 2021/10/27.
 */
@Service
@Transactional
public class ReportOperateDetailServiceImpl extends AbstractService<ReportOperateDetail, Integer> implements ReportOperateDetailService {

    @Autowired
    private ReportOperateDetailMapper reportOperateDetailMapper;

    @Override
    public List<ReportOperateDetail> findList(ReportOperateDetailCriteria criteria) {
        return reportOperateDetailMapper.findList(criteria);
    }
}
