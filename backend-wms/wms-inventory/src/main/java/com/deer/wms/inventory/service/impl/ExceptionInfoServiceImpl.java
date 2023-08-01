package com.deer.wms.inventory.service.impl;

import com.deer.wms.inventory.dao.ExceptionInfoMapper;
import com.deer.wms.inventory.model.ExceptionInfo;
import com.deer.wms.inventory.service.ExceptionInfoService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by  on 2020/10/28.
 */
@Service
@Transactional
public class ExceptionInfoServiceImpl extends AbstractService<ExceptionInfo, Integer> implements ExceptionInfoService {

    @Autowired
    private ExceptionInfoMapper exceptionInfoMapper;

}
