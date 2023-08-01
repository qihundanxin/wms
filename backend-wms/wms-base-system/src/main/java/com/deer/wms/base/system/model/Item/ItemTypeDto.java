package com.deer.wms.base.system.model.Item;

import lombok.ToString;

@ToString
public class ItemTypeDto extends ItemType {
    private String organizationName;
    private String batchTacticName;
    private String upperTacticName;
    private String qcTacticName;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getBatchTacticName() {
        return batchTacticName;
    }

    public void setBatchTacticName(String batchTacticName) {
        this.batchTacticName = batchTacticName;
    }

    public String getUpperTacticName() {
        return upperTacticName;
    }

    public void setUpperTacticName(String upperTacticName) {
        this.upperTacticName = upperTacticName;
    }

    public String getQcTacticName() {
        return qcTacticName;
    }

    public void setQcTacticName(String qcTacticName) {
        this.qcTacticName = qcTacticName;
    }
}
