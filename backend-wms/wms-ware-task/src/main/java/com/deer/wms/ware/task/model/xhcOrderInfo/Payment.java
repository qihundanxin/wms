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
public class Payment {

    private String type;   /***/
    private Onlinepay onlinepay;
    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setOnlinepay(Onlinepay onlinepay) {
         this.onlinepay = onlinepay;
     }
     public Onlinepay getOnlinepay() {
         return onlinepay;
     }

}