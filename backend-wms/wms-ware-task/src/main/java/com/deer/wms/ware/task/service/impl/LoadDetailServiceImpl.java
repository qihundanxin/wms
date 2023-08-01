package com.deer.wms.ware.task.service.impl;

import com.deer.wms.ware.task.dao.LoadDetailMapper;
import com.deer.wms.ware.task.model.Load.LoadDetail;
import com.deer.wms.ware.task.model.Load.LoadDetailCriteria;
import com.deer.wms.ware.task.model.Load.LoadDetailDto;
import com.deer.wms.ware.task.service.LoadDetailService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ll on 2020/02/17.
 */
@Service
@Transactional
public class LoadDetailServiceImpl extends AbstractService<LoadDetail, Integer> implements LoadDetailService {

    @Autowired
    private LoadDetailMapper loadDetailMapper;

    @Override
    public List<LoadDetailDto> findList(LoadDetailCriteria criteria) {
        return loadDetailMapper.findList(criteria);
    }
}
