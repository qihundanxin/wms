package com.deer.wms.system.manage.service.impl;

import com.deer.wms.system.manage.dao.OrganizationTacticMapper;

import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.system.manage.model.organization.OrganizationTactic;
import com.deer.wms.system.manage.model.organization.OrganizationTacticCriteria;
import com.deer.wms.system.manage.model.organization.OrganizationTacticDto;
import com.deer.wms.system.manage.service.OrganizationTacticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2019/12/31.
 */
@Service
@Transactional
public class OrganizationTacticServiceImpl extends AbstractService<OrganizationTactic, Integer> implements OrganizationTacticService {

    @Autowired
    private OrganizationTacticMapper organizationTacticMapper;

    @Override
    public List<OrganizationTacticDto> findList(OrganizationTacticCriteria criteria) {
        return organizationTacticMapper.findList(criteria);
    }
}
