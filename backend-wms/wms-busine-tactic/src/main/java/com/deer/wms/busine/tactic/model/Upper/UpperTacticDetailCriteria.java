package com.deer.wms.busine.tactic.model.Upper;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2019/11/28.
 */
@ToString
public class UpperTacticDetailCriteria extends QueryCriteria {
    private Integer upperTacticDetailId;
    private Integer upperTacticId;
    private String upperTacticCode;
    private Integer shelfInfoId;

    public Integer getUpperTacticDetailId() {
        return upperTacticDetailId;
    }

    public void setUpperTacticDetailId(Integer upperTacticDetailId) {
        this.upperTacticDetailId = upperTacticDetailId;
    }

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

    public Integer getShelfInfoId() {
        return shelfInfoId;
    }

    public void setShelfInfoId(Integer shelfInfoId) {
        this.shelfInfoId = shelfInfoId;
    }
}
