package com.deer.wms.inventory.service.impl;

import com.deer.wms.inventory.dao.IvTransOrgDetailMapper;
import com.deer.wms.inventory.model.IvTransOrgDetail;
import com.deer.wms.inventory.model.IvTransOrgDetailCriteria;
import com.deer.wms.inventory.service.IvTransOrgDetailService;

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
public class IvTransOrgDetailServiceImpl extends AbstractService<IvTransOrgDetail, Integer> implements IvTransOrgDetailService {

    @Autowired
    private IvTransOrgDetailMapper ivTransOrgDetailMapper;

    @Override
    public List<IvTransOrgDetail> findList(IvTransOrgDetailCriteria criteria) {
        return ivTransOrgDetailMapper.findList(criteria);
    }
}
