package com.deer.wms.ware.task.model.Sale;

import java.util.List;

import lombok.ToString;

@ToString
public class SaleData {
    private SaleMaster saleMaster;
    private List<SaleDetail> saleDetails;
    private Integer insertType;  //0-提交  -2保存为草稿
    private Integer billSource; //单据来源

    public SaleMaster getSaleMaster() {
        return saleMaster;
    }

    public void setSaleMaster(SaleMaster saleMaster) {
        this.saleMaster = saleMaster;
    }

    public List<SaleDetail> getSaleDetails() {
        return saleDetails;
    }

    public void setSaleDetails(List<SaleDetail> saleDetails) {
        this.saleDetails = saleDetails;
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
