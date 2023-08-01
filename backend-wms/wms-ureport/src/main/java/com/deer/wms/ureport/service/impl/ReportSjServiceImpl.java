package com.deer.wms.ureport.service.impl;

import com.deer.wms.ureport.dao.ReportSjMapper;
import com.deer.wms.ureport.model.ReportSj;
import com.deer.wms.ureport.service.ReportSjService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by  on 2021/01/24.
 */
@Service
@Transactional
public class ReportSjServiceImpl extends AbstractService<ReportSj, Integer> implements ReportSjService {

    @Autowired
    private ReportSjMapper reportSjMapper;

}
