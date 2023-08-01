package com.deer.wms.busine.tactic.model;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * SO合并策略
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "so_merge_tactic")
public class SoMergeTactic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "so_merge_tactic_id")
    private Integer soMergeTacticId;

    /**
     * SO合并策略编码
     */
    @Column(name = "so_merge_tactic_code")
    private String soMergeTacticCode;

    /**
     * SO合并策略名称
     */
    @Column(name = "so_merge_tactic_name")
    private String soMergeTacticName;

    /**
     * 货主ID
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 单据来源  0-不做限制  1-强制  2-提示
     */
    @Column(name = "bill_source")
    private Integer billSource;

    /**
     * 订单类型
     */
    @Column(name = "so_type")
    private Integer soType;

    /**
     * 交货时间
     */
    @Column(name = "delivery_time")
    private Integer deliveryTime;

    /**
     * 预期发货时间FM
     */
    @Column(name = "expect_time_fm")
    private Integer expectTimeFm;

    /**
     * 预期发货时间TO
     */
    @Column(name = "expect_time_to")
    private Integer expectTimeTo;

    /**
     * 自定义1
     */
    @Column(name = "so_udf_hs1")
    private Integer soUdfHs1;

    /**
     * 自定义2
     */
    @Column(name = "so_udf_hs2")
    private Integer soUdfHs2;

    /**
     * 自定义3
     */
    @Column(name = "so_udf_hs3")
    private Integer soUdfHs3;

    /**
     * 备注
     */
    @Column(name = "memo")
    private Integer memo;
}
