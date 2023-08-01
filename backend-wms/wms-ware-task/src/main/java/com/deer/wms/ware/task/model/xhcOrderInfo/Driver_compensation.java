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
public class Driver_compensation {

    private int status;
    private int amount;
    private int counts;
    public void setStatus(int status) {
         this.status = status;
     }
     public int getStatus() {
         return status;
     }

    public void setAmount(int amount) {
         this.amount = amount;
     }
     public int getAmount() {
         return amount;
     }

    public void setCounts(int counts) {
         this.counts = counts;
     }
     public int getCounts() {
         return counts;
     }

}