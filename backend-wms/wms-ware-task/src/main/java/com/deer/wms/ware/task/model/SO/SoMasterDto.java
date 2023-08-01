package com.deer.wms.ware.task.model.SO;

import java.util.List;

import lombok.ToString;

@ToString
public class SoMasterDto extends SoMaster {
    private Double weight;
    private Double size;
    private String shipName;
    private Double pickTotalQuantity;

    List<SoDetail> list;

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public List<SoDetail> getList() {
        return list;
    }

    public void setList(List<SoDetail> list) {
        this.list = list;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Double getPickTotalQuantity() {
        return pickTotalQuantity;
    }

    public void setPickTotalQuantity(Double pickTotalQuantity) {
        this.pickTotalQuantity = pickTotalQuantity;
    }
}
