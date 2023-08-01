package com.deer.wms.inventory.model;

import java.util.List;

import lombok.ToString;

@ToString
public class IvTransOrgInsert {
    private IvTransOrgMaster ivTransOrgMaster;
    private List<IvTransOrgDetail> ivTransOrgDetails;

    public IvTransOrgMaster getIvTransOrgMaster() {
        return ivTransOrgMaster;
    }

    public void setIvTransOrgMaster(IvTransOrgMaster ivTransOrgMaster) {
        this.ivTransOrgMaster = ivTransOrgMaster;
    }

    public List<IvTransOrgDetail> getIvTransOrgDetails() {
        return ivTransOrgDetails;
    }

    public void setIvTransOrgDetails(List<IvTransOrgDetail> ivTransOrgDetails) {
        this.ivTransOrgDetails = ivTransOrgDetails;
    }
}
