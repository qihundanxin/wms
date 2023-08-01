/**
  * Copyright 2021 bejson.com 
  */
package com.deer.wms.ware.task.model.SO.Order;
import java.util.List;

import lombok.ToString;

/**
 * Auto-generated: 2021-08-22 11:15:55
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@ToString
public class OrderInfo {

    private long saleOrderNo;
    private long customerNo;
    private String customerName;
    private long deliveryPartyNo;
    private String deliveryPartyName;
    private String purchaseOrderNo;
    private String endCustomerOrderNo;
    private String endCustomerAbbreviation;
    private String salesMan;
    private String creator;
    private String createDate;
    private int distribution;
    private String checkTime;
    private String saleOrderType;
    private String customerAbbreviation;
    private String exportOrderNo;
    private String shippingMark;
    private List<Items> items;
    public void setSaleOrderNo(long saleOrderNo) {
         this.saleOrderNo = saleOrderNo;
     }
     public long getSaleOrderNo() {
         return saleOrderNo;
     }

    public void setCustomerNo(long customerNo) {
         this.customerNo = customerNo;
     }
     public long getCustomerNo() {
         return customerNo;
     }

    public void setCustomerName(String customerName) {
         this.customerName = customerName;
     }
     public String getCustomerName() {
         return customerName;
     }

    public void setDeliveryPartyNo(long deliveryPartyNo) {
         this.deliveryPartyNo = deliveryPartyNo;
     }
     public long getDeliveryPartyNo() {
         return deliveryPartyNo;
     }

    public void setDeliveryPartyName(String deliveryPartyName) {
         this.deliveryPartyName = deliveryPartyName;
     }
     public String getDeliveryPartyName() {
         return deliveryPartyName;
     }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
         this.purchaseOrderNo = purchaseOrderNo;
     }
     public String getPurchaseOrderNo() {
         return purchaseOrderNo;
     }

    public void setEndCustomerOrderNo(String endCustomerOrderNo) {
         this.endCustomerOrderNo = endCustomerOrderNo;
     }
     public String getEndCustomerOrderNo() {
         return endCustomerOrderNo;
     }

    public void setEndCustomerAbbreviation(String endCustomerAbbreviation) {
         this.endCustomerAbbreviation = endCustomerAbbreviation;
     }
     public String getEndCustomerAbbreviation() {
         return endCustomerAbbreviation;
     }

    public void setSalesMan(String salesMan) {
         this.salesMan = salesMan;
     }
     public String getSalesMan() {
         return salesMan;
     }

    public void setCreator(String creator) {
         this.creator = creator;
     }
     public String getCreator() {
         return creator;
     }

    public void setDistribution(int distribution) {
         this.distribution = distribution;
     }
     public int getDistribution() {
         return distribution;
     }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public void setSaleOrderType(String saleOrderType) {
         this.saleOrderType = saleOrderType;
     }
     public String getSaleOrderType() {
         return saleOrderType;
     }

    public void setCustomerAbbreviation(String customerAbbreviation) {
         this.customerAbbreviation = customerAbbreviation;
     }
     public String getCustomerAbbreviation() {
         return customerAbbreviation;
     }

    public void setExportOrderNo(String exportOrderNo) {
         this.exportOrderNo = exportOrderNo;
     }
     public String getExportOrderNo() {
         return exportOrderNo;
     }

    public void setShippingMark(String shippingMark) {
         this.shippingMark = shippingMark;
     }
     public String getShippingMark() {
         return shippingMark;
     }

    public void setItems(List<Items> items) {
         this.items = items;
     }
     public List<Items> getItems() {
         return items;
     }

}