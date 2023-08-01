package com.deer.wms.base.system.model.Supplier;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2019/11/21.
 */
@ToString
public class SupplierManageCriteria extends QueryCriteria {
    private Integer supplierId;
    private String supplierCode;
    private String supplierName;
    private String linkMan;
    private String linkPhone;
    private String supplierSite;

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getSupplierSite() {
        return supplierSite;
    }

    public void setSupplierSite(String supplierSite) {
        this.supplierSite = supplierSite;
    }
}
