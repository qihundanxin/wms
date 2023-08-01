package com.deer.wms.inventory.model.Inventory;

import lombok.ToString;

@ToString
public class InventoryTransactDto extends InventoryTransact {
    private Integer itemTypeId;
    private String itemTypeName;

    public Integer getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Integer itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }
}
