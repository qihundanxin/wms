package com.deer.wms.review.manage.model;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/03/05.
 */
@ToString
public class BillRecordCriteria extends QueryCriteria {
    private Integer billRecordId;
    private String billNo;
    private Integer recordType;
    private Integer createUserId;
    private Integer modifyUserId;

    public Integer getBillRecordId() {
        return billRecordId;
    }

    public void setBillRecordId(Integer billRecordId) {
        this.billRecordId = billRecordId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(Integer modifyUserId) {
        this.modifyUserId = modifyUserId;
    }
}
