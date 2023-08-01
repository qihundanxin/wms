package com.deer.wms.ASN.manage.model.asn;

import java.util.List;

import lombok.ToString;

@ToString
public class AsnMasterDto extends AsnMaster {

    private String carrierName;
    private String linkMan;
    private String linkPhone;
    private Integer type;

    private String carType;
    private String carNo;
    private String driver;
    private String driverPhone;
    private String acceptTime;
    private Integer length;
    private Integer width;
    private Integer weight;
    private Integer height;
    private String detailNames;
    private String[] detailNames2;

    public String[] getDetailNames2() {
        return detailNames2;
    }

    public void setDetailNames2(String[] detailNames2) {
        this.detailNames2 = detailNames2;

    }

    public String getDetailNames() {
        return detailNames;
    }

    public void setDetailNames(String detailNames) {
        this.detailNames = detailNames;
        this.detailNames2 = detailNames.split("fengefu");
    }

    private List<AsnDetail> detailList;

    public List<AsnDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<AsnDetail> detailList) {
        this.detailList = detailList;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }
}
