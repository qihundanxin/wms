package com.deer.wms.ASN.manage.model.qc;

import java.util.List;

import lombok.ToString;

@ToString
public class QcInsert {
    private QcMaster qcMaster;
    private List<QcDetail> qcDetails;

    private Integer insertType;  //0-提交  -2保存为草稿
    private Integer billSource; //单据来源   0-ASN单生成  1-库存生成

    public QcMaster getQcMaster() {
        return qcMaster;
    }

    public void setQcMaster(QcMaster qcMaster) {
        this.qcMaster = qcMaster;
    }

    public List<QcDetail> getQcDetails() {
        return qcDetails;
    }

    public void setQcDetails(List<QcDetail> qcDetails) {
        this.qcDetails = qcDetails;
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

}
