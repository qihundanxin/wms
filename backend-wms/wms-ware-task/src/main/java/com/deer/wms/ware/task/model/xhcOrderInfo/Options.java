/**
  * Copyright 2021 bejson.com 
  */
package com.deer.wms.ware.task.model.xhcOrderInfo;

import lombok.ToString;

/**
 * Auto-generated: 2021-07-13 1:20:49
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@ToString
public class Options {

    private int platform_discount;
    private double merchant_discount;
    private String shop_name;
    private String shop_img;
    private String discount_tag;
    private String discount_desc;
    private String discount_memo;
    private int full_amount;
    private String gift_product;
    public void setPlatform_discount(int platform_discount) {
         this.platform_discount = platform_discount;
     }
     public int getPlatform_discount() {
         return platform_discount;
     }

    public void setMerchant_discount(double merchant_discount) {
         this.merchant_discount = merchant_discount;
     }
     public double getMerchant_discount() {
         return merchant_discount;
     }

    public void setShop_name(String shop_name) {
         this.shop_name = shop_name;
     }
     public String getShop_name() {
         return shop_name;
     }

    public void setShop_img(String shop_img) {
         this.shop_img = shop_img;
     }
     public String getShop_img() {
         return shop_img;
     }

    public void setDiscount_tag(String discount_tag) {
         this.discount_tag = discount_tag;
     }
     public String getDiscount_tag() {
         return discount_tag;
     }

    public void setDiscount_desc(String discount_desc) {
         this.discount_desc = discount_desc;
     }
     public String getDiscount_desc() {
         return discount_desc;
     }

    public void setDiscount_memo(String discount_memo) {
         this.discount_memo = discount_memo;
     }
     public String getDiscount_memo() {
         return discount_memo;
     }

    public void setFull_amount(int full_amount) {
         this.full_amount = full_amount;
     }
     public int getFull_amount() {
         return full_amount;
     }

    public void setGift_product(String gift_product) {
         this.gift_product = gift_product;
     }
     public String getGift_product() {
         return gift_product;
     }

}