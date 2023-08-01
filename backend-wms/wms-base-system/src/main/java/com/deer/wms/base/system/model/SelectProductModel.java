package com.deer.wms.base.system.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.ToString;

@ToString
public class SelectProductModel {

    private MultipartFile model;
    private Integer selectType;
    private String keywords;
    private Double m4000;
    private Double click20;
    private Double click10;
    private Double md600;
    private Double payRate10;

    public MultipartFile getModel() {
        return model;
    }

    public void setModel(MultipartFile model) {
        this.model = model;
    }

    public Integer getSelectType() {
        return selectType;
    }

    public void setSelectType(Integer selectType) {
        this.selectType = selectType;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Double getM4000() {
        return m4000;
    }

    public void setM4000(Double m4000) {
        this.m4000 = m4000;
    }

    public Double getClick20() {
        return click20;
    }

    public void setClick20(Double click20) {
        this.click20 = click20;
    }

    public Double getClick10() {
        return click10;
    }

    public void setClick10(Double click10) {
        this.click10 = click10;
    }

    public Double getMd600() {
        return md600;
    }

    public void setMd600(Double md600) {
        this.md600 = md600;
    }

    public Double getPayRate10() {
        return payRate10;
    }

    public void setPayRate10(Double payRate10) {
        this.payRate10 = payRate10;
    }
}
