package com.deer.wms.ware.task.model.Sale;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/03/13.
 */
@ToString
public class SaleMasterCriteria extends QueryCriteria {
    private Integer saleMasterId;
    private String billNo;
    private Integer customerId;
    private Integer insertType;
    private Integer createUserId;
    private Integer state;
    private Integer saleState;

    public Integer getSaleMasterId() {
        return saleMasterId;
    }

    public void setSaleMasterId(Integer saleMasterId) {
        this.saleMasterId = saleMasterId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getInsertType() {
        return insertType;
    }

    public void setInsertType(Integer insertType) {
        this.insertType = insertType;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getSaleState() {
        return saleState;
    }

    public void setSaleState(Integer saleState) {
        this.saleState = saleState;
    }
}
