package com.deer.wms.ASN.manage.model.asn;

import java.util.List;

import com.deer.wms.ASN.manage.model.buy.PackDetails;
import com.deer.wms.base.system.model.ItemUpc;

import lombok.ToString;

@ToString
public class AsnDetailDto extends AsnDetail {

    private String url;  //SKU采购链接
    private String packName;
    private Integer itemTypeId;
    private String itemTypeName;

    private Integer supplierId;
    private String supplierCode;
    private String supplierName;
    private Integer organizationId;
    private String organizationName;
    private Integer wareId;
    private String wareName;
    private Integer billType;
    private String asnBillNo;

    private List<ItemUpc> upclist;

    private String createTime;

    /**
     * 主单状态
     */
    private Integer asnState;

    /**
     * 优先级
     */
    private Integer orderNo;

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getAsnState() {
        return asnState;
    }

    public void setAsnState(Integer asnState) {
        this.asnState = asnState;
    }

    private String expectTime;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ItemUpc> getUpclist() {
        return upclist;
    }

    public void setUpclist(List<ItemUpc> upclist) {
        this.upclist = upclist;
    }

    public String getAsnBillNo() {
        return asnBillNo;
    }

    public void setAsnBillNo(String asnBillNo) {
        this.asnBillNo = asnBillNo;
    }

    private List<PackDetails> packDetails;

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public Integer getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Integer itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public List<PackDetails> getPackDetails() {
        return packDetails;
    }

    public void setPackDetails(List<PackDetails> packDetails) {
        this.packDetails = packDetails;
    }

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

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Integer getWareId() {
        return wareId;
    }

    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }

    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }
}
