package com.deer.wms.system.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.organization.UserOrganization;
import com.deer.wms.system.manage.model.organization.UserOrganizationCriteria;
import com.deer.wms.system.manage.model.organization.UserOrganizationDto;

import java.util.List;

public interface UserOrganizationMapper extends Mapper<UserOrganization> {

    List<UserOrganizationDto> findList(UserOrganizationCriteria criteria);

    void deleteByUserId(Integer userId);

    void deleteByOrganizationId(Integer organizationId);
}