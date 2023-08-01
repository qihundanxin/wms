package com.deer.wms.system.manage.model.role;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2018/8/11.
 */
@ToString
public class RoleCriteria extends QueryCriteria {
    /**
     * 角色层级
     */
    private Integer roleLevel;

    /**
     * 用户id
     */
    private Integer userId;

    public Integer getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(Integer roleLevel) {
        this.roleLevel = roleLevel;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
