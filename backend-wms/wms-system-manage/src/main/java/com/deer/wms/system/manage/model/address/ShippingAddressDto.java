package com.deer.wms.system.manage.model.address;

import lombok.ToString;

@ToString
public class ShippingAddressDto extends ShippingAddress {

    private String wareName;

    private String organizationName;

    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
