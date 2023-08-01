package com.deer.wms.base.system.model.ItemCombined;

import lombok.ToString;

@ToString
public class ItemCombinedDto extends ItemCombined {
    private String organizationName;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
