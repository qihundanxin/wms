package com.deer.wms.system.manage.model.carrier;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2020/01/06.
 */
@ToString
public class CarrierDetailCriteria extends QueryCriteria {
    private Integer carrierDetailId;
    private String carrierCode;
    private String carType;
    private String carNo;
    private String driver;
    private String driverPhone;
    private String memo;

    public Integer getCarrierDetailId() {
        return carrierDetailId;
    }

    public void setCarrierDetailId(Integer carrierDetailId) {
        this.carrierDetailId = carrierDetailId;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
