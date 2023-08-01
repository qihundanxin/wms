package com.deer.wms.base.system.model.Pack;

import lombok.ToString;

@ToString
public class PackDetail2 {
    private Integer packDetailId1;
    private String packDescribe1;
    private Double transRatio1;

    public Integer getPackDetailId1() {
        return packDetailId1;
    }

    public void setPackDetailId1(Integer packDetailId1) {
        this.packDetailId1 = packDetailId1;
    }

    public String getPackDescribe1() {
        return packDescribe1;
    }

    public void setPackDescribe1(String packDescribe1) {
        this.packDescribe1 = packDescribe1;
    }

    public Double getTransRatio1() {
        return transRatio1;
    }

    public void setTransRatio1(Double transRatio1) {
        this.transRatio1 = transRatio1;
    }
}