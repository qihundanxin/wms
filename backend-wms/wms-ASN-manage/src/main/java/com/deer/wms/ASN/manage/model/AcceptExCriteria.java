package com.deer.wms.ASN.manage.model;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by on 2021/01/02.
 */
@ToString
public class AcceptExCriteria extends QueryCriteria {

    private String shipBillNo;
    private String billNo;
    private Integer state;
    private Integer type;
    private Integer reason;

    private String itemCode;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getReason() {
        return reason;
    }

    public void setReason(Integer reason) {
        this.reason = reason;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getShipBillNo() {
        return shipBillNo;
    }

    public void setShipBillNo(String shipBillNo) {
        this.shipBillNo = shipBillNo;
    }
}
