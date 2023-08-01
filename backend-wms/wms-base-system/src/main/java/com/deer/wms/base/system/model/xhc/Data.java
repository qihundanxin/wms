/**
 * Copyright 2021 bejson.com
 */
package com.deer.wms.base.system.model.xhc;
import java.util.List;

import lombok.ToString;

/**
 * Auto-generated: 2021-08-05 18:31:58
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@ToString
public class Data {

    private String category;
    private List<Products> products;
    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }
    public List<Products> getProducts() {
        return products;
    }

}
