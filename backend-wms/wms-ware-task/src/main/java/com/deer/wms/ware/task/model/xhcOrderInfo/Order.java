/**
  * Copyright 2022 bejson.com 
  */
package com.deer.wms.ware.task.model.xhcOrderInfo;
import java.util.List;

import lombok.ToString;

/**
 * Auto-generated: 2022-03-03 15:27:49
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@ToString
public class Order {

    private long id;  /***/
//    private long customer_id;
    private int status;
    private long created_at;
    private String finished_at;   /***/
    private Data data;   /***/
    
    //2022-6-3
    private String payment_type;
    
//    private int shipping;
    private int shop_id;
    private double grand_total;
//    private String subtotal;
//    private String shipping_amount;
//    private String discount_amount;
//    private String total_paid;
//    private long delivery_time;
//    private long updated_at;
//    private String deleted_at;
//    private int type;
//    private String order_type;
//    private int pin_id;
//    private String preorder_reminder;
//    private int total_qty;
//    private String tax;
//    private String shipping_tax;
//    private int is_rating;
//    private String distance;
//    private int tips_rate;
//    private int tips_amount;
//    private int hurryup;
//    private String order_from;
//    private String charge_token;
//    private int payment_status;
//    private String payment_type;
//    private int free_type;
//    private String free_note;
//    private double customer_lat;
//    private double customer_lng;
//    private int cookingtime_set;
//    private long cookingtime_result;
//    private String shipping_type;
//    private int payment_fee;
//    private int city_id;
//    private int is_real_onlinepay;
//    private String locale;
//    private long expectant_late_time;
//    private long confirm_at;
//    private long assign_at;
//    private String pickup_at;
//    private int cooked_at;
//    private String delivered_at;
//    private String user_tags;
//    private int express_id;
//    private String payment_gateway;
//    private String membership_fee;
//    private int polling_status;
//    private int done_status;
//    private long contact_id;
//    private String batch_group_no;
//    private String shop_name;
//    private String shop_tel;
//    private Contact contact;
    private Comments comments;
//    private String gift;
//    private int customer_order_count;
    private int shop_daily_count;   /***/
//    private boolean paid;
//    private boolean preorder;
//    private double after_tax_total;
//    private String origin_text;
    private Payment payment;
      private Price price;
//    private List<String> adjust_price;
//    private List<String> cancel_fee;
//    private List<String> coupon_logs;
//    private String delivery_assign_from;
//    private int order_delivered;
//    private List<String> sys_settings;
//    private List<String> extra_driver_info;
//    private int is_polling;
//    private int remaining_time_secs;
//    private int shipping_amount_mult;
//    private int platform_rate;
//    private boolean driver_collection_enabled;
//    private int driver_give_shop_money;
//    private int customer_give_driver_money;
   // private Date local_time; /***/
//    private String time_zone;
//    private Date local_finished_at;
//    private String cookingtime_result_formatted;
//    private Date local_cookingtime_result;
//    private int shipping_discount;
//    private int free_driver_shipping_fee;
//    private String expectant_delivered_time;
//    private Compensation compensation;
//    private Driver_compensation driver_compensation;
//    private String local_delivered_at;
//    private int driver_pickup_ready;
//    private int driver_late_minutes;
//    private List<String> driver_info;
//    private int cooking_time_left;
//    private int clicked_cooking_time_delay;
//    private int shop_commission_amount;
//    private boolean is_first_order;
//    private Redeem_info redeem_info;
//    private String org_shipping_amount;
//    private int service_fee;
//    private String delay_polling_at;
//    private String reserve_datetime;
//    private String car_type;
//    private int utensils_amount;
    private List<Items> items;
//    private Shop shop;
//    private Deliverer deliverer;
//    private Customer customer;

    public void setGrand_total(double grand_total) {
        this.grand_total = grand_total;
    }
    public double getGrand_total() {
        return grand_total;
    }
    
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFinished_at() {
        return finished_at;
    }

    public void setFinished_at(String finished_at) {
        this.finished_at = finished_at;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    //2022-6-3
    //private String payment_type;
    public String getPayment_type() {
        return payment_type;
    }

    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }
    

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }
    
    

    public int getShop_daily_count() {
        return shop_daily_count;
    }

    public void setShop_daily_count(int shop_daily_count) {
        this.shop_daily_count = shop_daily_count;
    }
    

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}