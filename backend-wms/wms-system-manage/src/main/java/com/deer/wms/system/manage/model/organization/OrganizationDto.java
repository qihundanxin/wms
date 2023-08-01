package com.deer.wms.system.manage.model.organization;

import lombok.ToString;

@ToString
public class OrganizationDto extends Organization {
    private String batchTacticName;
    private String upperTacticName;
    private String qcTacticName;
    private String soMergeTacticName;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getSoMergeTacticName() {
        return soMergeTacticName;
    }

    public void setSoMergeTacticName(String soMergeTacticName) {
        this.soMergeTacticName = soMergeTacticName;
    }
}
