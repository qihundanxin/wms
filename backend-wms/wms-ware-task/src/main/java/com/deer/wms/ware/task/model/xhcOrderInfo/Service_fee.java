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
public class Service_fee {

    private int enabled;
    private String type;
    private List<String> rules;
    public void setEnabled(int enabled) {
         this.enabled = enabled;
     }
     public int getEnabled() {
         return enabled;
     }

    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setRules(List<String> rules) {
         this.rules = rules;
     }
     public List<String> getRules() {
         return rules;
     }

}