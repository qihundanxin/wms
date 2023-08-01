/**
  * Copyright 2022 bejson.com 
  */
package com.deer.wms.ware.task.model.xhcOrderInfo;
import java.util.Date;
import java.util.List;

import lombok.ToString;

/**
 * Auto-generated: 2022-03-03 15:27:49
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@ToString
public class Shop {

    private int bid;
    private int segment_id;
    private int mid;
    private String name;
    private String pinyin;
    private String desc;
    private String thumbnail;
    private String preview_img;
    private String images;
    private String addr;
    private String tel;
    private String keywords;
    private String city;
    private double latitude;
    private double longitude;
    private String style_id;
    private int is_choice;
    private String deleted_at;
    private String email;
    private String legal_name;
    private String horn_message;
    private int platform_rate;
    private int stage_type;
    private String lang_type;
    private List<String> support_languages;
    private int merchant_share_discount;
    private int commission_free_tax;
    private int driver_collection_enabled;
    private String shop_type;
    private String short_name;
    private String delivery_setting_ids;
    private String owner_contact;
    private String owner_tel;
    private String shop_rating;
    private int shop_shipping_type;
    private int setting_id;
    private int suported_lang_menu;
    private Date created_at;
    private int driver_setting_id;
    private Contacts contacts;
    private Custom_html custom_html;
    private List<String> available_hours;
    private List<String> dine_addrs;
    private boolean takeout_enable;
    private boolean dine_enable;
    private int min_amount;
    private int shipping_fee;
    private double tax_rate;
    private int delivery_time;
    private int is_credit_shop;
    private String thumb;
    private String thumb_small;
    private String preview;
    private int customer_pickup;
    private int flash_sale_enabled;
    private List<String> flash_sale;
    private int open;
    private List<String> supported_order_types;
    private String pay_foodhwy;
    private int is_taxfree;
    private int credits_shop_id;
    private int area_id;
    private String shipping_guide;
    private int grab_enabled;
    private String time_zone;
    private String today_open_time;
    private int free_driver_shipping_fee;
    private boolean has_promotion;
    private boolean has_new_customer_promotion;
    private int preorder_reminder_ahead;
    private int preorder_enabled;
    private List<String> delivery_setting_id_arr;
    private Service_fee service_fee;
    private List<String> supported_payment_methods;
    private int service_tip;
    private int timely_delivery;
    public void setBid(int bid) {
         this.bid = bid;
     }
     public int getBid() {
         return bid;
     }

    public void setSegment_id(int segment_id) {
         this.segment_id = segment_id;
     }
     public int getSegment_id() {
         return segment_id;
     }

    public void setMid(int mid) {
         this.mid = mid;
     }
     public int getMid() {
         return mid;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setPinyin(String pinyin) {
         this.pinyin = pinyin;
     }
     public String getPinyin() {
         return pinyin;
     }

    public void setDesc(String desc) {
         this.desc = desc;
     }
     public String getDesc() {
         return desc;
     }

    public void setThumbnail(String thumbnail) {
         this.thumbnail = thumbnail;
     }
     public String getThumbnail() {
         return thumbnail;
     }

    public void setPreview_img(String preview_img) {
         this.preview_img = preview_img;
     }
     public String getPreview_img() {
         return preview_img;
     }

    public void setImages(String images) {
         this.images = images;
     }
     public String getImages() {
         return images;
     }

    public void setAddr(String addr) {
         this.addr = addr;
     }
     public String getAddr() {
         return addr;
     }

    public void setTel(String tel) {
         this.tel = tel;
     }
     public String getTel() {
         return tel;
     }

    public void setKeywords(String keywords) {
         this.keywords = keywords;
     }
     public String getKeywords() {
         return keywords;
     }

    public void setCity(String city) {
         this.city = city;
     }
     public String getCity() {
         return city;
     }

    public void setLatitude(double latitude) {
         this.latitude = latitude;
     }
     public double getLatitude() {
         return latitude;
     }

    public void setLongitude(double longitude) {
         this.longitude = longitude;
     }
     public double getLongitude() {
         return longitude;
     }

    public void setStyle_id(String style_id) {
         this.style_id = style_id;
     }
     public String getStyle_id() {
         return style_id;
     }

    public void setIs_choice(int is_choice) {
         this.is_choice = is_choice;
     }
     public int getIs_choice() {
         return is_choice;
     }

    public void setDeleted_at(String deleted_at) {
         this.deleted_at = deleted_at;
     }
     public String getDeleted_at() {
         return deleted_at;
     }

    public void setEmail(String email) {
         this.email = email;
     }
     public String getEmail() {
         return email;
     }

    public void setLegal_name(String legal_name) {
         this.legal_name = legal_name;
     }
     public String getLegal_name() {
         return legal_name;
     }

    public void setHorn_message(String horn_message) {
         this.horn_message = horn_message;
     }
     public String getHorn_message() {
         return horn_message;
     }

    public void setPlatform_rate(int platform_rate) {
         this.platform_rate = platform_rate;
     }
     public int getPlatform_rate() {
         return platform_rate;
     }

    public void setStage_type(int stage_type) {
         this.stage_type = stage_type;
     }
     public int getStage_type() {
         return stage_type;
     }

    public void setLang_type(String lang_type) {
         this.lang_type = lang_type;
     }
     public String getLang_type() {
         return lang_type;
     }

    public void setSupport_languages(List<String> support_languages) {
         this.support_languages = support_languages;
     }
     public List<String> getSupport_languages() {
         return support_languages;
     }

    public void setMerchant_share_discount(int merchant_share_discount) {
         this.merchant_share_discount = merchant_share_discount;
     }
     public int getMerchant_share_discount() {
         return merchant_share_discount;
     }

    public void setCommission_free_tax(int commission_free_tax) {
         this.commission_free_tax = commission_free_tax;
     }
     public int getCommission_free_tax() {
         return commission_free_tax;
     }

    public void setDriver_collection_enabled(int driver_collection_enabled) {
         this.driver_collection_enabled = driver_collection_enabled;
     }
     public int getDriver_collection_enabled() {
         return driver_collection_enabled;
     }

    public void setShop_type(String shop_type) {
         this.shop_type = shop_type;
     }
     public String getShop_type() {
         return shop_type;
     }

    public void setShort_name(String short_name) {
         this.short_name = short_name;
     }
     public String getShort_name() {
         return short_name;
     }

    public void setDelivery_setting_ids(String delivery_setting_ids) {
         this.delivery_setting_ids = delivery_setting_ids;
     }
     public String getDelivery_setting_ids() {
         return delivery_setting_ids;
     }

    public void setOwner_contact(String owner_contact) {
         this.owner_contact = owner_contact;
     }
     public String getOwner_contact() {
         return owner_contact;
     }

    public void setOwner_tel(String owner_tel) {
         this.owner_tel = owner_tel;
     }
     public String getOwner_tel() {
         return owner_tel;
     }

    public void setShop_rating(String shop_rating) {
         this.shop_rating = shop_rating;
     }
     public String getShop_rating() {
         return shop_rating;
     }

    public void setShop_shipping_type(int shop_shipping_type) {
         this.shop_shipping_type = shop_shipping_type;
     }
     public int getShop_shipping_type() {
         return shop_shipping_type;
     }

    public void setSetting_id(int setting_id) {
         this.setting_id = setting_id;
     }
     public int getSetting_id() {
         return setting_id;
     }

    public void setSuported_lang_menu(int suported_lang_menu) {
         this.suported_lang_menu = suported_lang_menu;
     }
     public int getSuported_lang_menu() {
         return suported_lang_menu;
     }

    public void setCreated_at(Date created_at) {
         this.created_at = created_at;
     }
     public Date getCreated_at() {
         return created_at;
     }

    public void setDriver_setting_id(int driver_setting_id) {
         this.driver_setting_id = driver_setting_id;
     }
     public int getDriver_setting_id() {
         return driver_setting_id;
     }

    public void setContacts(Contacts contacts) {
         this.contacts = contacts;
     }
     public Contacts getContacts() {
         return contacts;
     }

    public void setCustom_html(Custom_html custom_html) {
         this.custom_html = custom_html;
     }
     public Custom_html getCustom_html() {
         return custom_html;
     }

    public void setAvailable_hours(List<String> available_hours) {
         this.available_hours = available_hours;
     }
     public List<String> getAvailable_hours() {
         return available_hours;
     }

    public void setDine_addrs(List<String> dine_addrs) {
         this.dine_addrs = dine_addrs;
     }
     public List<String> getDine_addrs() {
         return dine_addrs;
     }

    public void setTakeout_enable(boolean takeout_enable) {
         this.takeout_enable = takeout_enable;
     }
     public boolean getTakeout_enable() {
         return takeout_enable;
     }

    public void setDine_enable(boolean dine_enable) {
         this.dine_enable = dine_enable;
     }
     public boolean getDine_enable() {
         return dine_enable;
     }

    public void setMin_amount(int min_amount) {
         this.min_amount = min_amount;
     }
     public int getMin_amount() {
         return min_amount;
     }

    public void setShipping_fee(int shipping_fee) {
         this.shipping_fee = shipping_fee;
     }
     public int getShipping_fee() {
         return shipping_fee;
     }

    public void setTax_rate(double tax_rate) {
         this.tax_rate = tax_rate;
     }
     public double getTax_rate() {
         return tax_rate;
     }

    public void setDelivery_time(int delivery_time) {
         this.delivery_time = delivery_time;
     }
     public int getDelivery_time() {
         return delivery_time;
     }

    public void setIs_credit_shop(int is_credit_shop) {
         this.is_credit_shop = is_credit_shop;
     }
     public int getIs_credit_shop() {
         return is_credit_shop;
     }

    public void setThumb(String thumb) {
         this.thumb = thumb;
     }
     public String getThumb() {
         return thumb;
     }

    public void setThumb_small(String thumb_small) {
         this.thumb_small = thumb_small;
     }
     public String getThumb_small() {
         return thumb_small;
     }

    public void setPreview(String preview) {
         this.preview = preview;
     }
     public String getPreview() {
         return preview;
     }

    public void setCustomer_pickup(int customer_pickup) {
         this.customer_pickup = customer_pickup;
     }
     public int getCustomer_pickup() {
         return customer_pickup;
     }

    public void setFlash_sale_enabled(int flash_sale_enabled) {
         this.flash_sale_enabled = flash_sale_enabled;
     }
     public int getFlash_sale_enabled() {
         return flash_sale_enabled;
     }

    public void setFlash_sale(List<String> flash_sale) {
         this.flash_sale = flash_sale;
     }
     public List<String> getFlash_sale() {
         return flash_sale;
     }

    public void setOpen(int open) {
         this.open = open;
     }
     public int getOpen() {
         return open;
     }

    public void setSupported_order_types(List<String> supported_order_types) {
         this.supported_order_types = supported_order_types;
     }
     public List<String> getSupported_order_types() {
         return supported_order_types;
     }

    public void setPay_foodhwy(String pay_foodhwy) {
         this.pay_foodhwy = pay_foodhwy;
     }
     public String getPay_foodhwy() {
         return pay_foodhwy;
     }

    public void setIs_taxfree(int is_taxfree) {
         this.is_taxfree = is_taxfree;
     }
     public int getIs_taxfree() {
         return is_taxfree;
     }

    public void setCredits_shop_id(int credits_shop_id) {
         this.credits_shop_id = credits_shop_id;
     }
     public int getCredits_shop_id() {
         return credits_shop_id;
     }

    public void setArea_id(int area_id) {
         this.area_id = area_id;
     }
     public int getArea_id() {
         return area_id;
     }

    public void setShipping_guide(String shipping_guide) {
         this.shipping_guide = shipping_guide;
     }
     public String getShipping_guide() {
         return shipping_guide;
     }

    public void setGrab_enabled(int grab_enabled) {
         this.grab_enabled = grab_enabled;
     }
     public int getGrab_enabled() {
         return grab_enabled;
     }

    public void setTime_zone(String time_zone) {
         this.time_zone = time_zone;
     }
     public String getTime_zone() {
         return time_zone;
     }

    public void setToday_open_time(String today_open_time) {
         this.today_open_time = today_open_time;
     }
     public String getToday_open_time() {
         return today_open_time;
     }

    public void setFree_driver_shipping_fee(int free_driver_shipping_fee) {
         this.free_driver_shipping_fee = free_driver_shipping_fee;
     }
     public int getFree_driver_shipping_fee() {
         return free_driver_shipping_fee;
     }

    public void setHas_promotion(boolean has_promotion) {
         this.has_promotion = has_promotion;
     }
     public boolean getHas_promotion() {
         return has_promotion;
     }

    public void setHas_new_customer_promotion(boolean has_new_customer_promotion) {
         this.has_new_customer_promotion = has_new_customer_promotion;
     }
     public boolean getHas_new_customer_promotion() {
         return has_new_customer_promotion;
     }

    public void setPreorder_reminder_ahead(int preorder_reminder_ahead) {
         this.preorder_reminder_ahead = preorder_reminder_ahead;
     }
     public int getPreorder_reminder_ahead() {
         return preorder_reminder_ahead;
     }

    public void setPreorder_enabled(int preorder_enabled) {
         this.preorder_enabled = preorder_enabled;
     }
     public int getPreorder_enabled() {
         return preorder_enabled;
     }

    public void setDelivery_setting_id_arr(List<String> delivery_setting_id_arr) {
         this.delivery_setting_id_arr = delivery_setting_id_arr;
     }
     public List<String> getDelivery_setting_id_arr() {
         return delivery_setting_id_arr;
     }

    public void setService_fee(Service_fee service_fee) {
         this.service_fee = service_fee;
     }
     public Service_fee getService_fee() {
         return service_fee;
     }

    public void setSupported_payment_methods(List<String> supported_payment_methods) {
         this.supported_payment_methods = supported_payment_methods;
     }
     public List<String> getSupported_payment_methods() {
         return supported_payment_methods;
     }

    public void setService_tip(int service_tip) {
         this.service_tip = service_tip;
     }
     public int getService_tip() {
         return service_tip;
     }

    public void setTimely_delivery(int timely_delivery) {
         this.timely_delivery = timely_delivery;
     }
     public int getTimely_delivery() {
         return timely_delivery;
     }

}