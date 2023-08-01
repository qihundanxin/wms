package com.deer.wms.system.manage.model.carrier;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 快递详情表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "carrier_detail")
public class CarrierDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carrier_detail_id")
    private Integer carrierDetailId;

    /**
     * 承运方编码
     */
    @Column(name = "carrier_code")
    private String carrierCode;

    /**
     * 车型
     */
    @Column(name = "car_type")
    private String carType;

    /**
     * 车号
     */
    @Column(name = "car_no")
    private String carNo;

    /**
     * 司机
     */
    @Column(name = "driver")
    private String driver;

    /**
     * 司机电话
     */
    @Column(name = "driver_phone")
    private String driverPhone;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;
}
