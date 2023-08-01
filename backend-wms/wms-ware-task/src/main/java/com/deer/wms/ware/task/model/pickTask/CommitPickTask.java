package com.deer.wms.ware.task.model.pickTask;

import lombok.ToString;

@ToString
public class CommitPickTask {
    private Integer pickTaskId;
    private Integer inventoryId;
    private Double pickQuantity;
    private String toCellCode;
    private String toTrackingNum;

    public Integer getPickTaskId() {
        return pickTaskId;
    }

    public void setPickTaskId(Integer pickTaskId) {
        this.pickTaskId = pickTaskId;
    }

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

    public String getToCellCode() {
        return toCellCode;
    }

    public void setToCellCode(String toCellCode) {
        this.toCellCode = toCellCode;
    }

    public String getToTrackingNum() {
        return toTrackingNum;
    }

    public void setToTrackingNum(String toTrackingNum) {
        this.toTrackingNum = toTrackingNum;
    }
}
