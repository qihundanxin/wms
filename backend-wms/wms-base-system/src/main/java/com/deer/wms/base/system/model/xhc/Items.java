/**
  * Copyright 2021 bejson.com
  */
package com.deer.wms.base.system.model.xhc;

import lombok.ToString;

/**
 * Auto-generated: 2021-07-14 13:57:11
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@ToString
public class Items {

    private String name;
    private String price;
    private String checked;
    private String max;
    private String status;
    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
