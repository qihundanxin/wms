package com.deer.wms.inventory.model.freeze;

import java.util.List;

import lombok.ToString;

@ToString
public class InsertData {

    private FreezeMaster freezeMaster;
    private List<FreezeDetail> freezeDetails;
    private Integer insertType;

    public FreezeMaster getFreezeMaster() {
        return freezeMaster;
    }

    public void setFreezeMaster(FreezeMaster freezeMaster) {
        this.freezeMaster = freezeMaster;
    }

    public List<FreezeDetail> getFreezeDetails() {
        return freezeDetails;
    }

    public void setFreezeDetails(List<FreezeDetail> freezeDetails) {
        this.freezeDetails = freezeDetails;
    }

    public Integer getInsertType() {
        return insertType;
    }

    public void setInsertType(Integer insertType) {
        this.insertType = insertType;
    }
}
