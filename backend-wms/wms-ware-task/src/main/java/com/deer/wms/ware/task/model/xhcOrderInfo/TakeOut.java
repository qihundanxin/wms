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
public class TakeOut {

    private boolean bookable;
    private boolean needVerify;
    private List<String> printers;
    private List<String> profile;
    private Contacts contacts;
    private CustomHTML customHTML;
    private String available_hours;
    private List<String> available_hours_org;
    private boolean isAutoOpenTomorrow;
    public void setBookable(boolean bookable) {
         this.bookable = bookable;
     }
     public boolean getBookable() {
         return bookable;
     }

    public void setNeedVerify(boolean needVerify) {
         this.needVerify = needVerify;
     }
     public boolean getNeedVerify() {
         return needVerify;
     }

    public void setPrinters(List<String> printers) {
         this.printers = printers;
     }
     public List<String> getPrinters() {
         return printers;
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

    public void setAvailable_hours(String available_hours) {
         this.available_hours = available_hours;
     }
     public String getAvailable_hours() {
         return available_hours;
     }

    public void setAvailable_hours_org(List<String> available_hours_org) {
         this.available_hours_org = available_hours_org;
     }
     public List<String> getAvailable_hours_org() {
         return available_hours_org;
     }

    public void setIsAutoOpenTomorrow(boolean isAutoOpenTomorrow) {
         this.isAutoOpenTomorrow = isAutoOpenTomorrow;
     }
     public boolean getIsAutoOpenTomorrow() {
         return isAutoOpenTomorrow;
     }

}