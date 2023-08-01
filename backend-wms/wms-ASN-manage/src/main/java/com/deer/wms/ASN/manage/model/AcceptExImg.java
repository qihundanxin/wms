package com.deer.wms.ASN.manage.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 异常入库图片
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "accept_ex_img")
public class AcceptExImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accept_ex_img_id")
    private Integer acceptExImgId;

    /**
     * 异常入库id
     */
    @Column(name = "accept_ex_id")
    private Integer acceptExId;

    /**
     * 异常入库图片url
     */
    @Column(name = "ex_img_url")
    private String exImgUrl;

    /**
     * 快递单号
     */
    @Column(name = "ship_bill_code")
    private String shipBillCode;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
}
