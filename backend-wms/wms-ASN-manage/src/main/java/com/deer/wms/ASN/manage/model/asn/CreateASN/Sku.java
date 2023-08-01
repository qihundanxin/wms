package com.deer.wms.ASN.manage.model.asn.CreateASN;

import lombok.ToString;

@ToString
public class Sku {

    private String skuCode;
    private Double quantity;

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

}
