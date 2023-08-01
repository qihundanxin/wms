package com.deer.wms.system.manage.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.constant.CacheKey;
import com.deer.wms.project.root.constant.CacheTime;
import com.deer.wms.project.root.constant.Constants;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.CacheTool;
import com.deer.wms.system.manage.model.organization.Organization;
import com.deer.wms.system.manage.model.organization.OrganizationCriteria;
import com.deer.wms.system.manage.model.organization.OrganizationDto;
import com.deer.wms.system.manage.model.organization.UserOrganization;
import com.deer.wms.system.manage.model.organization.UserOrganizationCriteria;
import com.deer.wms.system.manage.model.organization.UserOrganizationDto;
import com.deer.wms.system.manage.service.OrganizationService;
import com.deer.wms.system.manage.service.UserOrganizationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 货主信息api接口
 * 
 * Created by guo on 2019/11/05.
 */
@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserOrganizationService userOrganizationService;

    @Autowired
    private CacheTool cache;

    /**
     * 添加货主信息
     */
    @PostMapping("/insert")
    @Authority("organizations_insert")
    public Result add(@RequestBody OrganizationDto organization, @User CurrentUser currentUser) {
        organizationService.createOrganization(organization, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 删除货主信息
     */
    @Transactional
    @GetMapping("/delete")
    @Authority("organizations_delete")
    public Result delete(Integer organizationId, @User CurrentUser currentUser) {
        UserOrganizationCriteria criteria = new UserOrganizationCriteria();
        criteria.setOrganizationId(organizationId);
        List<UserOrganizationDto> userOrganizations = userOrganizationService.findList(criteria);
        for (UserOrganization userOrganization : userOrganizations) {
            Integer currentUserOrganization = userOrganization.getOrganizationId();
            if (currentUserOrganization.equals(organizationId)) {
                throw new ServiceException(CommonCode.ORGANIZATION_BOUND);
            }
        }
        OrganizationCriteria criteria1 = new OrganizationCriteria();
        criteria1.setOrganizationId(organizationId);
        organizationService.deleteByCodeAndCom(criteria1);
        userOrganizationService.deleteByOrganizationId(organizationId);
        return ResultGenerator.genSuccessResult();
    }

    @Transactional
    @PostMapping("/update")
    @Authority("organizations_update")
    public Result update(@RequestBody Organization organization) {

        List<OrganizationDto> list = organizationService.findByOrganizationCode(organization.getOrganizationCode());
        if (list.size() == 0) {
            organizationService.update(organization);
        } else if (list.size() == 1) {
            if (list.get(0).getOrganizationId().equals(organization.getOrganizationId())) {
                List<OrganizationDto> list1 = organizationService.findByOrganizationName(organization.getOrganizationName());
                if (list1.size() == 0) {
                    organizationService.update(organization);
                } else if (list1.size() == 1 && list1.get(0).getOrganizationId().equals(organization.getOrganizationId())) {
                    organizationService.update(organization);
                } else {
                    throw new ServiceException(CommonCode.ORGANIZATION_EXISTS, "此货主已存在");
                }

            } else if (!list.get(0).getOrganizationId().equals(organization.getOrganizationId())) {

                throw new ServiceException(CommonCode.ORGANIZATION_CODE_EXISTS, "此货主编码已存在");
            }
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 单个货主查询
     */
    @GetMapping("/detail")
    @Authority("organizations_detail")
    public Result detail(Integer id) {
        Organization organization = organizationService.findById(id);
        return ResultGenerator.genSuccessResult(organization);
    }

    @Transactional
    @GetMapping("/list")
    @Authority("organizations_list")
    public Result list(OrganizationCriteria criteria, @User CurrentUser currentUser) {
        if (currentUser.getIsAdmin() == 1) {
            criteria.setUserId(null);
        } else {
            criteria.setUserId(currentUser.getUserId());
        }
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<OrganizationDto> list = organizationService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Transactional
    @GetMapping("/allList")
    @Authority("organizations_allList")
    public Result allList(@User CurrentUser currentUser) {

        List<Organization> list = cache.getObject(CacheKey.ALL_ORGANIZATION, null);
        if (list == null || list.size() == 0) {
            list = organizationService.findAll();
            cache.setObject(CacheKey.ALL_ORGANIZATION, null, list, CacheTime.ALL_ORGANIZATION);
        }

        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 启用货主
     */
    @GetMapping("/enable")
    @Authority("organizations_enable")
    public Result enable(Integer organizationId, @User CurrentUser currentUser) {
        Organization organization = organizationService.findById(organizationId);

        organizationService.modifyUserInfoAccountState(organizationId, Constants.ACCOUNT_STATUS_ENABLE, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 停用货主
     */
    @GetMapping("/disable")
    @Authority("organizations_disable")
    public Result disable(Integer organizationId, @User CurrentUser currentUser) {
        Organization organization = organizationService.findById(organizationId);

        organizationService.modifyUserInfoAccountState(organizationId, Constants.ACCOUNT_STATUS_DISABLE, currentUser);
        return ResultGenerator.genSuccessResult();
    }

}
