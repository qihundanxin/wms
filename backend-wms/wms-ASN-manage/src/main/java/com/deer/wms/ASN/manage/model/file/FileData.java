package com.deer.wms.ASN.manage.model.file;

import java.util.List;

import lombok.ToString;

@ToString
public class FileData {
    private List<String> billNos;

    private List<String> detailNos;

    public List<String> getBillNos() {
        return billNos;
    }

    public void setBillNos(List<String> billNos) {
        this.billNos = billNos;
    }

    public List<String> getDetailNos() {
        return detailNos;
    }

    public void setDetailNos(List<String> detailNos) {
        this.detailNos = detailNos;
    }
}
