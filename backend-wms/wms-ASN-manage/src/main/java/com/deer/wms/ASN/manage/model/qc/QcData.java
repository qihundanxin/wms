package com.deer.wms.ASN.manage.model.qc;

import lombok.ToString;

@ToString
public class QcData extends QcRecord {
    private Integer billSource;

    public Integer getBillSource() {
        return billSource;
    }

    public void setBillSource(Integer billSource) {
        this.billSource = billSource;
    }

}
