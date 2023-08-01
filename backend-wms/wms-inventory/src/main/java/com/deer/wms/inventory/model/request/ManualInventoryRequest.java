package com.deer.wms.inventory.model.request;

import lombok.ToString;

@ToString
public class ManualInventoryRequest {
    /**
     * sku编码
     */
    private String itemCode;
    /**
     * sku数量
     */
    private Integer itemQuantity;
    /**
     * 扫描内容
     */
    private String scanText;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getScanText() {
        return scanText;
    }

    public void setScanText(String scanText) {
        this.scanText = scanText;
    }
}
