/**
  * Copyright 2021 bejson.com 
  */
package com.deer.wms.ware.task.model.xhcOrderInfo;
import java.util.List;

import lombok.ToString;

/**
 * Auto-generated: 2021-07-13 1:20:49
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@ToString
public class BookSeat {

    private List<String> profile;
    private Contacts contacts;
    private List<String> printers;
    public void setProfile(List<String> profile) {
         this.profile = profile;
     }
     public List<String> getProfile() {
         return profile;
     }

    public void setContacts(Contacts contacts) {
         this.contacts = contacts;
     }
     public Contacts getContacts() {
         return contacts;
     }

    public void setPrinters(List<String> printers) {
         this.printers = printers;
     }
     public List<String> getPrinters() {
         return printers;
     }

}