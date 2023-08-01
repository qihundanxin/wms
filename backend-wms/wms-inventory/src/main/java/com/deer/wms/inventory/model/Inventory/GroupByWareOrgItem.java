package com.deer.wms.inventory.model.Inventory;

import lombok.ToString;

@ToString
public class GroupByWareOrgItem {
    private Integer wareId;
    private String wareName;
    private Integer organizationId;
    private String organizationName;
    private String itemName;
    private String itemCode;
    private Double totalQuantity;
    private Double totalAcceptQuantity;
    private Double totalOutQuantity;
    private Double totalAllotQuantity;
    private Double totalFreezeQuantity;
    private Double canAllotQuantity;
    private Double totalSjcQuantity;
    private Double totalCcwQuantity;

    public Double getTotalCcwQuantity() {
        return totalCcwQuantity;
    }

    public void setTotalCcwQuantity(Double totalCcwQuantity) {
        this.totalCcwQuantity = totalCcwQuantity;
    }

    public Double getTotalSjcQuantity() {
        return totalSjcQuantity;
    }

    public void setTotalSjcQuantity(Double totalSjcQuantity) {
        this.totalSjcQuantity = totalSjcQuantity;
    }



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

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Double getTotalAcceptQuantity() {
        return totalAcceptQuantity;
    }

    public void setTotalAcceptQuantity(Double totalAcceptQuantity) {
        this.totalAcceptQuantity = totalAcceptQuantity;
    }

    public Double getTotalOutQuantity() {
        return totalOutQuantity;
    }

    public void setTotalOutQuantity(Double totalOutQuantity) {
        this.totalOutQuantity = totalOutQuantity;
    }

    public Double getTotalAllotQuantity() {
        return totalAllotQuantity;
    }

    public void setTotalAllotQuantity(Double totalAllotQuantity) {
        this.totalAllotQuantity = totalAllotQuantity;
    }

    public Double getTotalFreezeQuantity() {
        return totalFreezeQuantity;
    }

    public void setTotalFreezeQuantity(Double totalFreezeQuantity) {
        this.totalFreezeQuantity = totalFreezeQuantity;
    }

    public Double getCanAllotQuantity() {
        return canAllotQuantity;
    }

    public void setCanAllotQuantity(Double canAllotQuantity) {
        this.canAllotQuantity = canAllotQuantity;
    }
}
