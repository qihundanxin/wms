package com.deer.wms.ware.task.model;

import lombok.ToString;

@ToString
public class ShipBoxDto extends ShipBox {
    private String shipName;

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }
}