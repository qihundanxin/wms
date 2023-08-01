package com.deer.wms.system.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.system.manage.dao.UserOrganizationMapper;
import com.deer.wms.system.manage.model.organization.Organization;
import com.deer.wms.system.manage.model.organization.UserOrganization;
import com.deer.wms.system.manage.model.organization.UserOrganizationCriteria;
import com.deer.wms.system.manage.model.organization.UserOrganizationDto;
import com.deer.wms.system.manage.service.OrganizationService;
import com.deer.wms.system.manage.service.UserOrganizationService;

/**
 * Created by guo on 2019/11/05.
 */
@Service
@Transactional
public class UserOrganizationServiceImpl extends AbstractService<UserOrganization, Integer> implements UserOrganizationService {

    @Autowired
    private UserOrganizationMapper userOrganizationMapper;

    @Autowired
    private OrganizationService organizationService;

    @Override
    public List<UserOrganizationDto> findList(UserOrganizationCriteria criteria) {
        return userOrganizationMapper.findList(criteria);
    }

    @Override
    public void addUserOrganization(Integer userId, Integer organizationId) {
        UserOrganization userOrganization = new UserOrganization();
        userOrganization.setUserId(userId);
        userOrganization.setOrganizationId(organizationId);
        super.save(userOrganization);
        Organization organization = organizationService.findById(organizationId);
        organization.setOrganizationStatus("enable");
        organizationService.update(organization);
    }

    @Override
    public void deleteByUserId(Integer userId) {
        userOrganizationMapper.deleteByUserId(userId);
    }

    @Override
    public void deleteByOrganizationId(Integer organizationId) {
        userOrganizationMapper.deleteByOrganizationId(organizationId);
    }

}
