package com.deer.wms.ware.task.model.Load;

import lombok.ToString;

@ToString
public class LoadBoxData {
    private Integer loadMasterId;
    private String boxNo;

    public Integer getLoadMasterId() {
        return loadMasterId;
    }

    public void setLoadMasterId(Integer loadMasterId) {
        this.loadMasterId = loadMasterId;
    }

    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }
}
