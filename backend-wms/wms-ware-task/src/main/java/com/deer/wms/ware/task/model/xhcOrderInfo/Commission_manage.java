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
public class Commission_manage {

    private Date updated_at;
    private String updated_by;
    public void setUpdated_at(Date updated_at) {
         this.updated_at = updated_at;
     }
     public Date getUpdated_at() {
         return updated_at;
     }

    public void setUpdated_by(String updated_by) {
         this.updated_by = updated_by;
     }
     public String getUpdated_by() {
         return updated_by;
     }

}