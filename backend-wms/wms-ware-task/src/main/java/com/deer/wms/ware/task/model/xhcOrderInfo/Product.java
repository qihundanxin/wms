/**
  * Copyright 2022 bejson.com 
  */
package com.deer.wms.ware.task.model.xhcOrderInfo;

import lombok.ToString;

/**
 * Auto-generated: 2022-03-03 15:27:49
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@ToString
public class Product {

    private int shop_id;
    private long revision_id;
    private int position;
    private long id;
    private int status;
    private int stock;
    private int min;
    private int max;
    private String sku;
    private boolean manage_stock;
    public void setShop_id(int shop_id) {
         this.shop_id = shop_id;
     }
     public int getShop_id() {
         return shop_id;
     }

    public void setRevision_id(long revision_id) {
         this.revision_id = revision_id;
     }
     public long getRevision_id() {
         return revision_id;
     }

    public void setPosition(int position) {
         this.position = position;
     }
     public int getPosition() {
         return position;
     }

    public void setId(long id) {
         this.id = id;
     }
     public long getId() {
         return id;
     }

    public void setStatus(int status) {
         this.status = status;
     }
     public int getStatus() {
         return status;
     }

    public void setStock(int stock) {
         this.stock = stock;
     }
     public int getStock() {
         return stock;
     }

    public void setMin(int min) {
         this.min = min;
     }
     public int getMin() {
         return min;
     }

    public void setMax(int max) {
         this.max = max;
     }
     public int getMax() {
         return max;
     }

    public void setSku(String sku) {
         this.sku = sku;
     }
     public String getSku() {
         return sku;
     }

    public void setManage_stock(boolean manage_stock) {
         this.manage_stock = manage_stock;
     }
     public boolean getManage_stock() {
         return manage_stock;
     }

}