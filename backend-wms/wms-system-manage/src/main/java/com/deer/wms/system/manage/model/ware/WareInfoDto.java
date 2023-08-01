package com.deer.wms.system.manage.model.ware;

import java.util.List;

import lombok.ToString;

@ToString
public class WareInfoDto extends WareInfo {

    private String dutyUserName;
    private String linkPhone;

    List<Integer> wareUserIds;

    List<String> wareUserNames;

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

    public List<Integer> getWareUserIds() {
        return wareUserIds;
    }

    public void setWareUserIds(List<Integer> wareUserIds) {
        this.wareUserIds = wareUserIds;
    }

    public List<String> getWareUserNames() {
        return wareUserNames;
    }

    public void setWareUserNames(List<String> wareUserNames) {
        this.wareUserNames = wareUserNames;
    }
}
