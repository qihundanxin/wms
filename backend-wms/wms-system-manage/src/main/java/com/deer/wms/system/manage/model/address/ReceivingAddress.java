package com.deer.wms.system.manage.model.address;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 收件信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "receiving_address")
public class ReceivingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receiving_address_id")
    private Integer receivingAddressId;

    /**
     * 客户ID
     */
    @Column(name = "customer_id")
    private Integer customerId;

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
     * 所在国家
     */
    @Column(name = "country")
    private String country;

    /**
     * 所在省份
     */
    @Column(name = "province")
    private String province;

    /**
     * 所在城市
     */
    @Column(name = "city")
    private String city;

    /**
     * 所在区
     */
    @Column(name = "area")
    private String area;

    /**
     * 详细地址
     */
    @Column(name = "detail_address")
    private String detailAddress;

    /**
     * 0-普通地址  1-默认地址
     */
    @Column(name = "state")
    private Integer state;
}
