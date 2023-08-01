package com.deer.wms.ware.task.model.Send;


import lombok.Data;

@Data
public class SendOrderInfo {
    private String soBillNo;
    private int orderQuantity;
    private int sendQuantity;
    private int deliverIndex;
    private String driverName;
    public String getDriverName() {
        return driverName != null ? driverName : "Unknown";
    }
}
