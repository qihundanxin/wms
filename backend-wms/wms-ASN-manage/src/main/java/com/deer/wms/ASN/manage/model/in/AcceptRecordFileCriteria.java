package com.deer.wms.ASN.manage.model.in;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2020/01/09.
 */
@ToString
public class AcceptRecordFileCriteria extends QueryCriteria {

    private Integer acceptRecordId;
    private Integer batchId;
    private String itemCode;
    private Integer billType;
    private Integer acceptUserId;
    private String acceptTime;
    private String boxCode;
    private String cellCode;
    private Integer packDetailId;
    private String packDescribe;
    private String asnDetailNo;
    private String requestDetailDetailNo;

    public Integer getAcceptRecordId() {
        return acceptRecordId;
    }

    public void setAcceptRecordId(Integer acceptRecordId) {
        this.acceptRecordId = acceptRecordId;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Integer getAcceptUserId() {
        return acceptUserId;
    }

    public void setAcceptUserId(Integer acceptUserId) {
        this.acceptUserId = acceptUserId;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }

    public String getCellCode() {
        return cellCode;
    }

    public void setCellCode(String cellCode) {
        this.cellCode = cellCode;
    }

    public Integer getPackDetailId() {
        return packDetailId;
    }

    public void setPackDetailId(Integer packDetailId) {
        this.packDetailId = packDetailId;
    }

    public String getPackDescribe() {
        return packDescribe;
    }

    public void setPackDescribe(String packDescribe) {
        this.packDescribe = packDescribe;
    }

    public String getAsnDetailNo() {
        return asnDetailNo;
    }

    public void setAsnDetailNo(String asnDetailNo) {
        this.asnDetailNo = asnDetailNo;
    }

    public String getRequestDetailDetailNo() {
        return requestDetailDetailNo;
    }

    public void setRequestDetailDetailNo(String requestDetailDetailNo) {
        this.requestDetailDetailNo = requestDetailDetailNo;
    }
}
