package com.deer.wms.ureport.service.impl;

import com.deer.wms.ureport.dao.ReportPdMapper;
import com.deer.wms.ureport.model.ReportPd;
import com.deer.wms.ureport.service.ReportPdService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by  on 2021/01/24.
 */
@Service
@Transactional
public class ReportPdServiceImpl extends AbstractService<ReportPd, Integer> implements ReportPdService {

    @Autowired
    private ReportPdMapper reportPdMapper;

}
