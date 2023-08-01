package com.deer.wms.ware.task.model.pickTask;

import lombok.ToString;

@ToString
public class CreatePickTaskDetail {
    private Integer inventoryId;
    private Double pickQuantity;
    private Integer packDetailId;

    public Integer getPackDetailId() {
        return packDetailId;
    }

    public void setPackDetailId(Integer packDetailId) {
        this.packDetailId = packDetailId;
    }

    //单位

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Double getPickQuantity() {
        return pickQuantity;
    }

    public void setPickQuantity(Double pickQuantity) {
        this.pickQuantity = pickQuantity;
    }
}
