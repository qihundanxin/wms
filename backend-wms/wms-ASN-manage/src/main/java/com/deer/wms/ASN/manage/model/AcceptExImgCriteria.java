package com.deer.wms.ASN.manage.model;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by on 2021/01/02.
 */
@ToString
public class AcceptExImgCriteria extends QueryCriteria {
    private String shipBillCode;

    public String getShipBillCode() {
        return shipBillCode;
    }

    public void setShipBillCode(String shipBillCode) {
        this.shipBillCode = shipBillCode;
    }
}
