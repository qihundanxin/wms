package com.deer.wms.ware.task.model.Return;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/03/13.
 */
@ToString
public class ReturnMasterCriteria extends QueryCriteria {
    private Integer returnMasterId;
    private String billNo;
    private String linkMan;
    private Integer supplierId;
    private Integer createUserId;
    private Integer insertType;
    private Integer carrierId;

    public Integer getReturnMasterId() {
        return returnMasterId;
    }

    public void setReturnMasterId(Integer returnMasterId) {
        this.returnMasterId = returnMasterId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
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

    public Integer getInsertType() {
        return insertType;
    }

    public void setInsertType(Integer insertType) {
        this.insertType = insertType;
    }

    public Integer getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Integer carrierId) {
        this.carrierId = carrierId;
    }
}
