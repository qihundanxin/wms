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
public class CustomHTML {

    private String header;
    private String footer;
    public void setHeader(String header) {
         this.header = header;
     }
     public String getHeader() {
         return header;
     }

    public void setFooter(String footer) {
         this.footer = footer;
     }
     public String getFooter() {
         return footer;
     }

}