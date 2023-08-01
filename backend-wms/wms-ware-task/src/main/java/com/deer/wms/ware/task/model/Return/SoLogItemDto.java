package com.deer.wms.ware.task.model.Return;

import lombok.ToString;

@ToString
public class SoLogItemDto {

    private String billNo;

    private String itemCode;

    private String itemName;

    private String originalItemName;

    private Integer status;

    private Double quantity;

    private Double deliveryQuantity;

    private String wareName;

    private String cellCode;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
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

    public String getOriginalItemName() {
        return originalItemName;
    }

    public void setOriginalItemName(String originalItemName) {
        this.originalItemName = originalItemName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getDeliveryQuantity() {
        return deliveryQuantity;
    }

    public void setDeliveryQuantity(Double deliveryQuantity) {
        this.deliveryQuantity = deliveryQuantity;
    }

    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }

    public String getCellCode() {
        return cellCode;
    }

    public void setCellCode(String cellCode) {
        this.cellCode = cellCode;
    }
}
