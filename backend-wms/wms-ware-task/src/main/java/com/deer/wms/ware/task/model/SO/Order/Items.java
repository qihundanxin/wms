/**
  * Copyright 2021 bejson.com 
  */
package com.deer.wms.ware.task.model.SO.Order;
import java.util.Date;
import java.util.List;

import lombok.ToString;

/**
 * Auto-generated: 2021-08-22 11:15:55
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@ToString
public class Items {

    private String deliveryType;
    private int saleOrderItemNo;
    private String purchaseOrderItemsNo;
    private Date customerRequireDeliveryDate;
    private Date customerDeliveryDate;
    private Date storageDate;
    private String partDesc;
    private int qtyInStorage;
    private int qtyNotInStorage;
    private long customerPartCode;
    private String customerPartDesc;
    private String partNo;
    private String partOuterNo;
    private String oldCodeWithDots;
    private String oldCode;
    private int saleOrderQty;
    private String unit;
    private int factory;
    private String saleOrderStatus;
    private String saleOrderItemNote;
    private String outstockBillTy;
    private String tdInvoiceNo;
    private String customerIdCode;
    private String deliveryRequest;
    private int qtyOutStock;
    private int qtyNotOutStock;
    private String partVer;
    private int cancelQty;
    private List<ScheduleLines> scheduleLines;
    public void setDeliveryType(String deliveryType) {
         this.deliveryType = deliveryType;
     }
     public String getDeliveryType() {
         return deliveryType;
     }

    public void setSaleOrderItemNo(int saleOrderItemNo) {
         this.saleOrderItemNo = saleOrderItemNo;
     }
     public int getSaleOrderItemNo() {
         return saleOrderItemNo;
     }

    public void setPurchaseOrderItemsNo(String purchaseOrderItemsNo) {
         this.purchaseOrderItemsNo = purchaseOrderItemsNo;
     }
     public String getPurchaseOrderItemsNo() {
         return purchaseOrderItemsNo;
     }

    public void setCustomerRequireDeliveryDate(Date customerRequireDeliveryDate) {
         this.customerRequireDeliveryDate = customerRequireDeliveryDate;
     }
     public Date getCustomerRequireDeliveryDate() {
         return customerRequireDeliveryDate;
     }

    public void setCustomerDeliveryDate(Date customerDeliveryDate) {
         this.customerDeliveryDate = customerDeliveryDate;
     }
     public Date getCustomerDeliveryDate() {
         return customerDeliveryDate;
     }

    public void setStorageDate(Date storageDate) {
         this.storageDate = storageDate;
     }
     public Date getStorageDate() {
         return storageDate;
     }

    public void setPartDesc(String partDesc) {
         this.partDesc = partDesc;
     }
     public String getPartDesc() {
         return partDesc;
     }

    public void setQtyInStorage(int qtyInStorage) {
         this.qtyInStorage = qtyInStorage;
     }
     public int getQtyInStorage() {
         return qtyInStorage;
     }

    public void setQtyNotInStorage(int qtyNotInStorage) {
         this.qtyNotInStorage = qtyNotInStorage;
     }
     public int getQtyNotInStorage() {
         return qtyNotInStorage;
     }

    public void setCustomerPartCode(long customerPartCode) {
         this.customerPartCode = customerPartCode;
     }
     public long getCustomerPartCode() {
         return customerPartCode;
     }

    public void setCustomerPartDesc(String customerPartDesc) {
         this.customerPartDesc = customerPartDesc;
     }
     public String getCustomerPartDesc() {
         return customerPartDesc;
     }

    public void setPartNo(String partNo) {
         this.partNo = partNo;
     }
     public String getPartNo() {
         return partNo;
     }

    public void setPartOuterNo(String partOuterNo) {
         this.partOuterNo = partOuterNo;
     }
     public String getPartOuterNo() {
         return partOuterNo;
     }

    public void setOldCodeWithDots(String oldCodeWithDots) {
         this.oldCodeWithDots = oldCodeWithDots;
     }
     public String getOldCodeWithDots() {
         return oldCodeWithDots;
     }

    public void setOldCode(String oldCode) {
         this.oldCode = oldCode;
     }
     public String getOldCode() {
         return oldCode;
     }

    public void setSaleOrderQty(int saleOrderQty) {
         this.saleOrderQty = saleOrderQty;
     }
     public int getSaleOrderQty() {
         return saleOrderQty;
     }

    public void setUnit(String unit) {
         this.unit = unit;
     }
     public String getUnit() {
         return unit;
     }

    public void setFactory(int factory) {
         this.factory = factory;
     }
     public int getFactory() {
         return factory;
     }

    public void setSaleOrderStatus(String saleOrderStatus) {
         this.saleOrderStatus = saleOrderStatus;
     }
     public String getSaleOrderStatus() {
         return saleOrderStatus;
     }

    public void setSaleOrderItemNote(String saleOrderItemNote) {
         this.saleOrderItemNote = saleOrderItemNote;
     }
     public String getSaleOrderItemNote() {
         return saleOrderItemNote;
     }

    public void setOutstockBillTy(String outstockBillTy) {
         this.outstockBillTy = outstockBillTy;
     }
     public String getOutstockBillTy() {
         return outstockBillTy;
     }

    public void setTdInvoiceNo(String tdInvoiceNo) {
         this.tdInvoiceNo = tdInvoiceNo;
     }
     public String getTdInvoiceNo() {
         return tdInvoiceNo;
     }

    public void setCustomerIdCode(String customerIdCode) {
         this.customerIdCode = customerIdCode;
     }
     public String getCustomerIdCode() {
         return customerIdCode;
     }

    public void setDeliveryRequest(String deliveryRequest) {
         this.deliveryRequest = deliveryRequest;
     }
     public String getDeliveryRequest() {
         return deliveryRequest;
     }

    public void setQtyOutStock(int qtyOutStock) {
         this.qtyOutStock = qtyOutStock;
     }
     public int getQtyOutStock() {
         return qtyOutStock;
     }

    public void setQtyNotOutStock(int qtyNotOutStock) {
         this.qtyNotOutStock = qtyNotOutStock;
     }
     public int getQtyNotOutStock() {
         return qtyNotOutStock;
     }

    public void setPartVer(String partVer) {
         this.partVer = partVer;
     }
     public String getPartVer() {
         return partVer;
     }

    public void setCancelQty(int cancelQty) {
         this.cancelQty = cancelQty;
     }
     public int getCancelQty() {
         return cancelQty;
     }

    public void setScheduleLines(List<ScheduleLines> scheduleLines) {
         this.scheduleLines = scheduleLines;
     }
     public List<ScheduleLines> getScheduleLines() {
         return scheduleLines;
     }

}