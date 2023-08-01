package com.deer.wms.ureport.service.impl;

import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.ureport.dao.UreportFileMapper;
import com.deer.wms.ureport.model.UreportFile;
import com.deer.wms.ureport.service.UreportFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2020/01/07.
 */
@Service
@Transactional
public class UreportServiceImpl extends AbstractService<UreportFile, Integer> implements UreportFileService {

    @Autowired
    private UreportFileMapper ureportFileMapper;

}
