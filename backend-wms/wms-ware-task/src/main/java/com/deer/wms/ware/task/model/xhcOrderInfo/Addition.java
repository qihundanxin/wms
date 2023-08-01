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
public class Addition {

    private int shipping_fee;
    private int shipping_draw_fee;
    private boolean subsidy_enabled;
    private int grab_draw_fee;
    public void setShipping_fee(int shipping_fee) {
         this.shipping_fee = shipping_fee;
     }
     public int getShipping_fee() {
         return shipping_fee;
     }

    public void setShipping_draw_fee(int shipping_draw_fee) {
         this.shipping_draw_fee = shipping_draw_fee;
     }
     public int getShipping_draw_fee() {
         return shipping_draw_fee;
     }

    public void setSubsidy_enabled(boolean subsidy_enabled) {
         this.subsidy_enabled = subsidy_enabled;
     }
     public boolean getSubsidy_enabled() {
         return subsidy_enabled;
     }

    public void setGrab_draw_fee(int grab_draw_fee) {
         this.grab_draw_fee = grab_draw_fee;
     }
     public int getGrab_draw_fee() {
         return grab_draw_fee;
     }

}