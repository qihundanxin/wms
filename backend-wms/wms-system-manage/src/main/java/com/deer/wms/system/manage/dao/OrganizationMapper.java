package com.deer.wms.system.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.organization.Organization;
import com.deer.wms.system.manage.model.organization.OrganizationCriteria;
import com.deer.wms.system.manage.model.organization.OrganizationDto;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrganizationMapper extends Mapper<Organization> {
    List<OrganizationDto> findList(OrganizationCriteria criteria);

    List<OrganizationDto> findByOrganizationCode(@Param("organizationCode") String organizationCode);

    List<OrganizationDto> findByOrganizationName(@Param("organizationName") String organizationName);

    List<Organization> findByBatchTacticCode(@Param("batchTacticCode") String batchTacticCode);

    void deleteByCodeAndCom(OrganizationCriteria criteria);

}