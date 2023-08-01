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
public class Contact {

    private String name;
    private String tel;
    private String short_tel;
    private String addr;
    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setTel(String tel) {
         this.tel = tel;
     }
     public String getTel() {
         return tel;
     }

    public void setShort_tel(String short_tel) {
         this.short_tel = short_tel;
     }
     public String getShort_tel() {
         return short_tel;
     }

    public void setAddr(String addr) {
         this.addr = addr;
     }
     public String getAddr() {
         return addr;
     }

}