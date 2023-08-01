package com.deer.wms.busine.tactic.model;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/02/04.
 */
@ToString
public class SoMergeTacticCriteria extends QueryCriteria {

    private Integer soMergeTacticId;
    private String soMergeTacticCode;
    private Integer billSource;
    private Integer soType;

    public Integer getSoMergeTacticId() {
        return soMergeTacticId;
    }

    public void setSoMergeTacticId(Integer soMergeTacticId) {
        this.soMergeTacticId = soMergeTacticId;
    }

    public String getSoMergeTacticCode() {
        return soMergeTacticCode;
    }

    public void setSoMergeTacticCode(String soMergeTacticCode) {
        this.soMergeTacticCode = soMergeTacticCode;
    }

    public Integer getBillSource() {
        return billSource;
    }

    public void setBillSource(Integer billSource) {
        this.billSource = billSource;
    }

    public Integer getSoType() {
        return soType;
    }

    public void setSoType(Integer soType) {
        this.soType = soType;
    }
}
