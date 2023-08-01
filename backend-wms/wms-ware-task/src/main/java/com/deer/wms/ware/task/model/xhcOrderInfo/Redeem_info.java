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
public class Redeem_info {

    private String qrcode_url;
    private String expire_date;
    public void setQrcode_url(String qrcode_url) {
         this.qrcode_url = qrcode_url;
     }
     public String getQrcode_url() {
         return qrcode_url;
     }

    public void setExpire_date(String expire_date) {
         this.expire_date = expire_date;
     }
     public String getExpire_date() {
         return expire_date;
     }

}