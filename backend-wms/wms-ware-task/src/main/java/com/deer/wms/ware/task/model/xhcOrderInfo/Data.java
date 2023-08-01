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
public class Data {

    private Price price;  /***/
//    private Contact contact;
//    private int customer_order_count;
//    private int shop_daily_count;
    private Payment payment;   /***/
//    private Shop shop;
//    private double city_tax_rate;
//    private String time_zone;
//    private Reward_deduct reward_deduct;
//    private int free_driver_shipping_fee;
//    private String device_token;
//    private boolean is_first_order;
    /**
     * {@link Shipping_directions} 由于PQ给的数据类型不固定，这里使用
     */
    private String shipping_directions;
//    private int tableware;
//    private String expectant_delivered_time;
//    private Comments comments;
//    private int platform_rate;
//    private boolean driver_collection_enabled;
//    private Addition addition;
//    private String moneris_receipt;
//    private String remark_url;
//    private long polling_done;

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getShipping_directions() {
        return shipping_directions;
    }

    public void setShipping_directions(String shipping_directions) {
        this.shipping_directions = shipping_directions;
    }
}