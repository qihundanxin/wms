package com.deer.wms.base.system.model.Item;

import java.util.List;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2019/11/19.
 */
@ToString
public class ItemInfoCriteria extends QueryCriteria {
    private String itemCode;
    private String itemName;
    private String itemTypeCode;
    private Integer itemTypeId;
    private String itemTypeName;
    private String memo;
    private String supplierCode;
    private String spec;
    private String model;
    private String organizationName;
    private String packCode;
    private String packName;
    private Integer supplierId;
    private String supplierName;
    private String batchTacticCode;
    private String upperTacticCode;
    private String qcTacticCode;
    private String abcLevel;
    private List<Integer> ids;
    private String itemStatus;
    private Integer noUpc;
    private Boolean weighting;
    private Boolean unpacking;
    
    public Boolean getWeighting() {
        return weighting;
    }

    public void setWeighting(Boolean weighting) {
        this.weighting = weighting;
    }

    public Boolean getUnpacking() {
        return unpacking;
    }

    public void setUnpacking(Boolean unpacking) {
        this.unpacking = unpacking;
    }

    public Integer getNoUpc() {
        return noUpc;
    }

    public void setNoUpc(Integer noUpc) {
        this.noUpc = noUpc;
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

    public String getItemTypeCode() {
        return itemTypeCode;
    }

    public void setItemTypeCode(String itemTypeCode) {
        this.itemTypeCode = itemTypeCode;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

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

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getBatchTacticCode() {
        return batchTacticCode;
    }

    public void setBatchTacticCode(String batchTacticCode) {
        this.batchTacticCode = batchTacticCode;
    }

    public String getUpperTacticCode() {
        return upperTacticCode;
    }

    public void setUpperTacticCode(String upperTacticCode) {
        this.upperTacticCode = upperTacticCode;
    }

    public String getAbcLevel() {
        return abcLevel;
    }

    public void setAbcLevel(String abcLevel) {
        this.abcLevel = abcLevel;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public String getQcTacticCode() {
        return qcTacticCode;
    }

    public void setQcTacticCode(String qcTacticCode) {
        this.qcTacticCode = qcTacticCode;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }
}
