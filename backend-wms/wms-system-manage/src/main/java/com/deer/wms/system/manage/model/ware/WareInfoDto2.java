package com.deer.wms.system.manage.model.ware;

import lombok.ToString;

@ToString
public class WareInfoDto2 extends WareInfo {
    private String dutyUserName;
    private String linkPhone;

    public String getDutyUserName() {
        return dutyUserName;
    }

    public void setDutyUserName(String dutyUserName) {
        this.dutyUserName = dutyUserName;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }
}
