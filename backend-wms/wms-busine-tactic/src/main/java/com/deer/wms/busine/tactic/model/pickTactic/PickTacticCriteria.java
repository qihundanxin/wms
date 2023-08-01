package com.deer.wms.busine.tactic.model.pickTactic;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/02/02.
 */
@ToString
public class PickTacticCriteria extends QueryCriteria {
    private String allocationRuleCode;
    private String description;
    private Integer pickTacticId;
    private Integer billNo;

    public Integer getBillNo() {
        return billNo;
    }

    public void setBillNo(Integer billNo) {
        this.billNo = billNo;
    }

    public String getAllocationRuleCode() {
        return allocationRuleCode;
    }

    public void setAllocationRuleCode(String allocationRuleCode) {
        this.allocationRuleCode = allocationRuleCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPickTacticId() {
        return pickTacticId;
    }

    public void setPickTacticId(Integer pickTacticId) {
        this.pickTacticId = pickTacticId;
    }

    public PickTacticCriteria() {
    }

    public PickTacticCriteria(String allocationRuleCode) {
        this.allocationRuleCode = allocationRuleCode;
    }

    public PickTacticCriteria(String allocationRuleCode, Integer pickTacticId) {
        this.allocationRuleCode = allocationRuleCode;
        this.pickTacticId = pickTacticId;
    }
}
