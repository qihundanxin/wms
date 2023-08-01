package com.deer.wms.ASN.manage.model.asn;

import java.util.List;

import lombok.ToString;

@ToString
public class AsnData {
    private AsnMaster asnMaster;
    private List<AsnDetail> asnDetails;
    private Integer insertType;  //0-提交  -2保存为草稿
    private Integer billSource; //单据来源 1-新建  0-采购单生成  2-供应商采购生成  3-供应商新建 4-调拨生成  5-SO退货生成入库单
    private List<AsnShipDetail> asnShipDetails;
    private Integer requestBuyVersion;
    private Integer requestBuyId;

    public Integer getRequestBuyId() {
        return requestBuyId;
    }

    public void setRequestBuyId(Integer requestBuyId) {
        this.requestBuyId = requestBuyId;
    }

    public Integer getRequestBuyVersion() {
        return requestBuyVersion;
    }

    public void setRequestBuyVersion(Integer requestBuyVersion) {
        this.requestBuyVersion = requestBuyVersion;
    }

    public List<AsnShipDetail> getAsnShipDetails() {
        return asnShipDetails;
    }

    public void setAsnShipDetails(List<AsnShipDetail> asnShipDetails) {
        this.asnShipDetails = asnShipDetails;
    }

    public AsnMaster getAsnMaster() {
        return asnMaster;
    }

    public void setAsnMaster(AsnMaster asnMaster) {
        this.asnMaster = asnMaster;
    }

    public List<AsnDetail> getAsnDetails() {
        return asnDetails;
    }

    public void setAsnDetails(List<AsnDetail> asnDetails) {
        this.asnDetails = asnDetails;
    }

    public Integer getBillSource() {
        return billSource;
    }

    public void setBillSource(Integer billSource) {
        this.billSource = billSource;
    }

    public Integer getInsertType() {
        return insertType;
    }

    public void setInsertType(Integer insertType) {
        this.insertType = insertType;
    }

}
