package com.deer.wms.system.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.busine.tactic.service.UpperTacticService;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.constant.CacheKey;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.CacheTool;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.system.manage.dao.OrganizationMapper;
import com.deer.wms.system.manage.model.organization.Organization;
import com.deer.wms.system.manage.model.organization.OrganizationCriteria;
import com.deer.wms.system.manage.model.organization.OrganizationDto;
import com.deer.wms.system.manage.model.organization.UserOrganization;
import com.deer.wms.system.manage.service.OrganizationService;
import com.deer.wms.system.manage.service.OrganizationTacticService;
import com.deer.wms.system.manage.service.UserOrganizationService;

/**
 * Created by guo on 2019/11/05.
 */
@Service
@Transactional
public class OrganizationServiceImpl extends AbstractService<Organization, Integer> implements OrganizationService {

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private UserOrganizationService userOrganizationService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UpperTacticService upperTacticService;

    @Autowired
    private OrganizationTacticService organizationTacticService;

    @Autowired
    private CacheTool cache;

    @Override
    public List<OrganizationDto> findList(OrganizationCriteria criteria) {
        return organizationMapper.findList(criteria);
    }

    @Override
    public List<OrganizationDto> findByOrganizationCode(String organizationCode) {
        return organizationMapper.findByOrganizationCode(organizationCode);
    }

    @Override
    public List<OrganizationDto> findByOrganizationName(String organizationName) {
        return organizationMapper.findByOrganizationName(organizationName);
    }

    @Override
    public void deleteByCodeAndCom(OrganizationCriteria criteria1) {
        organizationMapper.deleteByCodeAndCom(criteria1);
    }

    @Override
    public Result createOrganization(OrganizationDto organization, CurrentUser currentUser) {

        /**
         * 新建货主
         */
        List<OrganizationDto> list = organizationService.findByOrganizationCode(organization.getOrganizationCode());
        List<OrganizationDto> list1 = organizationService.findByOrganizationName(organization.getOrganizationName());

        if (list.size() == 0 && list1.size() == 0) {
            String nowDate = DateUtils.getNowDateTimeString();
            organization.setCreateTime(nowDate);
            save(organization);
            UserOrganization userOrganization = new UserOrganization();
            userOrganization.setOrganizationId(organization.getOrganizationId());
            userOrganization.setUserId(currentUser.getUserId());
            userOrganizationService.save(userOrganization);
//            List<UpperTactic> list2 = upperTacticService.findByUpperTacticCode(organization.getUpperTacticCode());
//            OrganizationTactic organizationTactic = new OrganizationTactic();
//            for (int i = 0; list2.size() < i; i++) {
//                organizationTactic.setOrganizationId(organization.getOrganizationId());
//                organizationTactic.setUpperTacticCode(list2.get(i).getUpperTacticCode());
//                organizationTactic.setUpperTacticName(list2.get(i).getUpperTacticName());
//                organizationTactic.setWareId(list2.get(i).getWareId());
//                organizationTacticService.save(organizationTactic);
//            }
            return ResultGenerator.genSuccessResult();
        } else if (list.size() == 0 && list1.size() != 0) {
            throw new ServiceException(CommonCode.ORGANIZATION_EXISTS, "此货主已存在");
        } else {
            throw new ServiceException(CommonCode.ORGANIZATION_CODE_EXISTS, "此货主编码已存在");

        }
    }

    @Override
    public void modifyUserInfoAccountState(Integer organizationId, String sate, CurrentUser currentUser) {
        Organization organization = new Organization();
        organization.setOrganizationId(organizationId);
        organization.setOrganizationStatus(sate);
        super.update(organization);
    }

    @Override
    public void save(Organization organization) {
        super.save(organization);
        cache.del(CacheKey.ALL_ORGANIZATION);
    }

    @Override
    public void deleteById(Integer organizationId) {
        super.deleteById(organizationId);
        cache.del(CacheKey.ALL_ORGANIZATION);
    }

    @Override
    public int update(Organization organization) {
        int update = super.update(organization);
        cache.del(CacheKey.ALL_ORGANIZATION);
        return update;
    }

}
