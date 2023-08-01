package com.deer.wms.ureport.service.impl;

import com.deer.wms.ureport.dao.ReportAcceptMapper;
import com.deer.wms.ureport.model.ReportAccept;
import com.deer.wms.ureport.service.ReportAcceptService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by  on 2021/01/24.
 */
@Service
@Transactional
public class ReportAcceptServiceImpl extends AbstractService<ReportAccept, Integer> implements ReportAcceptService {

    @Autowired
    private ReportAcceptMapper reportAcceptMapper;

}
