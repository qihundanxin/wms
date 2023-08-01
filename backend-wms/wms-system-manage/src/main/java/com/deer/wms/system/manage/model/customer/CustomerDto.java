package com.deer.wms.system.manage.model.customer;

import lombok.ToString;

@ToString
public class CustomerDto extends Customer {
    private String carrierName;

    private String organizationName;

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

}
