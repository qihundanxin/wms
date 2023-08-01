/**
  * Copyright 2022 bejson.com 
  */
package com.deer.wms.ware.task.model.xhcOrderInfo;

import lombok.ToString;

/**
 * Auto-generated: 2022-03-03 15:27:49
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@ToString
public class Revision {

    private long id;
    private String name;
    private String image;
    private String sku;
    private String short_desc;
    private String desc;
    private String category;
    private int price;
    private long product_id;
    private int type;
    private String sale_start_time;
    private String sale_end_time;
    private String org_price;
    private int is_tax;
    private int is_hide;
    private int pur_price;
    private long created_at;
    private long updated_at;
    private int operator_id;
    private String tax_rate;
    private Product product;
    private Long ProductId;
    
    public void setId(long id) {
         this.id = id;
     }
     public long getId() {
         return id;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setImage(String image) {
         this.image = image;
     }
     public String getImage() {
         return image;
     }

    public void setShort_desc(String short_desc) {
         this.short_desc = short_desc;
     }
     public String getShort_desc() {
         return short_desc;
     }

    public void setDesc(String desc) {
         this.desc = desc;
     }
     public String getDesc() {
         return desc;
     }

    public void setCategory(String category) {
         this.category = category;
     }
     public String getCategory() {
         return category;
     }

    public void setPrice(int price) {
         this.price = price;
     }
     public int getPrice() {
         return price;
     }

    public void setProduct_id(long product_id) {
         this.product_id = product_id;
     }
     public long getProduct_id() {
         return product_id;
     }

    public void setType(int type) {
         this.type = type;
     }
     public int getType() {
         return type;
     }

    public void setSale_start_time(String sale_start_time) {
         this.sale_start_time = sale_start_time;
     }
     public String getSale_start_time() {
         return sale_start_time;
     }

    public void setSale_end_time(String sale_end_time) {
         this.sale_end_time = sale_end_time;
     }
     public String getSale_end_time() {
         return sale_end_time;
     }

    public void setOrg_price(String org_price) {
         this.org_price = org_price;
     }
     public String getOrg_price() {
         return org_price;
     }

    public void setIs_tax(int is_tax) {
         this.is_tax = is_tax;
     }
     public int getIs_tax() {
         return is_tax;
     }

    public void setIs_hide(int is_hide) {
         this.is_hide = is_hide;
     }
     public int getIs_hide() {
         return is_hide;
     }

    public void setPur_price(int pur_price) {
         this.pur_price = pur_price;
     }
     public int getPur_price() {
         return pur_price;
     }

    public void setCreated_at(long created_at) {
         this.created_at = created_at;
     }
     public long getCreated_at() {
         return created_at;
     }

    public void setUpdated_at(long updated_at) {
         this.updated_at = updated_at;
     }
     public long getUpdated_at() {
         return updated_at;
     }

    public void setOperator_id(int operator_id) {
         this.operator_id = operator_id;
     }
     public int getOperator_id() {
         return operator_id;
     }

    public void setTax_rate(String tax_rate) {
         this.tax_rate = tax_rate;
     }
     public String getTax_rate() {
         return tax_rate;
     }

    public void setProduct(Product product) {
         this.product = product;
     }
     public Product getProduct() {
         return product;
     }
     
     public void setSku(String skustr) {
 		this.sku = skustr;
 	}
	public String getSku() {
		return this.sku;
	}
	
	 public void setProductId(Long ProductIdstr) {
 		this.ProductId = ProductIdstr;
 	}
	public Long getProductId() {
		return this.ProductId;
	}
}