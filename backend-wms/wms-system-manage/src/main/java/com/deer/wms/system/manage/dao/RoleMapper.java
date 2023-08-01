package com.deer.wms.system.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.UserRole;
import com.deer.wms.system.manage.model.role.Role;
import com.deer.wms.system.manage.model.role.RoleCriteria;
import com.deer.wms.system.manage.model.role.RoleDetailVO;
import com.deer.wms.system.manage.model.role.RoleListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends Mapper<Role> {

    /**
     * 查找角色信息列表
     *
     * @param roleCriteria 查询条件
     * @return 角色信息列表
     */
    List<RoleListVO> selectRoleFormList(RoleCriteria roleCriteria);

    /**
     * 查找指定角色的详细信息
     *
     * @param roleId 角色信息id
     * @return 角色详细信息，如果没有找到返回null
     */
    RoleDetailVO selectRoleByRoleId(@Param("roleId") Integer roleId);

    List<Role> findByRoleName(String roleName);

    /**
     * @Author luolin
     * @Description 根据角色id查询具有该角色的所有用户
     * @Date 14:18 2023/6/30
     * @Param
     * @param roleId
     * @return
     * @return java.util.List<com.deer.wms.system.manage.model.UserRole>
     **/
    List<UserRole> selectUserRoleByRoleId(Integer roleId);
}