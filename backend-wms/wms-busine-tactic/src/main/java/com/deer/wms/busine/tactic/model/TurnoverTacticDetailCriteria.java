package com.deer.wms.busine.tactic.model;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by on 2020/03/15.
 */
@ToString
public class TurnoverTacticDetailCriteria extends QueryCriteria {
    private String turnoverCode;

    public String getTurnoverCode() {
        return turnoverCode;
    }

    public void setTurnoverCode(String turnoverCode) {
        this.turnoverCode = turnoverCode;
    }

    public TurnoverTacticDetailCriteria() {
    }

    public TurnoverTacticDetailCriteria(String turnoverCode) {
        this.turnoverCode = turnoverCode;
    }
}
