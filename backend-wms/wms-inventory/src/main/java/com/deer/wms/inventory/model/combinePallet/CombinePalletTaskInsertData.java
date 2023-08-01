package com.deer.wms.inventory.model.combinePallet;

import java.util.List;

import lombok.ToString;

@ToString
public class CombinePalletTaskInsertData {
    private List<CombinePalletTaskDetail> details;

    public List<CombinePalletTaskDetail> getDetails() {
        return details;
    }

    public void setDetails(List<CombinePalletTaskDetail> details) {
        this.details = details;
    }
}
