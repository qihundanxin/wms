package com.deer.wms.system.manage.model.customer;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 客户信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    /**
     * 客户编码
     */
    @Column(name = "customer_code")
    private String customerCode;

    /**
     * 客户名称
     */
    @Column(name = "customer_name")
    private String customerName;

    /**
     * 地址
     */
    @Column(name = "address")
    private String address;

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
     * 客户默认的承运人
     */
    @Column(name = "carrier_id")
    private Integer carrierId;

    /**
     * 货主ID
     */
    @Column(name = "organization_id")
    private Integer organizationId;
}
