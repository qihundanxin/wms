package com.deer.wms.system.manage.service;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.system.manage.model.organization.Organization;
import com.deer.wms.system.manage.model.organization.OrganizationCriteria;
import com.deer.wms.system.manage.model.organization.OrganizationDto;

import java.util.List;

/**
 * Created by guo on 2019/11/05.
 */
public interface OrganizationService extends Service<Organization, Integer> {
    List<OrganizationDto> findList(OrganizationCriteria criteria);

    List<OrganizationDto> findByOrganizationCode(String organizationCode);

    List<OrganizationDto> findByOrganizationName(String organizationName);

    public Result createOrganization(OrganizationDto organization, CurrentUser currentUser);

    void deleteByCodeAndCom(OrganizationCriteria criteria1);

    /**
     * 修改货主状态
     *
     * @param organizationId 用户信息id
     * @param sate           用户信息状态
     * @param currentUser    当前操作人员的信息
     */
    void modifyUserInfoAccountState(Integer organizationId, String sate, CurrentUser currentUser);

}
