package com.deer.wms.busine.tactic.model;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by on 2020/11/13.
 */
@ToString
public class PrintTempInfoCriteria extends QueryCriteria {
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
