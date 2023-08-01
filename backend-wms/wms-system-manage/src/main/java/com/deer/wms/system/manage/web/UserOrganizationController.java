package com.deer.wms.system.manage.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.system.manage.model.organization.UserOrganization;
import com.deer.wms.system.manage.model.organization.UserOrganizationCriteria;
import com.deer.wms.system.manage.model.organization.UserOrganizationDto;
import com.deer.wms.system.manage.service.UserOrganizationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 用户货主关联信息api接口
 * 
 * Created by guo on 2019/11/05.
 */
@RestController
@RequestMapping("/user/organizations")
public class UserOrganizationController {

    @Autowired
    private UserOrganizationService userOrganizationService;

    @PostMapping("/insert")
    @Authority("user_organizations_insert")
    public Result add(@RequestBody UserOrganization userOrganization) {
        userOrganizationService.save(userOrganization);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("user_organizations_delete")
    public Result delete(@PathVariable Integer id) {
        userOrganizationService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("user_organizations_update")
    public Result update(@RequestBody UserOrganization userOrganization) {
        userOrganizationService.update(userOrganization);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("user_organizations_get")
    public Result detail(@PathVariable Integer id) {
        UserOrganization userOrganization = userOrganizationService.findById(id);
        return ResultGenerator.genSuccessResult(userOrganization);
    }

    @GetMapping("/list")
    @Authority("user_organizations_list")
    public Result list(UserOrganizationCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<UserOrganizationDto> list = userOrganizationService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
