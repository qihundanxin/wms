package com.deer.wms.ureport.service.impl;

import com.deer.wms.ureport.dao.ReportDbMapper;
import com.deer.wms.ureport.model.ReportDb;
import com.deer.wms.ureport.service.ReportDbService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by  on 2021/01/24.
 */
@Service
@Transactional
public class ReportDbServiceImpl extends AbstractService<ReportDb, Integer> implements ReportDbService {

    @Autowired
    private ReportDbMapper reportDbMapper;

}
