package com.deer.wms.ware.task.model;

import java.util.List;

import lombok.ToString;

@ToString
public class ShipBoxInsert {

    private ShipBox shipBox;
    private List<String> list;

    public ShipBox getShipBox() {
        return shipBox;
    }

    public void setShipBox(ShipBox shipBox) {
        this.shipBox = shipBox;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}