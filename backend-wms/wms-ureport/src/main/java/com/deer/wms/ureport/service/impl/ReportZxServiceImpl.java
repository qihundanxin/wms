package com.deer.wms.ureport.service.impl;

import com.deer.wms.ureport.dao.ReportZxMapper;
import com.deer.wms.ureport.model.ReportZx;
import com.deer.wms.ureport.service.ReportZxService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by  on 2021/01/24.
 */
@Service
@Transactional
public class ReportZxServiceImpl extends AbstractService<ReportZx, Integer> implements ReportZxService {

    @Autowired
    private ReportZxMapper reportZxMapper;

}
