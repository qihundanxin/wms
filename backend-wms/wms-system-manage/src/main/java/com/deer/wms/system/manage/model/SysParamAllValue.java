package com.deer.wms.system.manage.model;

import java.util.List;

import lombok.ToString;

@ToString
public class SysParamAllValue {
    private List<SysParam> sysParams;

    public List<SysParam> getSysParams() {
        return sysParams;
    }

    public void setSysParams(List<SysParam> sysParams) {
        this.sysParams = sysParams;
    }
}