package com.deer.wms.system.manage.model.ware;

import lombok.ToString;

@ToString
public class WareUserDto extends WareUser {

    private String userName;

    private String wareName;

    private String createUserName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
}
