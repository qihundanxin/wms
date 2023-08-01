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
public class Extra {

    private long time;
    private String desc;
    private int admin_id;
    public void setTime(long time) {
         this.time = time;
     }
     public long getTime() {
         return time;
     }

    public void setDesc(String desc) {
         this.desc = desc;
     }
     public String getDesc() {
         return desc;
     }

    public void setAdmin_id(int admin_id) {
         this.admin_id = admin_id;
     }
     public int getAdmin_id() {
         return admin_id;
     }

}