package com.deer.wms.ASN.manage.model.file;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2020/01/19.
 */
@ToString
public class QcDetailFileCriteria extends QueryCriteria {

    private String billNo;
    private Integer qcDetailId;
    private String detailNo;
    private String asnDetailNo;

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
}
