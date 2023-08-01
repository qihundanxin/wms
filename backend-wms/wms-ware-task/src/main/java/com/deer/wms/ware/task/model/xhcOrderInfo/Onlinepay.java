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
public class Onlinepay {

    private String stripe_key;
    public void setStripe_key(String stripe_key) {
         this.stripe_key = stripe_key;
     }
     public String getStripe_key() {
         return stripe_key;
     }

}