package com.deer.wms.system.manage.model.address;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 寄件信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "shipping_address")
public class ShippingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipping_address_id")
    private Integer shippingAddressId;

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
     * 所在省份
     */
    @Column(name = "province")
    private String province;

    /**
     * 所在市、县
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
     * 货主ID
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 仓库ID
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 0 - 普通地址  1-默认地址
     */
    @Column(name = "state")
    private Integer state;
}
