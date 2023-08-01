package com.deer.wms.busine.tactic.model.QC;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 质检策略详情表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "qc_tactic_detail")
public class QcTacticDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qc_tactic_detail_id")
    private Integer qcTacticDetailId;

    /**
     * 质检编码
     */
    @Column(name = "qc_tactic_code")
    private String qcTacticCode;

    /**
     * ASN类型   -1所有
     */
    @Column(name = "asn_type")
    private Integer asnType;

    /**
     * 批量起始数量
     */
    @Column(name = "start_quantity")
    private Double startQuantity;

    /**
     * 批量结束数量
     */
    @Column(name = "end_quantity")
    private Double endQuantity;

    /**
     * 样本数量
     */
    @Column(name = "sample_quantity")
    private Double sampleQuantity;

    /**
     * 检验百分比
     */
    @Column(name = "percentage")
    private Double percentage;

    /**
     * 最小质检数量
     */
    @Column(name = "min_quantity")
    private Double minQuantity;

    /**
     * 最小合格率
     */
    @Column(name = "min_pass_rate")
    private Double minPassRate;
}
