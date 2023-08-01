package com.deer.wms.ware.task.model.SO;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/02/03.
 */
@ToString
public class SoMasterFileCriteria extends QueryCriteria {
    private Integer soMasterFileId;
    private String billNo;
    private Integer state;
    private Integer customerId;
    private Integer createUserId;
    private Integer billSource;
    private Integer insertType;
    private Integer receivingAddressId;
    private Integer carrierId;
    private Integer soType;
    private String mergeNo;

    public Integer getSoMasterFileId() {
        return soMasterFileId;
    }

    public void setSoMasterFileId(Integer soMasterFileId) {
        this.soMasterFileId = soMasterFileId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getBillSource() {
        return billSource;
    }

    public void setBillSource(Integer billSource) {
        this.billSource = billSource;
    }

    public Integer getInsertType() {
        return insertType;
    }

    public void setInsertType(Integer insertType) {
        this.insertType = insertType;
    }

    public Integer getReceivingAddressId() {
        return receivingAddressId;
    }

    public void setReceivingAddressId(Integer receivingAddressId) {
        this.receivingAddressId = receivingAddressId;
    }

    public Integer getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Integer carrierId) {
        this.carrierId = carrierId;
    }

    public Integer getSoType() {
        return soType;
    }

    public void setSoType(Integer soType) {
        this.soType = soType;
    }

    public String getMergeNo() {
        return mergeNo;
    }

    public void setMergeNo(String mergeNo) {
        this.mergeNo = mergeNo;
    }
}
