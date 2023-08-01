/**
  * Copyright 2021 bejson.com 
  */
package com.deer.wms.ware.task.model.xhcOrderInfo;
import java.util.Date;

import lombok.ToString;

/**
 * Auto-generated: 2021-07-13 1:20:49
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@ToString
public class Shop_full_discount {

    private int id;
    private int shop_id;
    private String discount_code;
    private double discount_amount;
    private String discount_type;
    private String event;
    private int enabled;
    private Date start_date;
    private Date end_date;
    private Date created_at;
    private Date updated_at;
    private int batch_id;
    private int limit_times;
    private String use_type;
    private int festival_event_id;
    private String shipping_type;
    private int is_dailyonce;
    private String discount_group;
    private int order_type;
    private Options options;
    public void setId(int id) {
         this.id = id;
     }
     public int getId() {
         return id;
     }

    public void setShop_id(int shop_id) {
         this.shop_id = shop_id;
     }
     public int getShop_id() {
         return shop_id;
     }

    public void setDiscount_code(String discount_code) {
         this.discount_code = discount_code;
     }
     public String getDiscount_code() {
         return discount_code;
     }

    public void setDiscount_amount(double discount_amount) {
         this.discount_amount = discount_amount;
     }
     public double getDiscount_amount() {
         return discount_amount;
     }

    public void setDiscount_type(String discount_type) {
         this.discount_type = discount_type;
     }
     public String getDiscount_type() {
         return discount_type;
     }

    public void setEvent(String event) {
         this.event = event;
     }
     public String getEvent() {
         return event;
     }

    public void setEnabled(int enabled) {
         this.enabled = enabled;
     }
     public int getEnabled() {
         return enabled;
     }

    public void setStart_date(Date start_date) {
         this.start_date = start_date;
     }
     public Date getStart_date() {
         return start_date;
     }

    public void setEnd_date(Date end_date) {
         this.end_date = end_date;
     }
     public Date getEnd_date() {
         return end_date;
     }

    public void setCreated_at(Date created_at) {
         this.created_at = created_at;
     }
     public Date getCreated_at() {
         return created_at;
     }

    public void setUpdated_at(Date updated_at) {
         this.updated_at = updated_at;
     }
     public Date getUpdated_at() {
         return updated_at;
     }

    public void setBatch_id(int batch_id) {
         this.batch_id = batch_id;
     }
     public int getBatch_id() {
         return batch_id;
     }

    public void setLimit_times(int limit_times) {
         this.limit_times = limit_times;
     }
     public int getLimit_times() {
         return limit_times;
     }

    public void setUse_type(String use_type) {
         this.use_type = use_type;
     }
     public String getUse_type() {
         return use_type;
     }

    public void setFestival_event_id(int festival_event_id) {
         this.festival_event_id = festival_event_id;
     }
     public int getFestival_event_id() {
         return festival_event_id;
     }

    public void setShipping_type(String shipping_type) {
         this.shipping_type = shipping_type;
     }
     public String getShipping_type() {
         return shipping_type;
     }

    public void setIs_dailyonce(int is_dailyonce) {
         this.is_dailyonce = is_dailyonce;
     }
     public int getIs_dailyonce() {
         return is_dailyonce;
     }

    public void setDiscount_group(String discount_group) {
         this.discount_group = discount_group;
     }
     public String getDiscount_group() {
         return discount_group;
     }

    public void setOrder_type(int order_type) {
         this.order_type = order_type;
     }
     public int getOrder_type() {
         return order_type;
     }

    public void setOptions(Options options) {
         this.options = options;
     }
     public Options getOptions() {
         return options;
     }

}