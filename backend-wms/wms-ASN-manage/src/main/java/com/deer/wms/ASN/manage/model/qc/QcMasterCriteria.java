package com.deer.wms.ASN.manage.model.qc;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2020/01/15.
 */
@ToString
public class QcMasterCriteria extends QueryCriteria {

    private String billNo;
    private String asnBillNo;
    private Integer insertType;  //0-提交  -2保存为草稿
    private Integer billSource;
    private Integer state;
    private String startCreateTime;
    private String endCreateTime;
    private Integer qcType;
    private Integer qcState;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getAsnBillNo() {
        return asnBillNo;
    }

    public void setAsnBillNo(String asnBillNo) {
        this.asnBillNo = asnBillNo;
    }

    public Integer getInsertType() {
        return insertType;
    }

    public void setInsertType(Integer insertType) {
        this.insertType = insertType;
    }

    public Integer getBillSource() {
        return billSource;
    }

    public void setBillSource(Integer billSource) {
        this.billSource = billSource;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getStartCreateTime() {
        return startCreateTime;
    }

    public void setStartCreateTime(String startCreateTime) {
        this.startCreateTime = startCreateTime;
    }

    public String getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(String endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    public Integer getQcType() {
        return qcType;
    }

    public void setQcType(Integer qcType) {
        this.qcType = qcType;
    }

    public Integer getQcState() {
        return qcState;
    }

    public void setQcState(Integer qcState) {
        this.qcState = qcState;
    }
}
