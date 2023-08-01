package com.deer.wms.ware.task.model.SO;

import lombok.ToString;

@ToString
public class SoDetailQh {

    private String itemCode;
    private String itemName;
    private String imgUrl;

    private Double qhQuantity;
    private Double hasBhQuantity;
    private Double canAllotQuantity;
    private Double billQhQuantity;
    private Double ccwQuantity;
    private Integer wareId;
    private String wareName;
    private Integer organizationId;
    private String organizationName;

    public Double getHasBhQuantity() {
        return hasBhQuantity;
    }

    public void setHasBhQuantity(Double hasBhQuantity) {
        this.hasBhQuantity = hasBhQuantity;
    }

    public Double getCanAllotQuantity() {
        return canAllotQuantity;
    }

    public void setCanAllotQuantity(Double canAllotQuantity) {
        this.canAllotQuantity = canAllotQuantity;
    }

    public Double getBillQhQuantity() {
        return billQhQuantity;
    }

    public void setBillQhQuantity(Double billQhQuantity) {
        this.billQhQuantity = billQhQuantity;
    }

    public Double getCcwQuantity() {
        return ccwQuantity;
    }

    public void setCcwQuantity(Double ccwQuantity) {
        this.ccwQuantity = ccwQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Double getQhQuantity() {
        return qhQuantity;
    }

    public void setQhQuantity(Double qhQuantity) {
        this.qhQuantity = qhQuantity;
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
}
