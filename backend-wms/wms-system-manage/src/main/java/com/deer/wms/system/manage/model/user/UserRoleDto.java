package com.deer.wms.system.manage.model.user;

import com.deer.wms.system.manage.model.UserRole;

import lombok.ToString;

@ToString
public class UserRoleDto extends UserRole {
    private String roleName;

    private String userName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
