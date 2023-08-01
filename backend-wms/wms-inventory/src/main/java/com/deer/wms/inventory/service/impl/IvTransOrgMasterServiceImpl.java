package com.deer.wms.inventory.service.impl;

import com.deer.wms.inventory.dao.IvTransOrgMasterMapper;
import com.deer.wms.inventory.model.IvTransOrgMaster;
import com.deer.wms.inventory.model.IvTransOrgMasterCriteria;
import com.deer.wms.inventory.service.IvTransOrgMasterService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ll on 2020/03/13.
 */
@Service
@Transactional
public class IvTransOrgMasterServiceImpl extends AbstractService<IvTransOrgMaster, Integer> implements IvTransOrgMasterService {

    @Autowired
    private IvTransOrgMasterMapper ivTransOrgMasterMapper;

    @Override
    public List<IvTransOrgMaster> findList(IvTransOrgMasterCriteria criteria) {
        return ivTransOrgMasterMapper.findList(criteria);
    }
}
