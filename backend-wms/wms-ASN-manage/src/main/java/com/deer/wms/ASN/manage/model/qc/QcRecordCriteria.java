package com.deer.wms.ASN.manage.model.qc;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2020/01/15.
 */
@ToString
public class QcRecordCriteria extends QueryCriteria {

    private Integer qcRecordId;
    private String qcDetailNo;
    private String qcBillNo;
    private Integer qcResult;
    private Integer billType;
    private Integer inventoryId;

    public Integer getQcRecordId() {
        return qcRecordId;
    }

    public void setQcRecordId(Integer qcRecordId) {
        this.qcRecordId = qcRecordId;
    }

    public String getQcDetailNo() {
        return qcDetailNo;
    }

    public void setQcDetailNo(String qcDetailNo) {
        this.qcDetailNo = qcDetailNo;
    }

    public String getQcBillNo() {
        return qcBillNo;
    }

    public void setQcBillNo(String qcBillNo) {
        this.qcBillNo = qcBillNo;
    }

    public Integer getQcResult() {
        return qcResult;
    }

    public void setQcResult(Integer qcResult) {
        this.qcResult = qcResult;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }
}
