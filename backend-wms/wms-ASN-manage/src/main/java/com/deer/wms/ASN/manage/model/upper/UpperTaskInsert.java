package com.deer.wms.ASN.manage.model.upper;

import java.util.List;

import com.deer.wms.busine.tactic.model.Upper.UpperTacticDetail;

import lombok.ToString;

@ToString
public class UpperTaskInsert extends UpperTacticDetail {

    private Integer billType;
    private String itemCode;
    private String itemName;
    private Integer organizationId;
    private String organizationName;
    private Integer wareId;
    private String wareName;
    private Integer batchId;
    private String batchName;
    private String fromBoxCode;
    private String fromCellCode;
    private String toBoxCode;
    private String toCellCode;
    private Integer packDetailId;
    private String packDescribe;
    private Double transRatio;
    private Double quantity;
    private Double toQuantity;
    private String memo;
    private Integer upperTaskUserId;
    private String upperTaskUserName;
    private String createTime;

    private String billNo;

    private List<String> serialNos;

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
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

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public List<String> getSerialNos() {
        return serialNos;
    }

    public void setSerialNos(List<String> serialNos) {
        this.serialNos = serialNos;
    }

    public Integer getPackDetailId() {
        return packDetailId;
    }

    public void setPackDetailId(Integer packDetailId) {
        this.packDetailId = packDetailId;
    }

    public String getPackDescribe() {
        return packDescribe;
    }

    public void setPackDescribe(String packDescribe) {
        this.packDescribe = packDescribe;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFromBoxCode() {
        return fromBoxCode;
    }

    public void setFromBoxCode(String fromBoxCode) {
        this.fromBoxCode = fromBoxCode;
    }

    @Override
    public String getFromCellCode() {
        return fromCellCode;
    }

    @Override
    public void setFromCellCode(String fromCellCode) {
        this.fromCellCode = fromCellCode;
    }

    public String getToBoxCode() {
        return toBoxCode;
    }

    public void setToBoxCode(String toBoxCode) {
        this.toBoxCode = toBoxCode;
    }

    @Override
    public String getToCellCode() {
        return toCellCode;
    }

    @Override
    public void setToCellCode(String toCellCode) {
        this.toCellCode = toCellCode;
    }

    public Integer getUpperTaskUserId() {
        return upperTaskUserId;
    }

    public void setUpperTaskUserId(Integer upperTaskUserId) {
        this.upperTaskUserId = upperTaskUserId;
    }

    public String getUpperTaskUserName() {
        return upperTaskUserName;
    }

    public void setUpperTaskUserName(String upperTaskUserName) {
        this.upperTaskUserName = upperTaskUserName;
    }

    public Double getToQuantity() {
        return toQuantity;
    }

    public void setToQuantity(Double toQuantity) {
        this.toQuantity = toQuantity;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Double getTransRatio() {
        return transRatio;
    }

    public void setTransRatio(Double transRatio) {
        this.transRatio = transRatio;
    }
}
