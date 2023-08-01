package com.deer.wms.ureport.service.impl;

import com.deer.wms.ureport.dao.ReportXjMapper;
import com.deer.wms.ureport.model.ReportXj;
import com.deer.wms.ureport.service.ReportXjService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by  on 2021/01/24.
 */
@Service
@Transactional
public class ReportXjServiceImpl extends AbstractService<ReportXj, Integer> implements ReportXjService {

    @Autowired
    private ReportXjMapper reportXjMapper;

}
