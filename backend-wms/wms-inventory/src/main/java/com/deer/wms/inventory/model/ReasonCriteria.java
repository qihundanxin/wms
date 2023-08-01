package com.deer.wms.inventory.model;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/02/13.
 */
@ToString
public class ReasonCriteria extends QueryCriteria {
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ReasonCriteria() {
    }

    public ReasonCriteria(String reason) {
        this.reason = reason;
    }
}
