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
public class Suggestion {

    private Contacts contacts;
    private CustomHTML customHTML;
    public void setContacts(Contacts contacts) {
         this.contacts = contacts;
     }
     public Contacts getContacts() {
         return contacts;
     }

    public void setCustomHTML(CustomHTML customHTML) {
         this.customHTML = customHTML;
     }
     public CustomHTML getCustomHTML() {
         return customHTML;
     }

}