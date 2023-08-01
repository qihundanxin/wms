package com.deer.wms.ASN.manage.model.qc;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2020/01/19.
 */
@ToString
public class QcRecordFileCriteria extends QueryCriteria {

    private Integer qcRecordFileId;
    private String qcDetailNo;
    private String qcBillNo;
    private Integer qcResult;
    private Integer billType;

    public Integer getQcRecordFileId() {
        return qcRecordFileId;
    }

    public void setQcRecordFileId(Integer qcRecordFileId) {
        this.qcRecordFileId = qcRecordFileId;
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
}
