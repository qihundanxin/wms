package com.deer.wms.ASN.manage.model.file;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2020/01/05.
 */
@ToString
public class RequestBuyFileCriteria extends QueryCriteria {
    private Integer requestBuyFileId;
    private String billNo;
    private Integer buyType;
    private Integer state;
    private Integer acceptState;
    private Integer supplierId;
    private Integer createUserId;
    private String createTime;
    private String expectTime;
    private Integer insertType;
    private String fileTime;
    private Integer fileUserId;
    private String fileUserName;

    public Integer getRequestBuyFileId() {
        return requestBuyFileId;
    }

    public void setRequestBuyFileId(Integer requestBuyFileId) {
        this.requestBuyFileId = requestBuyFileId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getBuyType() {
        return buyType;
    }

    public void setBuyType(Integer buyType) {
        this.buyType = buyType;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getAcceptState() {
        return acceptState;
    }

    public void setAcceptState(Integer acceptState) {
        this.acceptState = acceptState;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = expectTime;
    }

    public Integer getInsertType() {
        return insertType;
    }

    public void setInsertType(Integer insertType) {
        this.insertType = insertType;
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
}
