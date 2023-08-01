package com.deer.wms.busine.tactic.model;

import lombok.ToString;

@ToString
public class WaveTacticDianshangDto extends WaveTacticDianshang {
    private String wareName;
    private String customerName;
    private String carrierName;
    private String soTyName;

    public String getSoTyName() {
        return soTyName;
    }

    public void setSoTyName(String soTyName) {
        this.soTyName = soTyName;
    }

    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }
}