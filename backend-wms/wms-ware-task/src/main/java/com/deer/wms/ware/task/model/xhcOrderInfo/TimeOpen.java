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
public class TimeOpen {

    private int processing;
    private int message;
    private int will_open_at;
    public void setProcessing(int processing) {
         this.processing = processing;
     }
     public int getProcessing() {
         return processing;
     }

    public void setMessage(int message) {
         this.message = message;
     }
     public int getMessage() {
         return message;
     }

    public void setWill_open_at(int will_open_at) {
         this.will_open_at = will_open_at;
     }
     public int getWill_open_at() {
         return will_open_at;
     }

}