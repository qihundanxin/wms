package com.deer.wms.inventory.model.combinePallet;

import java.util.List;

import com.deer.wms.inventory.model.Inventory.Inventory;

import lombok.ToString;

@ToString
public class BoxResultInsertData {
    private String boxCode;
    private String boxCode2;
    private List<Inventory> inventoryList;
    private List<Inventory> inventoryList2;

    public String getBoxCode2() {
        return boxCode2;
    }

    public void setBoxCode2(String boxCode2) {
        this.boxCode2 = boxCode2;
    }

    public List<Inventory> getInventoryList2() {
        return inventoryList2;
    }

    public void setInventoryList2(List<Inventory> inventoryList2) {
        this.inventoryList2 = inventoryList2;
    }

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }

    public List<Inventory> getInventoryList() {
        return inventoryList;
    }

    public void setInventoryList(List<Inventory> inventoryList) {
        this.inventoryList = inventoryList;
    }
}
