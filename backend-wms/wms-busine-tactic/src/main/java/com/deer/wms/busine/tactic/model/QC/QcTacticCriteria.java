package com.deer.wms.busine.tactic.model.QC;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2020/01/15.
 */
@ToString
public class QcTacticCriteria extends QueryCriteria {
    private Integer qcTacticId;
    private String qcTacticCode;
    private String qcTacticName;
    private Integer qcType;

    public Integer getQcTacticId() {
        return qcTacticId;
    }

    public void setQcTacticId(Integer qcTacticId) {
        this.qcTacticId = qcTacticId;
    }

    public String getQcTacticCode() {
        return qcTacticCode;
    }

    public void setQcTacticCode(String qcTacticCode) {
        this.qcTacticCode = qcTacticCode;
    }

    public String getQcTacticName() {
        return qcTacticName;
    }

    public void setQcTacticName(String qcTacticName) {
        this.qcTacticName = qcTacticName;
    }

    public Integer getQcType() {
        return qcType;
    }

    public void setQcType(Integer qcType) {
        this.qcType = qcType;
    }
}
