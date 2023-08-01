package com.deer.wms.system.manage.model.address;

import lombok.ToString;

@ToString
public class ReceivingAddressDto extends ReceivingAddress {

    private String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
