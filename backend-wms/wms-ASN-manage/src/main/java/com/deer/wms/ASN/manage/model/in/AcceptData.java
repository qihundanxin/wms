package com.deer.wms.ASN.manage.model.in;

import java.util.List;

import lombok.ToString;

@ToString
public class AcceptData {
    private List<AcceptInsert> acceptInserts;

    public List<AcceptInsert> getAcceptInserts() {
        return acceptInserts;
    }

    public void setAcceptInserts(List<AcceptInsert> acceptInserts) {
        this.acceptInserts = acceptInserts;
    }

}
