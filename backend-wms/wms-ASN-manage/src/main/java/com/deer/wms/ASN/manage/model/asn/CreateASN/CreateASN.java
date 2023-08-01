package com.deer.wms.ASN.manage.model.asn.CreateASN;

import java.util.List;

import lombok.ToString;

@ToString
public class CreateASN {
    private String wareCode;
    private String orgCode;
    private String billNo;
    private List<CreateASNSKUDetail> skuList;
    private List<CreateASNShipDetail> shipList;

    public String getWareCode() {
        return wareCode;
    }

    public void setWareCode(String wareCode) {
        this.wareCode = wareCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public List<CreateASNSKUDetail> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<CreateASNSKUDetail> skuList) {
        this.skuList = skuList;
    }

    public List<CreateASNShipDetail> getShipList() {
        return shipList;
    }

    public void setShipList(List<CreateASNShipDetail> shipList) {
        this.shipList = shipList;
    }
}
