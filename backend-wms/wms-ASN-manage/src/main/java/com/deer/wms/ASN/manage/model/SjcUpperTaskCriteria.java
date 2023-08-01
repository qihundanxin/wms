package com.deer.wms.ASN.manage.model;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by on 2021/01/07.
 */
@ToString
public class SjcUpperTaskCriteria extends QueryCriteria {
    private String shelfCode;

    private Integer state;
    private Integer type;

    private Integer inventoryId;

    private String cellCode;

    private Integer acceptInventoryId;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAcceptInventoryId() {
        return acceptInventoryId;
    }

    public void setAcceptInventoryId(Integer acceptInventoryId) {
        this.acceptInventoryId = acceptInventoryId;
    }

    public String getCellCode() {
        return cellCode;
    }

    public void setCellCode(String cellCode) {
        this.cellCode = cellCode;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getShelfCode() {
        return shelfCode;
    }

    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode;
    }
}
