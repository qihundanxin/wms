package com.deer.wms.ASN.manage.model.asn.CreateASN;

import lombok.ToString;

@ToString
public class CreateASNShipDetail {
    private String shipCode;
    private String shipBillCode;

    public String getShipCode() {
        return shipCode;
    }

    public void setShipCode(String shipCode) {
        this.shipCode = shipCode;
    }

    public String getShipBillCode() {
        return shipBillCode;
    }

    public void setShipBillCode(String shipBillCode) {
        this.shipBillCode = shipBillCode;
    }
}
