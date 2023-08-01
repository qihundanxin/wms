package com.deer.wms.ware.task.model.SO.CreateSo;

import java.util.List;

import lombok.ToString;

@ToString
public class CreateSo {
    private String billNo;//关联单号
    private String wareCode;//仓库代码
    private String orgCode;//货主代码
    private String orderTime;//下单时间
    private String expressBillUrl;//快递电子面单地址
    private String shipCode;//物流公司
    private String shipBillCode;//快递单号
    private String tradeNo;//交易号
    private String shop;//店铺
    private String cnDeclarationName;//中文申报品名
    private String declarationName;//英文申报品名
    private Double codValue;// 货到付款金额
    private CreateSoDeliveryAddress deliveryAddress;//收货地址
    private CreateSoShippingAddress shippingAddress;//发货地址
    private List<CreateSoSkuDetail> soSkuDetails;//产品信息
    private String lastMileSortingCode;
    private String lastMileDestination;
    private String lastMileLineCode;
    private String ext1;
    private String ext2;
    private String ext3;

    public Double getCodValue() {
        return codValue;
    }

    public void setCodValue(Double codValue) {
        this.codValue = codValue;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public String getLastMileSortingCode() {
        return lastMileSortingCode;
    }

    public void setLastMileSortingCode(String lastMileSortingCode) {
        this.lastMileSortingCode = lastMileSortingCode;
    }

    public String getLastMileDestination() {
        return lastMileDestination;
    }

    public void setLastMileDestination(String lastMileDestination) {
        this.lastMileDestination = lastMileDestination;
    }

    public String getLastMileLineCode() {
        return lastMileLineCode;
    }

    public void setLastMileLineCode(String lastMileLineCode) {
        this.lastMileLineCode = lastMileLineCode;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getWareCode() {
        return wareCode;
    }

    public void setWareCode(String wareCode) {
        this.wareCode = wareCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getExpressBillUrl() {
        return expressBillUrl;
    }

    public void setExpressBillUrl(String expressBillUrl) {
        this.expressBillUrl = expressBillUrl;
    }

    public String getShipCode() {
        return shipCode;
    }

    public void setShipCode(String shipCode) {
        this.shipCode = shipCode;
    }

    public String getShipBillCode() {
        return shipBillCode;
    }

    public void setShipBillCode(String shipBillCode) {
        this.shipBillCode = shipBillCode;
    }

    public CreateSoDeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(CreateSoDeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public CreateSoShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(CreateSoShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<CreateSoSkuDetail> getSoSkuDetails() {
        return soSkuDetails;
    }

    public void setSoSkuDetails(List<CreateSoSkuDetail> soSkuDetails) {
        this.soSkuDetails = soSkuDetails;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getCnDeclarationName() {
        return cnDeclarationName;
    }

    public void setCnDeclarationName(String cnDeclarationName) {
        this.cnDeclarationName = cnDeclarationName;
    }

    public String getDeclarationName() {
        return declarationName;
    }

    public void setDeclarationName(String declarationName) {
        this.declarationName = declarationName;
    }
}
