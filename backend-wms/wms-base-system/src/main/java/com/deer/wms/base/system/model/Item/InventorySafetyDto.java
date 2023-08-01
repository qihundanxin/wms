package com.deer.wms.base.system.model.Item;

import lombok.ToString;

@ToString
public class InventorySafetyDto extends InventorySafety{
    private Double canAllotQuantity;
    private Double bhQuantity;

    public Double getBhQuantity() {
        return bhQuantity;
    }

    public void setBhQuantity(Double bhQuantity) {
        this.bhQuantity = bhQuantity;
    }

    public Double getCanAllotQuantity() {
        return canAllotQuantity;
    }

    public void setCanAllotQuantity(Double canAllotQuantity) {
        this.canAllotQuantity = canAllotQuantity;
    }
}
