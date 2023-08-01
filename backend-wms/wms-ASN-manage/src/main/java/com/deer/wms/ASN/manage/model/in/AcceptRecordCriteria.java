package com.deer.wms.ASN.manage.model.in;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by 郭靖勋 on 2019/12/18.
 */
@ToString
public class AcceptRecordCriteria extends QueryCriteria {
    private Integer acceptRecordId;
    private Integer batchId;
    private String itemCode;
    private String itemName;
    private Integer billType;
    private Integer acceptUserId;
    private String acceptTime;
    private String boxCode;
    private String cellCode;
    private Integer packDetailId;
    private String packDescribe;
    private String asnBillNo;
    private String asnDetailNo;
    private String requestDetailDetailNo;
    private Integer expectPackDetailId;
    private String expectPackDescribe;

    private Double transRatio;
    private Double expectTransRatio;

    private String fileTime;
    private Integer fileUserId;
    private String fileUserName;
    private String startAcceptTime;
    private String endAcceptTime;
    private String billNo;

    private String gtCreateTime;

    /**
     * 排序 ASC 时间正序，DESC 时间倒序
     */
    private String sort;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getGtCreateTime() {
        return gtCreateTime;
    }

    public void setGtCreateTime(String gtCreateTime) {
        this.gtCreateTime = gtCreateTime;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public String getAsnBillNo() {
        return asnBillNo;
    }

    public void setAsnBillNo(String asnBillNo) {
        this.asnBillNo = asnBillNo;
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

    public Integer getExpectPackDetailId() {
        return expectPackDetailId;
    }

    public void setExpectPackDetailId(Integer expectPackDetailId) {
        this.expectPackDetailId = expectPackDetailId;
    }

    public String getExpectPackDescribe() {
        return expectPackDescribe;
    }

    public void setExpectPackDescribe(String expectPackDescribe) {
        this.expectPackDescribe = expectPackDescribe;
    }

    public Double getTransRatio() {
        return transRatio;
    }

    public void setTransRatio(Double transRatio) {
        this.transRatio = transRatio;
    }

    public Double getExpectTransRatio() {
        return expectTransRatio;
    }

    public void setExpectTransRatio(Double expectTransRatio) {
        this.expectTransRatio = expectTransRatio;
    }

    public String getFileTime() {
        return fileTime;
    }

    public void setFileTime(String fileTime) {
        this.fileTime = fileTime;
    }

    public Integer getFileUserId() {
        return fileUserId;
    }

    public void setFileUserId(Integer fileUserId) {
        this.fileUserId = fileUserId;
    }

    public String getFileUserName() {
        return fileUserName;
    }

    public void setFileUserName(String fileUserName) {
        this.fileUserName = fileUserName;
    }

    public String getStartAcceptTime() {
        return startAcceptTime;
    }

    public void setStartAcceptTime(String startAcceptTime) {
        this.startAcceptTime = startAcceptTime;
    }

    public String getEndAcceptTime() {
        return endAcceptTime;
    }

    public void setEndAcceptTime(String endAcceptTime) {
        this.endAcceptTime = endAcceptTime;
    }
}
