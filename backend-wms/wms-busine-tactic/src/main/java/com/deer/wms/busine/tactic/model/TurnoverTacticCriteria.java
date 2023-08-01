package com.deer.wms.busine.tactic.model;

import java.util.List;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by on 2020/03/15.
 */
@ToString
public class TurnoverTacticCriteria extends QueryCriteria {
    private Integer turnoverMasterId;
    private String turnoverCode;
    private String turnoverDescription;
    private String batchTacticCode;
    private List<TurnoverTacticDetail> turnoverTacticDetails;

    public String getTurnoverDescription() {
        return turnoverDescription;
    }

    public void setTurnoverDescription(String turnoverDescription) {
        this.turnoverDescription = turnoverDescription;
    }

    public String getBatchTacticCode() {
        return batchTacticCode;
    }

    public void setBatchTacticCode(String batchTacticCode) {
        this.batchTacticCode = batchTacticCode;
    }

    public List<TurnoverTacticDetail> getTurnoverTacticDetails() {
        return turnoverTacticDetails;
    }

    public void setTurnoverTacticDetails(List<TurnoverTacticDetail> turnoverTacticDetails) {
        this.turnoverTacticDetails = turnoverTacticDetails;
    }

    public String getTurnoverCode() {
        return turnoverCode;
    }

    public void setTurnoverCode(String turnoverCode) {
        this.turnoverCode = turnoverCode;
    }

    public Integer getTurnoverMasterId() {
        return turnoverMasterId;
    }

    public void setTurnoverMasterId(Integer turnoverMasterId) {
        this.turnoverMasterId = turnoverMasterId;
    }
}
