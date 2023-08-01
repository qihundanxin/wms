package com.deer.wms.system.manage.service;

import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.system.manage.model.UserRole;
import com.deer.wms.system.manage.model.UserRoleCriteria;
import com.deer.wms.system.manage.model.user.UserRoleDto;

import java.util.List;

/**
 * Created by guo on 2018/8/11.
 */
public interface UserRoleService extends Service<UserRole, Integer> {
    List<UserRoleDto> findList(UserRoleCriteria criteria);

    /**
     * 添加用户角色信息
     *
     * @param userId 用户信息id
     * @param roleId 角色信息id
     */
    void addUserRole(Integer userId, Integer roleId);

    void deleteByUserId(Integer userId);

}
