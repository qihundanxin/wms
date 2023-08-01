package com.deer.wms.ASN.manage.model.asn.CreateASN;

import lombok.ToString;

@ToString
public class AddInShipNo {
    private String billNo;
    private String shipBillNo;
    private String shipCode;

    public String getShipCode() {
        return shipCode;
    }

    public void setShipCode(String shipCode) {
        this.shipCode = shipCode;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getShipBillNo() {
        return shipBillNo;
    }

    public void setShipBillNo(String shipBillNo) {
        this.shipBillNo = shipBillNo;
    }
}
