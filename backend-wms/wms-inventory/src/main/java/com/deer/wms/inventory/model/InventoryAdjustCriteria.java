package com.deer.wms.inventory.model;

import java.util.List;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/02/13.
 */
@ToString
public class InventoryAdjustCriteria extends QueryCriteria {
    private Integer stockTakeMasterId;
    private String organizationName;
    private String billNo;
    private String reason;
    private String reasonDescription;
    private String wareName;
    private String wareCode;
    private String memo;
    private Integer type;
    private Integer inventoryId;
    private Double adjustQuantity;
    private Double quantity;
    private Integer inventoryAdjustId;
    private Integer insertType;
    private Integer caseState;
    private String startCreateTime;
    private String endCreateTime;

    private InventoryAdjust inventoryAdjust;
    private List<InventoryAdjustDetail> adjustDetails;

    public InventoryAdjust getInventoryAdjust() {
        return inventoryAdjust;
    }

    public void setInventoryAdjust(InventoryAdjust inventoryAdjust) {
        this.inventoryAdjust = inventoryAdjust;
    }

    public List<InventoryAdjustDetail> getAdjustDetails() {
        return adjustDetails;
    }

    public void setAdjustDetails(List<InventoryAdjustDetail> adjustDetails) {
        this.adjustDetails = adjustDetails;
    }

    public String getStartCreateTime() {
        return startCreateTime;
    }

    public void setStartCreateTime(String startCreateTime) {
        this.startCreateTime = startCreateTime;
    }

    public String getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(String endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    public Integer getCaseState() {
        return caseState;
    }

    public void setCaseState(Integer caseState) {
        this.caseState = caseState;
    }

    public Integer getInsertType() {
        return insertType;
    }

    public void setInsertType(Integer insertType) {
        this.insertType = insertType;
    }

    public Integer getInventoryAdjustId() {
        return inventoryAdjustId;
    }

    public void setInventoryAdjustId(Integer inventoryAdjustId) {
        this.inventoryAdjustId = inventoryAdjustId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getAdjustQuantity() {
        return adjustQuantity;
    }

    public void setAdjustQuantity(Double adjustQuantity) {
        this.adjustQuantity = adjustQuantity;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStockTakeMasterId() {
        return stockTakeMasterId;
    }

    public void setStockTakeMasterId(Integer stockTakeMasterId) {
        this.stockTakeMasterId = stockTakeMasterId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReasonDescription() {
        return reasonDescription;
    }

    public void setReasonDescription(String reasonDescription) {
        this.reasonDescription = reasonDescription;
    }

    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }

    public String getWareCode() {
        return wareCode;
    }

    public void setWareCode(String wareCode) {
        this.wareCode = wareCode;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
