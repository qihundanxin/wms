package com.deer.wms.ASN.manage.model.qc;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2020/01/15.
 */
@ToString
public class QcDetailCriteria extends QueryCriteria {
    private String billNo;
    private Integer qcDetailId;
    private String detailNo;
    private String asnDetailNo;
    private Integer inventoryId;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getQcDetailId() {
        return qcDetailId;
    }

    public void setQcDetailId(Integer qcDetailId) {
        this.qcDetailId = qcDetailId;
    }

    public String getDetailNo() {
        return detailNo;
    }

    public void setDetailNo(String detailNo) {
        this.detailNo = detailNo;
    }

    public String getAsnDetailNo() {
        return asnDetailNo;
    }

    public void setAsnDetailNo(String asnDetailNo) {
        this.asnDetailNo = asnDetailNo;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }
}
