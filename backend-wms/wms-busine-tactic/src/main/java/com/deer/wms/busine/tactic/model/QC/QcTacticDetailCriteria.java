package com.deer.wms.busine.tactic.model.QC;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2020/01/15.
 */
@ToString
public class QcTacticDetailCriteria extends QueryCriteria {
    private Integer qcTacticDetailId;
    private String qcTacticCode;
    private Integer asnType;
    private Double startQuantity;
    private Double endQuantity;
    private Double sampleQuantity;
    private Double percentage;
    private Double minQuantity;

    public Integer getQcTacticDetailId() {
        return qcTacticDetailId;
    }

    public void setQcTacticDetailId(Integer qcTacticDetailId) {
        this.qcTacticDetailId = qcTacticDetailId;
    }

    public String getQcTacticCode() {
        return qcTacticCode;
    }

    public void setQcTacticCode(String qcTacticCode) {
        this.qcTacticCode = qcTacticCode;
    }

    public Integer getAsnType() {
        return asnType;
    }

    public void setAsnType(Integer asnType) {
        this.asnType = asnType;
    }

    public Double getStartQuantity() {
        return startQuantity;
    }

    public void setStartQuantity(Double startQuantity) {
        this.startQuantity = startQuantity;
    }

    public Double getEndQuantity() {
        return endQuantity;
    }

    public void setEndQuantity(Double endQuantity) {
        this.endQuantity = endQuantity;
    }

    public Double getSampleQuantity() {
        return sampleQuantity;
    }

    public void setSampleQuantity(Double sampleQuantity) {
        this.sampleQuantity = sampleQuantity;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Double getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(Double minQuantity) {
        this.minQuantity = minQuantity;
    }
}
