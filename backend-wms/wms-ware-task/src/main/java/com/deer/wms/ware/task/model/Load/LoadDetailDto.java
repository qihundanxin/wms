package com.deer.wms.ware.task.model.Load;

import lombok.ToString;

@ToString
public class LoadDetailDto extends LoadDetail {
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
