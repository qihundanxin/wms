package com.deer.wms.inventory.model.Inventory;

import com.deer.wms.base.system.model.ImportModel;

import lombok.ToString;

@ToString
public class InventoryImport extends ImportModel {

    private Integer wareId;
    private String wareName;

    public Integer getWareId() {
        return wareId;
    }

    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }

    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }
}
