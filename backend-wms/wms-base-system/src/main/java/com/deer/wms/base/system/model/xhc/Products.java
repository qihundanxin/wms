/**
 * Copyright 2021 bejson.com
 */
package com.deer.wms.base.system.model.xhc;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Auto-generated: 2021-08-05 18:31:58
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@ToString
@EqualsAndHashCode
public class Products {

    private int shop_id;
    private long product_id;
    private String name;
    private String price;
    private String category;
    private int status;
    private String status_desc;
    private String short_desc;
    private int position;
    private String image_url;
    private List<String> options;
    private String sku;
    /**
     * WMS是否使用此Product作为SKU信息同步
     */
    private Boolean wmsUsed;

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }
    public int getShop_id() {
        return shop_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }
    public long getProduct_id() {
        return product_id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getPrice() {
        return price;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus_desc(String status_desc) {
        this.status_desc = status_desc;
    }
    public String getStatus_desc() {
        return status_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }
    public String getShort_desc() {
        return short_desc;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    public int getPosition() {
        return position;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
    public String getImage_url() {
        return image_url;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
    public List<String> getOptions() {
        return options;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
    public String getSku() {
        return sku;
    }

    public Boolean getWmsUsed() {
        return wmsUsed;
    }

    public void setWmsUsed(Boolean wmsUsed) {
        this.wmsUsed = wmsUsed;
    }

}
