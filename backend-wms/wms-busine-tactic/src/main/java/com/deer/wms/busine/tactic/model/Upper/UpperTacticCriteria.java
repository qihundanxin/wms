package com.deer.wms.busine.tactic.model.Upper;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2019/11/28.
 */
@ToString
public class UpperTacticCriteria extends QueryCriteria {
    private Integer upperTacticId;
    private String upperTacticCode;
    private String upperTacticName;

    public Integer getUpperTacticId() {
        return upperTacticId;
    }

    public void setUpperTacticId(Integer upperTacticId) {
        this.upperTacticId = upperTacticId;
    }

    public String getUpperTacticCode() {
        return upperTacticCode;
    }

    public void setUpperTacticCode(String upperTacticCode) {
        this.upperTacticCode = upperTacticCode;
    }

    public String getUpperTacticName() {
        return upperTacticName;
    }

    public void setUpperTacticName(String upperTacticName) {
        this.upperTacticName = upperTacticName;
    }
}
