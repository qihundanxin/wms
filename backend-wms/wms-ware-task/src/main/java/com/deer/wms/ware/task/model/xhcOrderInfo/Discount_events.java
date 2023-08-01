/**
  * Copyright 2021 bejson.com 
  */
package com.deer.wms.ware.task.model.xhcOrderInfo;
import java.util.List;

import lombok.ToString;

/**
 * Auto-generated: 2021-07-13 1:20:49
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@ToString
public class Discount_events {

    private List<Shop_product_sale> shop_product_sale;
    private List<Shop_full_discount> shop_full_discount;
    public void setShop_product_sale(List<Shop_product_sale> shop_product_sale) {
         this.shop_product_sale = shop_product_sale;
     }
     public List<Shop_product_sale> getShop_product_sale() {
         return shop_product_sale;
     }

    public void setShop_full_discount(List<Shop_full_discount> shop_full_discount) {
         this.shop_full_discount = shop_full_discount;
     }
     public List<Shop_full_discount> getShop_full_discount() {
         return shop_full_discount;
     }

}