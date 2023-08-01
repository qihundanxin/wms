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
public class Comments {

    private String delivery;
    private String user;
    private List<Extra> extra;
    public void setDelivery(String delivery) {
         this.delivery = delivery;
     }
     public String getDelivery() {
         return delivery;
     }

    public void setUser(String user) {
         this.user = user;
     }
     public String getUser() {
         return user;
     }

    public void setExtra(List<Extra> extra) {
         this.extra = extra;
     }
     public List<Extra> getExtra() {
         return extra;
     }

}