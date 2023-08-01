package com.deer.wms.ASN.manage.model.qc;

import java.util.List;

import lombok.ToString;

@ToString
public class QcDatas {
    private List<QcData> qcData;

    public List<QcData> getQcData() {
        return qcData;
    }

    public void setQcData(List<QcData> qcData) {
        this.qcData = qcData;
    }
}
