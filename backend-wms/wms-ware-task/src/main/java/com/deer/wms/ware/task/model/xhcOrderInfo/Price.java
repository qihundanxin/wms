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
public class Price {

    private double subtotal_tax;
    private int setting_id;
    private int org_shipping_fee;
    private int service_fee;
    private int product_discount_no_tax;
    private int product_discount_plus_tax;
    private int shipping_discount;
    private double subtotal;
    //private double grand_total;  /***/
    private int shipping_amount;
    private int total_paid;
    private double tax;
    public void setSubtotal_tax(double subtotal_tax) {
         this.subtotal_tax = subtotal_tax;
     }
     public double getSubtotal_tax() {
         return subtotal_tax;
     }

    public void setSetting_id(int setting_id) {
         this.setting_id = setting_id;
     }
     public int getSetting_id() {
         return setting_id;
     }

    public void setOrg_shipping_fee(int org_shipping_fee) {
         this.org_shipping_fee = org_shipping_fee;
     }
     public int getOrg_shipping_fee() {
         return org_shipping_fee;
     }

    public void setService_fee(int service_fee) {
         this.service_fee = service_fee;
     }
     public int getService_fee() {
         return service_fee;
     }

    public void setProduct_discount_no_tax(int product_discount_no_tax) {
         this.product_discount_no_tax = product_discount_no_tax;
     }
     public int getProduct_discount_no_tax() {
         return product_discount_no_tax;
     }

    public void setProduct_discount_plus_tax(int product_discount_plus_tax) {
         this.product_discount_plus_tax = product_discount_plus_tax;
     }
     public int getProduct_discount_plus_tax() {
         return product_discount_plus_tax;
     }

    public void setShipping_discount(int shipping_discount) {
         this.shipping_discount = shipping_discount;
     }
     public int getShipping_discount() {
         return shipping_discount;
     }

    public void setSubtotal(double subtotal) {
         this.subtotal = subtotal;
     }
     public double getSubtotal() {
         return subtotal;
     }

     /*berwin 老郭取值错了 2022-4
    public void setGrand_total(double grand_total) {
         this.grand_total = grand_total;
     }
     public double getGrand_total() {
         return grand_total;
     }*/

    public void setShipping_amount(int shipping_amount) {
         this.shipping_amount = shipping_amount;
     }
     public int getShipping_amount() {
         return shipping_amount;
     }

    public void setTotal_paid(int total_paid) {
         this.total_paid = total_paid;
     }
     public int getTotal_paid() {
         return total_paid;
     }

    public void setTax(double tax) {
         this.tax = tax;
     }
     public double getTax() {
         return tax;
     }

}