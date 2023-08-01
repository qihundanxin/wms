package com.deer.wms.ware.task.model.Box;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.ToString;

@ToString
public class BoxDetails {
    @Id
    @Column(name = "box_detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer boxDetailId;

    /**
     * so单号
     */
    @Column(name = "bill_no")
    private String billNo;

    @Column(name = "item_code")
    private String itemCode;

    @Column(name = "item_name")
    private String itemName;

    private Double quantity;
    private Integer pickTaskId;
    private Double pickTaskQuantity;

    @Column(name = "pack_detail_id")
    private Integer packDetailId;

    @Column(name = "pack_describe")
    private String packDescribe;

    /**
     * 箱号
     */
    @Column(name = "box_no")
    private String boxNo;

    private String memo;

    @Column(name = "so_detail_id")
    private Integer soDetailId;

    @Column(name = "so_master_id")
    private Integer soMasterId;

    private Integer inventoryId;

    public Integer getPickTaskId() {
        return pickTaskId;
    }

    public void setPickTaskId(Integer pickTaskId) {
        this.pickTaskId = pickTaskId;
    }

    public Double getPickTaskQuantity() {
        return pickTaskQuantity;
    }

    public void setPickTaskQuantity(Double pickTaskQuantity) {
        this.pickTaskQuantity = pickTaskQuantity;
    }

    public Integer getBoxDetailId() {
        return boxDetailId;
    }

    public void setBoxDetailId(Integer boxDetailId) {
        this.boxDetailId = boxDetailId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
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

    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getSoDetailId() {
        return soDetailId;
    }

    public void setSoDetailId(Integer soDetailId) {
        this.soDetailId = soDetailId;
    }

    public Integer getSoMasterId() {
        return soMasterId;
    }

    public void setSoMasterId(Integer soMasterId) {
        this.soMasterId = soMasterId;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }
}
