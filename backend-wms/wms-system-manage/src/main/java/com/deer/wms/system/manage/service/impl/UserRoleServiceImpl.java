package com.deer.wms.system.manage.service.impl;

import com.deer.wms.system.manage.dao.UserRoleMapper;
import com.deer.wms.system.manage.model.UserRole;
import com.deer.wms.system.manage.model.UserRoleCriteria;
import com.deer.wms.system.manage.model.user.UserRoleDto;
import com.deer.wms.system.manage.service.UserRoleService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by guo on 2018/8/11.
 */
@Service
@Transactional
public class UserRoleServiceImpl extends AbstractService<UserRole, Integer> implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<UserRoleDto> findList(UserRoleCriteria criteria) {
        return userRoleMapper.findList(criteria);
    }

    @Override
    public void addUserRole(Integer userId, Integer roleId) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRole.setCreateTime(new Date());
        super.save(userRole);
    }

    @Override
    public void deleteByUserId(Integer id) {
        userRoleMapper.deleteByUserId(id);
    }
}
