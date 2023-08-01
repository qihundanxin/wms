package com.deer.wms.inventory.model;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/03/12.
 */
@ToString
public class InventoryAdjustDetailCriteria extends QueryCriteria {
    private String billNo;
    private Integer adjustDetailId;
    private Integer state;
    private Integer synErp;

    public Integer getSynErp() {
        return synErp;
    }

    public void setSynErp(Integer synErp) {
        this.synErp = synErp;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getAdjustDetailId() {
        return adjustDetailId;
    }

    public void setAdjustDetailId(Integer adjustDetailId) {
        this.adjustDetailId = adjustDetailId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
}
