package com.deer.wms.ware.task.model.SO;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

@ToString
public class SoLogCriteria extends QueryCriteria {

    private String itemCode;
    private String itemName;

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
}
