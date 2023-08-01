package com.deer.wms.base.system.model.Item;

import java.util.List;

import com.deer.wms.base.system.model.ItemUpc;
import com.deer.wms.base.system.model.Pack.PackDetail;

import lombok.ToString;

@ToString
public class ItemInfoDto extends ItemInfo {

    private String itemTypeName;

    private String organizationName;

    private String packName;

    private String supplierName;

    private String supplierCode;

    private String batchTacticName;

    private String upperTacticName;

    private String qcTacticName;

    private String description;

    private String turnoverDescription;

    private List<PackDetail> packDetails;

    private List<ItemUpc> upcs;

    public List<PackDetail> getPackDetails() {
        return packDetails;
    }

    public void setPackDetails(List<PackDetail> packDetails) {
        this.packDetails = packDetails;
    }

    public List<ItemUpc> getUpcs() {
        return upcs;
    }

    public void setUpcs(List<ItemUpc> upcs) {
        this.upcs = upcs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
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

    public String getBatchTacticName() {
        return batchTacticName;
    }

    public void setBatchTacticName(String batchTacticName) {
        this.batchTacticName = batchTacticName;
    }

    public String getUpperTacticName() {
        return upperTacticName;
    }

    public void setUpperTacticName(String upperTacticName) {
        this.upperTacticName = upperTacticName;
    }

    public String getQcTacticName() {
        return qcTacticName;
    }

    public void setQcTacticName(String qcTacticName) {
        this.qcTacticName = qcTacticName;
    }

    public String getTurnoverDescription() {
        return turnoverDescription;
    }

    public void setTurnoverDescription(String turnoverDescription) {
        this.turnoverDescription = turnoverDescription;
    }
}
