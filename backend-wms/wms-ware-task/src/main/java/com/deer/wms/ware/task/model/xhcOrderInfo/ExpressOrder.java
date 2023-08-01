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
public class ExpressOrder {

    private int enabled;
    private int id;
    public void setEnabled(int enabled) {
         this.enabled = enabled;
     }
     public int getEnabled() {
         return enabled;
     }

    public void setId(int id) {
         this.id = id;
     }
     public int getId() {
         return id;
     }

}