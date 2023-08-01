package com.deer.wms.ware.task.model.Return;

import java.util.List;

import lombok.ToString;

@ToString
public class ReturnDetailData {
    private List<ReturnDetail> returnDetails;

    public List<ReturnDetail> getReturnDetails() {
        return returnDetails;
    }

    public void setReturnDetails(List<ReturnDetail> returnDetails) {
        this.returnDetails = returnDetails;
    }
}
