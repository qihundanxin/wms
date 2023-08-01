package com.deer.wms.inventory.model;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by on 2021/02/14.
 */
@ToString
public class ErpRecordCriteria extends QueryCriteria {
    private Integer state;
    private Integer type;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
