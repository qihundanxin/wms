package com.deer.wms.system.manage.model.organization;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2019/11/05.
 */
@ToString
public class OrganizationCriteria extends QueryCriteria {
    private Integer userId;

    private String organizationCode;

    private String organizationName;

    private String batchTacticCode;
    private String upperTacticCode;
    private String qcTacticCode;
    private String soMergeTacticCode;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getBatchTacticCode() {
        return batchTacticCode;
    }

    public void setBatchTacticCode(String batchTacticCode) {
        this.batchTacticCode = batchTacticCode;
    }

    public String getUpperTacticCode() {
        return upperTacticCode;
    }

    public void setUpperTacticCode(String upperTacticCode) {
        this.upperTacticCode = upperTacticCode;
    }

    public String getQcTacticCode() {
        return qcTacticCode;
    }

    public void setQcTacticCode(String qcTacticCode) {
        this.qcTacticCode = qcTacticCode;
    }

    public String getSoMergeTacticCode() {
        return soMergeTacticCode;
    }

    public void setSoMergeTacticCode(String soMergeTacticCode) {
        this.soMergeTacticCode = soMergeTacticCode;
    }
}
