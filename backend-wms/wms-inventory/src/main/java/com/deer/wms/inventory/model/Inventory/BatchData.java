package com.deer.wms.inventory.model.Inventory;

import java.util.List;

import lombok.ToString;

@ToString
public class BatchData {
    private Integer qcState;
    private List<Integer> inventoryIds;

    public List<Integer> getInventoryIds() {
        return inventoryIds;
    }

    public void setInventoryIds(List<Integer> inventoryIds) {
        this.inventoryIds = inventoryIds;
    }

    public Integer getQcState() {
        return qcState;
    }

    public void setQcState(Integer qcState) {
        this.qcState = qcState;
    }
}
