package com.deer.wms.ware.task.model.pickTask;

import lombok.ToString;

@ToString
public class SeedingWallAlertInfo {
    private String soNo;
    private String itemCode;
    private String itemName;
    private Double pickQuantity;
    private Double neededQuantity;

    public String getSoNo() {
        return soNo;
    }

    public void setSoNo(String soNo) {
        this.soNo = soNo;
    }

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

    public Double getPickQuantity() {
        return pickQuantity;
    }

    public void setPickQuantity(Double pickQuantity) {
        this.pickQuantity = pickQuantity;
    }

    public Double getNeededQuantity() {
        return neededQuantity;
    }

    public void setNeededQuantity(Double neededQuantity) {
        this.neededQuantity = neededQuantity;
    }
}
