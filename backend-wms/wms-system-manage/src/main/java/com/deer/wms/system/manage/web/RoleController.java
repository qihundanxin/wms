package com.deer.wms.system.manage.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.StringUtil;
import com.deer.wms.system.manage.model.UserRole;
import com.deer.wms.system.manage.model.UserRoleCriteria;
import com.deer.wms.system.manage.model.permission.PermissionTreeModel;
import com.deer.wms.system.manage.model.role.Role;
import com.deer.wms.system.manage.model.role.RoleCreate;
import com.deer.wms.system.manage.model.role.RoleCriteria;
import com.deer.wms.system.manage.model.role.RoleDetailVO;
import com.deer.wms.system.manage.model.role.RoleListVO;
import com.deer.wms.system.manage.model.role.RoleModify;
import com.deer.wms.system.manage.model.user.UserRoleDto;
import com.deer.wms.system.manage.service.PermissionService;
import com.deer.wms.system.manage.service.RoleService;
import com.deer.wms.system.manage.service.UserRoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 角色信息api接口
 * 
 * Created by guo on 2017/10/07.
 */
@Authority
@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 删除角色信息
     *
     * @param
     * @param
     * @return
     */
    @GetMapping("/delete")
    @Authority("roles_delete")
    public Result delete(Integer roleId) {
        UserRoleCriteria criteria = new UserRoleCriteria();
        criteria.setRoleId(roleId);
        List<UserRoleDto> userRoleDtos = userRoleService.findList(criteria);
        for (UserRole userRole : userRoleDtos) {
            Integer userRoles = userRole.getRoleId();
            if (userRoles.equals(roleId)) {
                throw new ServiceException(CommonCode.ROLE_BOUND);
            }
        }
        roleService.deleteById(roleId);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 添加角色信息
     *
     * @param create
     * @param currentUser
     * @return
     */

    @PostMapping
    @Authority("roles_add")
    public Result save(@RequestBody RoleCreate create, @User CurrentUser currentUser) {
        create.setRoleLevel(1);
        Role role = roleService.findBy("roleName", create.getRoleName());
        if (role != null) {
            throw new ServiceException(CommonCode.ROLE_EXISTS);
        }
        roleService.addRole(create, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 修改角色信息 修改角色基本信息和权限信息
     *
     * @param modify      角色信息
     * @param currentUser 当前操作用户信息
     * @return
     */
    @PutMapping("/{roleId}")
    @Authority("roles_update")
    public Result modify(@PathVariable Integer roleId, @RequestBody RoleModify modify, @User CurrentUser currentUser) {

        Role role = roleService.findById(roleId);
        if (role != null) {
            roleService.modifyRole(modify, currentUser);
            return ResultGenerator.genSuccessResult();
        } else {
            throw new ServiceException(CommonCode.ROLE_EXISTS);
        }
    }

    /**
     * 获取角色详细信息 获取角色详细信息，包括角色基本信息和具有的权限信息
     *
     * @param roleId 角色信息id
     * @return
     */
    @GetMapping("/{roleId}")
    @Authority("roles_get")
    public Result detail(@PathVariable Integer roleId) {

        RoleDetailVO vo = roleService.findRoleByRoleId(roleId);
        return ResultGenerator.genSuccessResult(vo);
    }

    /**
     * 获取指定角色的权限信息
     *
     * @return 角色权限信息
     */
    @GetMapping("/{roleId}/permission")
    @Authority("roles_permission")
    public Result getRolePermission(@PathVariable Integer roleId) {
        List<PermissionTreeModel> treeModels = permissionService.findPermissionRangeTreeFormListByRoleId(roleId);
        return ResultGenerator.genSuccessResult(treeModels);
    }

    /**
     * 获取角色信息
     *
     * @param criteria 查询条件
     * @return
     */
    @GetMapping("/list")
    @Authority("roles_list")
    public Result list(RoleCriteria criteria, @User CurrentUser currentUser) {

        criteria.setUserId(currentUser.getUserId());

        List<RoleListVO> list = roleService.findRoleFormList(criteria);
        return ResultGenerator.genSuccessResult(list);
    }

    /**
     * 分页获取角色信息
     *
     * @param criteria 查询条件
     * @return
     */
    @GetMapping
    @Authority("roles_pagingList")
    public Result pagingList(RoleCriteria criteria, @User CurrentUser currentUser) {
        StringUtil.trimObjectStringProperties(criteria);
//        boolean isOperate = currentUser.getCompanyType() == SystemManageConstant.COMPANY_TYPE_OPERATE;
//        criteria.setUserId(isOperate ? null : currentUser.getUserId());
//        criteria.setCompanyId(isOperate ? null : currentUser.getCompanyId());

        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<RoleListVO> list = roleService.findRoleFormList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
