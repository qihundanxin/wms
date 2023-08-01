package com.deer.wms.ASN.manage.model.qc;

import lombok.ToString;

@ToString
public class QcRecordDto extends QcRecord {

    private Double one;
    private Double two;

    public Double getOne() {
        return one;
    }

    public void setOne(Double one) {
        this.one = one;
    }

    public Double getTwo() {
        return two;
    }

    public void setTwo(Double two) {
        this.two = two;
    }
}
