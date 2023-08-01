package com.deer.wms.ware.task.model.Load;

import java.util.List;

import lombok.ToString;

@ToString
public class LoadDetailInsertData {
    private Integer loadMasterId;
    private List<LoadDetail> loadDetails;

    public Integer getLoadMasterId() {
        return loadMasterId;
    }

    public void setLoadMasterId(Integer loadMasterId) {
        this.loadMasterId = loadMasterId;
    }

    public List<LoadDetail> getLoadDetails() {
        return loadDetails;
    }

    public void setLoadDetails(List<LoadDetail> loadDetails) {
        this.loadDetails = loadDetails;
    }
}
