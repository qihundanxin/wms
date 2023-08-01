package com.deer.wms.inventory.model.Inventory;

import lombok.ToString;

@ToString
public class InventorySelectDto extends Inventory {
    private String cellCode;
    private String cellName;
    private String shelfName;
    private String shelfCode;
    private String areaName;
    private String areaCode;

    @Override
    public String getCellCode() {
        return cellCode;
    }

    @Override
    public void setCellCode(String cellCode) {
        this.cellCode = cellCode;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public String getShelfName() {
        return shelfName;
    }

    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
    }

    public String getShelfCode() {
        return shelfCode;
    }

    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
