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
public class EatIn {

    private List<String> printers;
    private List<String> SpecifyAddresses;
    private List<String> profile;
    private Contacts contacts;
    private CustomHTML customHTML;
    private boolean bookable;
    public void setPrinters(List<String> printers) {
         this.printers = printers;
     }
     public List<String> getPrinters() {
         return printers;
     }

    public void setSpecifyAddresses(List<String> SpecifyAddresses) {
         this.SpecifyAddresses = SpecifyAddresses;
     }
     public List<String> getSpecifyAddresses() {
         return SpecifyAddresses;
     }

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

    public void setCustomHTML(CustomHTML customHTML) {
         this.customHTML = customHTML;
     }
     public CustomHTML getCustomHTML() {
         return customHTML;
     }

    public void setBookable(boolean bookable) {
         this.bookable = bookable;
     }
     public boolean getBookable() {
         return bookable;
     }

}