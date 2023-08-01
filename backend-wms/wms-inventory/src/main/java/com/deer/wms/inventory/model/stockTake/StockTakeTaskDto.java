package com.deer.wms.inventory.model.stockTake;

import java.util.List;

import com.deer.wms.base.system.model.ItemUpc;

import lombok.ToString;

@ToString
public class StockTakeTaskDto extends StockTakeTask {
    private String wareName;
    private String organizationName;
    private String itemCode;
    private String itemName;
    private Double quantity;
    private Integer batchId;
    private String batchName;
    private String cellCode;
    private String boxCode;
    private Integer packDetailId;
    private String packDescribe;
    private Double transRatio;
    private String lpn;
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    private List<ItemUpc> upcs;

    public List<ItemUpc> getUpcs() {
        return upcs;
    }

    public void setUpcs(List<ItemUpc> upcs) {
        this.upcs = upcs;
    }

    public String getLpn() {
        return lpn;
    }

    public void setLpn(String lpn) {
        this.lpn = lpn;
    }

    public Double getTransRatio() {
        return transRatio;
    }

    public void setTransRatio(Double transRatio) {
        this.transRatio = transRatio;
    }

    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getCellCode() {
        return cellCode;
    }

    public void setCellCode(String cellCode) {
        this.cellCode = cellCode;
    }

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }

    public Integer getPackDetailId() {
        return packDetailId;
    }

    public void setPackDetailId(Integer packDetailId) {
        this.packDetailId = packDetailId;
    }

    public String getPackDescribe() {
        return packDescribe;
    }

    public void setPackDescribe(String packDescribe) {
        this.packDescribe = packDescribe;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
