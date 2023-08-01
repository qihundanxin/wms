package com.deer.wms.ware.task.model.SO;

import lombok.ToString;

@ToString
public class SoCancelRequest {

    private String billNo;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
}
