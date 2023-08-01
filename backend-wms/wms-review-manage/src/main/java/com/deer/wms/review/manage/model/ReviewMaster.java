package com.deer.wms.review.manage.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 审核流程主表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "review_master")
public class ReviewMaster {

    /**
     * 审核表ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_master_id")
    private Integer reviewMasterId;

    /**
     * 审核流程名称
     */
    @Column(name = "review_master_name")
    private String reviewMasterName;

    /**
     * 审核流程编号
     */
    @Column(name = "review_master_code")
    private String reviewMasterCode;

    /**
     * 1-采购 2-ASN 3-发运 4-冻结 5-盘点 6-调整
     */
    @Column(name = "bill_type_id")
    private Integer billTypeId;

    @Column(name = "bill_type_name")
    private String billTypeName;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 状态：0-开启 1-停用
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 货主id
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 仓库id
     */
    @Column(name = "ware_id")
    private Integer wareId;
}
