package com.deer.wms.ware.task.service.impl;

import com.deer.wms.ware.task.dao.LoadMasterMapper;
import com.deer.wms.ware.task.model.Load.LoadMaster;
import com.deer.wms.ware.task.model.Load.LoadMasterCriteria;
import com.deer.wms.ware.task.service.LoadMasterService;

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
public class LoadMasterServiceImpl extends AbstractService<LoadMaster, Integer> implements LoadMasterService {

    @Autowired
    private LoadMasterMapper loadMasterMapper;

    @Override
    public List<LoadMaster> findList(LoadMasterCriteria criteria) {
        return loadMasterMapper.findList(criteria);
    }
}
