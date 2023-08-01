/**
  * Copyright 2021 bejson.com 
  */
package com.deer.wms.ware.task.model.SO.Order;
import java.util.Date;

import lombok.ToString;

/**
 * Auto-generated: 2021-08-22 11:15:55
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@ToString
public class ScheduleLines {

    private int scheduleNo;
    private int scheduleQty;
    private Date deliveryDate;
    private Date scheduleStorageDate;
    public void setScheduleNo(int scheduleNo) {
         this.scheduleNo = scheduleNo;
     }
     public int getScheduleNo() {
         return scheduleNo;
     }

    public void setScheduleQty(int scheduleQty) {
         this.scheduleQty = scheduleQty;
     }
     public int getScheduleQty() {
         return scheduleQty;
     }

    public void setDeliveryDate(Date deliveryDate) {
         this.deliveryDate = deliveryDate;
     }
     public Date getDeliveryDate() {
         return deliveryDate;
     }

    public void setScheduleStorageDate(Date scheduleStorageDate) {
         this.scheduleStorageDate = scheduleStorageDate;
     }
     public Date getScheduleStorageDate() {
         return scheduleStorageDate;
     }

}