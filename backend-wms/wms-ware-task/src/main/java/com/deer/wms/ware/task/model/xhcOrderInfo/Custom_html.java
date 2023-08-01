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
public class Custom_html {

    private String notifyBar;
    private String beforeNav;
    private String afterCart;
    public void setNotifyBar(String notifyBar) {
         this.notifyBar = notifyBar;
     }
     public String getNotifyBar() {
         return notifyBar;
     }

    public void setBeforeNav(String beforeNav) {
         this.beforeNav = beforeNav;
     }
     public String getBeforeNav() {
         return beforeNav;
     }

    public void setAfterCart(String afterCart) {
         this.afterCart = afterCart;
     }
     public String getAfterCart() {
         return afterCart;
     }

}