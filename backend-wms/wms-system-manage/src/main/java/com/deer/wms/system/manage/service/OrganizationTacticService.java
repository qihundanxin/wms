package com.deer.wms.system.manage.service;

import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.system.manage.model.organization.OrganizationTactic;
import com.deer.wms.system.manage.model.organization.OrganizationTacticCriteria;
import com.deer.wms.system.manage.model.organization.OrganizationTacticDto;

import java.util.List;

/**
 * Created by guo on 2019/12/31.
 */
public interface OrganizationTacticService extends Service<OrganizationTactic, Integer> {

    List<OrganizationTacticDto> findList(OrganizationTacticCriteria criteria);

}
