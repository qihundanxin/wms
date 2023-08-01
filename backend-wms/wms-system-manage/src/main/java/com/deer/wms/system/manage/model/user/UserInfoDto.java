package com.deer.wms.system.manage.model.user;

import lombok.ToString;

@ToString
public class UserInfoDto extends UserInfo {

    private String deptName;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
