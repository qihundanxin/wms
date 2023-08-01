package com.deer.wms.inventory.model;

import lombok.ToString;

@ToString
public class InventoryAdjustDetailDto extends InventoryAdjustDetail {
    private String reason;
    private Integer wareId;
    private String wareName;
    private Integer organizationId;
    private String organizationName;
    private Double transRatio;

    public Double getTransRatio() {
        return transRatio;
    }

    public void setTransRatio(Double transRatio) {
        this.transRatio = transRatio;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}