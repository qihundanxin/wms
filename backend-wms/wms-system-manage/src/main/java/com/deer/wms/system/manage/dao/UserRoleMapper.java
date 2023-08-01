package com.deer.wms.system.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.UserRole;
import com.deer.wms.system.manage.model.UserRoleCriteria;
import com.deer.wms.system.manage.model.user.UserRoleDto;

import java.util.List;

public interface UserRoleMapper extends Mapper<UserRole> {

    List<UserRoleDto> findList(UserRoleCriteria criteria);

    void deleteByUserId(Integer userId);
}