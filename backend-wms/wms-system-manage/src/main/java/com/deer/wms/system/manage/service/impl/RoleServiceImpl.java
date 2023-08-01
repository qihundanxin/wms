package com.deer.wms.system.manage.service.impl;

import com.deer.wms.intercept.common.data.CommonDataService;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.system.manage.dao.RoleMapper;
import com.deer.wms.system.manage.model.UserRole;
import com.deer.wms.system.manage.model.permission.Permission;
import com.deer.wms.system.manage.model.permission.PermissionModify;
import com.deer.wms.system.manage.model.role.*;
import com.deer.wms.system.manage.model.user.UserInfo;
import com.deer.wms.system.manage.service.PermissionService;
import com.deer.wms.system.manage.service.RoleService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by guo on 2018/8/11.
 */
@Service
@Transactional
public class RoleServiceImpl extends AbstractService<Role, Integer> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private CommonDataService commonDataService;

    @Override
    public List<RoleListVO> findRoleFormList(RoleCriteria criteria) {
        List<RoleListVO> voList = roleMapper.selectRoleFormList(criteria);
        return voList;
    }

    @Override
    public RoleDetailVO findRoleByRoleId(Integer roleId) {
        RoleDetailVO vo = roleMapper.selectRoleByRoleId(roleId);
        vo.setTreeList(permissionService.findPermissionTreeFormListByRoleId(roleId));
        return vo;
    }

    @Override
    public void addRole(RoleCreate create, CurrentUser currentUser) {
        //修改角色信息
        Role role = new Role();
        BeanUtils.copyProperties(create, role);
        role.setCreateUserId(currentUser.getUserId());
        role.setCreateTime(new Date());
//        Integer companyId = create.getCompanyId();
//        if(companyId==null){
//            role.setCompanyId(currentUser.getCompanyId());
//        }
        super.save(role);
        permissions(role.getRoleId(), create.getPermissions());
    }

    @Override
    public void modifyRole(RoleModify modify, CurrentUser currentUser) {
        //修改角色信息
        Role role = new Role();
        BeanUtils.copyProperties(modify, role);
        role.setModifyUserId(currentUser.getUserId());
        role.setModifyTime(new Date());
        super.update(role);

        //处理角色权限信息，为了简单，先删除角色所有权限信息，再新增角色权限
        permissionService.deleteByRoleId(modify.getRoleId());
        permissions(null, modify.getPermissions());
        List<UserRole> userRoleList = roleMapper.selectUserRoleByRoleId(modify.getRoleId());
        for (UserRole ur : userRoleList){
            commonDataService.removeAuthDataFromRedis(String.valueOf(ur.getUserId()));
        }
    }

    @Override
    public List<Role> findByRoleName(String roleName) {
        return roleMapper.findByRoleName(roleName);
    }

    private void permissions(Integer roleId, List<PermissionModify> permissions) {
        if (permissions == null) {
            return;
        } else if (permissions.size() == 0) {
            return;
        }
        permissions.forEach(data -> {
            Permission permission = new Permission();
            BeanUtils.copyProperties(data, permission);
            if (null != roleId) {
                permission.setObjectId(roleId);
            }
            permissionService.save(permission);
        });

    }
}
