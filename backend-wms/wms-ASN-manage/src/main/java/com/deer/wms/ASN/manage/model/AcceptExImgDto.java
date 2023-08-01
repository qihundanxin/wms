package com.deer.wms.ASN.manage.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.ToString;

@ToString
public class AcceptExImgDto {

    private String shipBillCode;
    private MultipartFile model;

    public String getShipBillCode() {
        return shipBillCode;
    }

    public void setShipBillCode(String shipBillCode) {
        this.shipBillCode = shipBillCode;
    }

    public MultipartFile getModel() {
        return model;
    }

    public void setModel(MultipartFile model) {
        this.model = model;
    }

}