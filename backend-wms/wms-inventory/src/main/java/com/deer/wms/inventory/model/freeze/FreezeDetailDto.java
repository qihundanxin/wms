package com.deer.wms.inventory.model.freeze;

import lombok.ToString;

@ToString
public class FreezeDetailDto extends FreezeDetail {
    private Double quantity;
    private Double lastQuantity;

    private String freezeDescribe;

    public String getFreezeDescribe() {
        return freezeDescribe;
    }

    public void setFreezeDescribe(String freezeDescribe) {
        this.freezeDescribe = freezeDescribe;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getLastQuantity() {
        return lastQuantity;
    }

    public void setLastQuantity(Double lastQuantity) {
        this.lastQuantity = lastQuantity;
    }
}
