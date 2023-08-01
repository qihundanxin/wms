package com.deer.wms.ASN.manage.model;

import lombok.ToString;

@ToString
public class PrintData {
    private String data;
    private Integer quantity;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
