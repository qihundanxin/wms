package com.deer.wms.ware.task.model;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by on 2021/01/06.
 */
@ToString
public class ShipBoxDetailCriteria extends QueryCriteria {
    private Integer shipBoxId;
    private String shipBoxCode;

    public Integer getShipBoxId() {
        return shipBoxId;
    }

    public void setShipBoxId(Integer shipBoxId) {
        this.shipBoxId = shipBoxId;
    }

    public String getShipBoxCode() {
        return shipBoxCode;
    }

    public void setShipBoxCode(String shipBoxCode) {
        this.shipBoxCode = shipBoxCode;
    }
}
