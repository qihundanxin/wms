package com.deer.wms.system.manage.model;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2019/07/12.
 */
@ToString
public class SysParamDetailCriteria extends QueryCriteria {
    private Integer sysParamId;

    public Integer getSysParamId() {
        return sysParamId;
    }

    public void setSysParamId(Integer sysParamId) {
        this.sysParamId = sysParamId;
    }
}
