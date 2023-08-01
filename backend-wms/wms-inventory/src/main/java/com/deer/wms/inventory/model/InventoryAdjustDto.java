package com.deer.wms.inventory.model;

import lombok.ToString;

@ToString
public class InventoryAdjustDto extends InventoryAdjust {
    private String itemCode;
    private String itemName;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}