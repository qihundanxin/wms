package com.deer.wms.inventory.model.freeze;

import java.util.List;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2019/12/25.
 */
@ToString
public class FreezeDetailCriteria extends QueryCriteria {
    private Integer freezeDetailId;
    private Integer inventoryId;
    private Integer itemTypeId;
    private Integer batchId;
    private Integer createUseId;
    private String createTime;
    private String billNo;
    private String itemTypeName;
    private String itemCode;
    private Integer state;
    private Integer packDetailId;
    private String detailNo;
    private Double quantity;

    private String cellCode;

    private String freezeDescribe;

    private List<String> billNos;

    private String startCreateTime;

    private String endCreateTime;

    private Integer wareId;

    /**
     * 排序 ASC 时间正序，DESC 时间倒序
     */
    private String sort;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public Integer getWareId() {
        return wareId;
    }

    @Override
    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }

    public List<String> getBillNos() {
        return billNos;
    }

    public void setBillNos(List<String> billNos) {
        this.billNos = billNos;
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

    public String getCellCode() {
        return cellCode;
    }

    public void setCellCode(String cellCode) {
        this.cellCode = cellCode;
    }

    public String getFreezeDescribe() {
        return freezeDescribe;
    }

    public void setFreezeDescribe(String freezeDescribe) {
        this.freezeDescribe = freezeDescribe;
    }

    public Integer getFreezeDetailId() {
        return freezeDetailId;
    }

    public void setFreezeDetailId(Integer freezeDetailId) {
        this.freezeDetailId = freezeDetailId;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Integer itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public Integer getCreateUseId() {
        return createUseId;
    }

    public void setCreateUseId(Integer createUseId) {
        this.createUseId = createUseId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getPackDetailId() {
        return packDetailId;
    }

    public void setPackDetailId(Integer packDetailId) {
        this.packDetailId = packDetailId;
    }

    public String getDetailNo() {
        return detailNo;
    }

    public void setDetailNo(String detailNo) {
        this.detailNo = detailNo;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
