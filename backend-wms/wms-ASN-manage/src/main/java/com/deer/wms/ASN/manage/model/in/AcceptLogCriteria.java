package com.deer.wms.ASN.manage.model.in;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

@ToString
public class AcceptLogCriteria extends QueryCriteria {

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
