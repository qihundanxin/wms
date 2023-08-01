package com.deer.wms.inventory.model;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/03/13.
 */
@ToString
public class IvTransOrgDetailCriteria extends QueryCriteria {
    private Integer masterId;

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }
}
