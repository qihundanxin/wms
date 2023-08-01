package com.deer.wms.system.manage.model.organization;

import lombok.ToString;

@ToString
public class UserOrganizationDto extends UserOrganization {
    private String userName;
    private String organizationName;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
