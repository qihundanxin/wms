package com.deer.wms.review.manage.model;

import lombok.ToString;

/**
 * Created by guo on 2018/10/24.
 */
@ToString
public class ReviewMasterDto extends ReviewMaster {

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
