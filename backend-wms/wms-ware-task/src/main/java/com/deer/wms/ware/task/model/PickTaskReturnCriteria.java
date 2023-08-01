package com.deer.wms.ware.task.model;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by on 2021/03/20.
 */
@ToString
public class PickTaskReturnCriteria extends QueryCriteria {
    private Integer state;
    private String itemCode;
    private Integer type;
    private Integer wareId;
    private Integer orgId;
    private String boxCode;

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public Integer getWareId() {
        return wareId;
    }

    @Override
    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }
}
