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
public class Deliverer {

    private int deliverer_id;
    private long order_id;
    private long created_at;
    private long updated_at;
    private int sorted_at;
    private String entrepot;
    private int package_id;
    private int status;
    private String delivered_at;
    private String order_from;
    private int on_duty;
    private String driver_lat;
    private String driver_lng;
    private String driver_lat_pickup;
    private String driver_lng_pickup;
    private int points_distance;
    private int pickup_distance;
    private String driver_avatar;
    private String driver_tel;
    private int driver_lv;
    private String driver_nickname;
    public void setDeliverer_id(int deliverer_id) {
         this.deliverer_id = deliverer_id;
     }
     public int getDeliverer_id() {
         return deliverer_id;
     }

    public void setOrder_id(long order_id) {
         this.order_id = order_id;
     }
     public long getOrder_id() {
         return order_id;
     }

    public void setCreated_at(long created_at) {
         this.created_at = created_at;
     }
     public long getCreated_at() {
         return created_at;
     }

    public void setUpdated_at(long updated_at) {
         this.updated_at = updated_at;
     }
     public long getUpdated_at() {
         return updated_at;
     }

    public void setSorted_at(int sorted_at) {
         this.sorted_at = sorted_at;
     }
     public int getSorted_at() {
         return sorted_at;
     }

    public void setEntrepot(String entrepot) {
         this.entrepot = entrepot;
     }
     public String getEntrepot() {
         return entrepot;
     }

    public void setPackage_id(int package_id) {
         this.package_id = package_id;
     }
     public int getPackage_id() {
         return package_id;
     }

    public void setStatus(int status) {
         this.status = status;
     }
     public int getStatus() {
         return status;
     }

    public void setDelivered_at(String delivered_at) {
         this.delivered_at = delivered_at;
     }
     public String getDelivered_at() {
         return delivered_at;
     }

    public void setOrder_from(String order_from) {
         this.order_from = order_from;
     }
     public String getOrder_from() {
         return order_from;
     }

    public void setOn_duty(int on_duty) {
         this.on_duty = on_duty;
     }
     public int getOn_duty() {
         return on_duty;
     }

    public void setDriver_lat(String driver_lat) {
         this.driver_lat = driver_lat;
     }
     public String getDriver_lat() {
         return driver_lat;
     }

    public void setDriver_lng(String driver_lng) {
         this.driver_lng = driver_lng;
     }
     public String getDriver_lng() {
         return driver_lng;
     }

    public void setDriver_lat_pickup(String driver_lat_pickup) {
         this.driver_lat_pickup = driver_lat_pickup;
     }
     public String getDriver_lat_pickup() {
         return driver_lat_pickup;
     }

    public void setDriver_lng_pickup(String driver_lng_pickup) {
         this.driver_lng_pickup = driver_lng_pickup;
     }
     public String getDriver_lng_pickup() {
         return driver_lng_pickup;
     }

    public void setPoints_distance(int points_distance) {
         this.points_distance = points_distance;
     }
     public int getPoints_distance() {
         return points_distance;
     }

    public void setPickup_distance(int pickup_distance) {
         this.pickup_distance = pickup_distance;
     }
     public int getPickup_distance() {
         return pickup_distance;
     }

    public void setDriver_avatar(String driver_avatar) {
         this.driver_avatar = driver_avatar;
     }
     public String getDriver_avatar() {
         return driver_avatar;
     }

    public void setDriver_tel(String driver_tel) {
         this.driver_tel = driver_tel;
     }
     public String getDriver_tel() {
         return driver_tel;
     }

    public void setDriver_lv(int driver_lv) {
         this.driver_lv = driver_lv;
     }
     public int getDriver_lv() {
         return driver_lv;
     }

    public void setDriver_nickname(String driver_nickname) {
         this.driver_nickname = driver_nickname;
     }
     public String getDriver_nickname() {
         return driver_nickname;
     }

}