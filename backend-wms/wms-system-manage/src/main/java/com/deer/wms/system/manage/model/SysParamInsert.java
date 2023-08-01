package com.deer.wms.system.manage.model;

import java.util.List;

import lombok.ToString;

@ToString
public class SysParamInsert extends SysParam {
    private List<SysParamDetail> details;

    public List<SysParamDetail> getDetails() {
        return details;
    }

    public void setDetails(List<SysParamDetail> details) {
        this.details = details;
    }
}