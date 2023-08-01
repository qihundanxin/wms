package com.deer.wms.ware.task.service.impl;

import com.deer.wms.ware.task.dao.RequestDetailMapper;
import com.deer.wms.ware.task.model.RequestDetail;
import com.deer.wms.ware.task.service.RequestDetailService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by guo on 2020/04/19.
 */
@Service
@Transactional
public class RequestDetailServiceImpl extends AbstractService<RequestDetail, Integer> implements RequestDetailService {

    @Autowired
    private RequestDetailMapper requestDetailMapper;

}
