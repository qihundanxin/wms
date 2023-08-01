/**
  * Copyright 2021 bejson.com 
  */
package com.deer.wms.ware.task.model.xhcOrderInfo;
import java.util.Date;

import lombok.ToString;

/**
 * Auto-generated: 2021-07-13 1:20:49
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@ToString
public class Flash_sale {

    private int enabled;
    private int platform_discount;
    private int merchant_discount;
    private Date start_date;
    private Date end_date;
    private String code;
    public void setEnabled(int enabled) {
         this.enabled = enabled;
     }
     public int getEnabled() {
         return enabled;
     }

    public void setPlatform_discount(int platform_discount) {
         this.platform_discount = platform_discount;
     }
     public int getPlatform_discount() {
         return platform_discount;
     }

    public void setMerchant_discount(int merchant_discount) {
         this.merchant_discount = merchant_discount;
     }
     public int getMerchant_discount() {
         return merchant_discount;
     }

    public void setStart_date(Date start_date) {
         this.start_date = start_date;
     }
     public Date getStart_date() {
         return start_date;
     }

    public void setEnd_date(Date end_date) {
         this.end_date = end_date;
     }
     public Date getEnd_date() {
         return end_date;
     }

    public void setCode(String code) {
         this.code = code;
     }
     public String getCode() {
         return code;
     }

}