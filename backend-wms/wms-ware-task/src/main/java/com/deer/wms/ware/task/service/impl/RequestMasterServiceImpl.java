package com.deer.wms.ware.task.service.impl;

import com.deer.wms.ware.task.dao.RequestMasterMapper;
import com.deer.wms.ware.task.model.RequestMaster;
import com.deer.wms.ware.task.service.RequestMasterService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by guo on 2020/04/19.
 */
@Service
@Transactional
public class RequestMasterServiceImpl extends AbstractService<RequestMaster, Integer> implements RequestMasterService {

    @Autowired
    private RequestMasterMapper requestMasterMapper;

}
