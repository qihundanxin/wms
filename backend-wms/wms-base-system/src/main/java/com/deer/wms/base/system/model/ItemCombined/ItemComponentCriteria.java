package com.deer.wms.base.system.model.ItemCombined;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/02/14.
 */
@ToString
public class ItemComponentCriteria extends QueryCriteria {
    private String itemCode;
    private String combinedItemCode;

    public String getCombinedItemCode() {
        return combinedItemCode;
    }

    public void setCombinedItemCode(String combinedItemCode) {
        this.combinedItemCode = combinedItemCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
}