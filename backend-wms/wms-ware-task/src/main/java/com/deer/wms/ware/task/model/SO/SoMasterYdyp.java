package com.deer.wms.ware.task.model.SO;

import lombok.ToString;

@ToString
public class SoMasterYdyp {

    private Integer soDetailId;

    private String billNo;
    private String shipBillCode;

    private Integer state;

    private String itemCode;

    private String itemName;

    private String spec;

    private String model;

    private String imgUrl;

    private Double orderQuantity;

    private Double allottedQuantity;

    private Double pickQuantity;

    private Double checkQuantity;

    private Double deliveryQuantity;

    public String getShipBillCode() {
        return shipBillCode;
    }

    public void setShipBillCode(String shipBillCode) {
        this.shipBillCode = shipBillCode;
    }

    public Double getDeliveryQuantity() {
        return deliveryQuantity;
    }

    public void setDeliveryQuantity(Double deliveryQuantity) {
        this.deliveryQuantity = deliveryQuantity;
    }

    public Integer getSoDetailId() {
        return soDetailId;
    }

    public void setSoDetailId(Integer soDetailId) {
        this.soDetailId = soDetailId;
    }

    public Double getCheckQuantity() {
        return checkQuantity;
    }

    public void setCheckQuantity(Double checkQuantity) {
        this.checkQuantity = checkQuantity;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Double getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Double orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public Double getAllottedQuantity() {
        return allottedQuantity;
    }

    public void setAllottedQuantity(Double allottedQuantity) {
        this.allottedQuantity = allottedQuantity;
    }

    public Double getPickQuantity() {
        return pickQuantity;
    }

    public void setPickQuantity(Double pickQuantity) {
        this.pickQuantity = pickQuantity;
    }
}
