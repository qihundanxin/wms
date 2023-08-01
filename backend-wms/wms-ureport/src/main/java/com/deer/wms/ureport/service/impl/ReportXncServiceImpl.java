package com.deer.wms.ureport.service.impl;

import com.deer.wms.ureport.dao.ReportXncMapper;
import com.deer.wms.ureport.model.ReportXnc;
import com.deer.wms.ureport.service.ReportXncService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by  on 2021/01/24.
 */
@Service
@Transactional
public class ReportXncServiceImpl extends AbstractService<ReportXnc, Integer> implements ReportXncService {

    @Autowired
    private ReportXncMapper reportXncMapper;

}
