package com.deer.wms.system.manage.model.carrier;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 快递信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "carrier")
public class Carrier {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carrier_id")
    private Integer carrierId;

    /**
     * 快递编码
     */
    @Column(name = "carrier_code")
    private String carrierCode;

    /**
     * 快递名称
     */
    @Column(name = "carrier_name")
    private String carrierName;

    /**
     * 联系人
     */
    @Column(name = "link_man")
    private String linkMan;

    /**
     * 联系电话
     */
    @Column(name = "link_phone")
    private String linkPhone;

    /**
     * 0-国内快递  1-国外快递 2-普通运输公司 3-自提
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;
}
