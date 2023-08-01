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
public class Payment_method {

    private boolean cash;
    private boolean online;
    public void setCash(boolean cash) {
         this.cash = cash;
     }
     public boolean getCash() {
         return cash;
     }

    public void setOnline(boolean online) {
         this.online = online;
     }
     public boolean getOnline() {
         return online;
     }

}