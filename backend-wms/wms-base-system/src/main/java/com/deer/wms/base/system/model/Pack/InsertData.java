package com.deer.wms.base.system.model.Pack;

import java.util.List;

import lombok.ToString;

@ToString
public class InsertData {

    private PackMaster packMaster;

    private List<PackDetail> packDetails;

    public PackMaster getPackMaster() {
        return packMaster;
    }

    public void setPackMaster(PackMaster packMaster) {
        this.packMaster = packMaster;
    }

    public List<PackDetail> getPackDetails() {
        return packDetails;
    }

    public void setPackDetails(List<PackDetail> packDetails) {
        this.packDetails = packDetails;
    }
}
