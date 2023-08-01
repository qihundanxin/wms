package com.deer.wms.system.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.organization.OrganizationTactic;
import com.deer.wms.system.manage.model.organization.OrganizationTacticCriteria;
import com.deer.wms.system.manage.model.organization.OrganizationTacticDto;

import java.util.List;

public interface OrganizationTacticMapper extends Mapper<OrganizationTactic> {

    List<OrganizationTacticDto> findList(OrganizationTacticCriteria criteria);
}