package com.deer.wms.system.manage.model;

import lombok.ToString;

/**
 * @author yang date 2021-03-13
 */
@ToString
public class ValidatorAreaInfoModel {
    private Integer wareId;

    private String areaCode;

    private String areaName;

    public Integer getWareId() {
        return wareId;
    }

    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
