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
public class Shipping_directions {

    private int distance;
    private int duration;
    private String start_address;
    private double start_address_lat;
    private double start_address_lng;
    private String end_address;  /***/
    private double end_address_lat;
    private double end_address_lng;
    private String polyline;
    private List<String> waypoint_order;
    private String gateway;
    private long time;
    public void setDistance(int distance) {
         this.distance = distance;
     }
     public int getDistance() {
         return distance;
     }

    public void setDuration(int duration) {
         this.duration = duration;
     }
     public int getDuration() {
         return duration;
     }

    public void setStart_address(String start_address) {
         this.start_address = start_address;
     }
     public String getStart_address() {
         return start_address;
     }

    public void setStart_address_lat(double start_address_lat) {
         this.start_address_lat = start_address_lat;
     }
     public double getStart_address_lat() {
         return start_address_lat;
     }

    public void setStart_address_lng(double start_address_lng) {
         this.start_address_lng = start_address_lng;
     }
     public double getStart_address_lng() {
         return start_address_lng;
     }

    public void setEnd_address(String end_address) {
         this.end_address = end_address;
     }
     public String getEnd_address() {
         return end_address;
     }

    public void setEnd_address_lat(double end_address_lat) {
         this.end_address_lat = end_address_lat;
     }
     public double getEnd_address_lat() {
         return end_address_lat;
     }

    public void setEnd_address_lng(double end_address_lng) {
         this.end_address_lng = end_address_lng;
     }
     public double getEnd_address_lng() {
         return end_address_lng;
     }

    public void setPolyline(String polyline) {
         this.polyline = polyline;
     }
     public String getPolyline() {
         return polyline;
     }

    public void setWaypoint_order(List<String> waypoint_order) {
         this.waypoint_order = waypoint_order;
     }
     public List<String> getWaypoint_order() {
         return waypoint_order;
     }

    public void setGateway(String gateway) {
         this.gateway = gateway;
     }
     public String getGateway() {
         return gateway;
     }

    public void setTime(long time) {
         this.time = time;
     }
     public long getTime() {
         return time;
     }

}