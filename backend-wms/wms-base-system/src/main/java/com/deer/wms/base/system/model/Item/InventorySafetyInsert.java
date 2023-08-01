package com.deer.wms.base.system.model.Item;

import java.util.List;

import lombok.ToString;

@ToString
public class InventorySafetyInsert {
    private List<InventorySafety> inventorySafeties;

    public List<InventorySafety> getInventorySafeties() {
        return inventorySafeties;
    }

    public void setInventorySafeties(List<InventorySafety> inventorySafeties) {
        this.inventorySafeties = inventorySafeties;
    }
}
