package com.deer.wms.system.manage.service;

import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.system.manage.model.organization.UserOrganization;
import com.deer.wms.system.manage.model.organization.UserOrganizationCriteria;
import com.deer.wms.system.manage.model.organization.UserOrganizationDto;

import java.util.List;

/**
 * Created by guo on 2019/11/05.
 */
public interface UserOrganizationService extends Service<UserOrganization, Integer> {
    List<UserOrganizationDto> findList(UserOrganizationCriteria criteria);

    void addUserOrganization(Integer userId, Integer organizationId);

    void deleteByUserId(Integer userId);

    void deleteByOrganizationId(Integer organizationId);
}
