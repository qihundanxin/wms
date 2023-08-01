package com.deer.wms.ware.task.model.SO;

import java.util.List;

import lombok.ToString;

@ToString
public class Merge {

    private List<String> billNos;

    public List<String> getBillNos() {
        return billNos;
    }

    public void setBillNos(List<String> billNos) {
        this.billNos = billNos;
    }

}
