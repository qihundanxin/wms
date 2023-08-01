package com.deer.wms.busine.tactic.model.Batch;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 批次编码来源表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "batch_tactic_detail_detail")
public class BatchTacticDetailDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_tactic_detail_detail_id")
    private Integer batchTacticDetailDetailId;

    /**
     * 批次策略详情id
     */
    @Column(name = "batch_tactic_detail_id")
    private Integer batchTacticDetailId;

    /**
     * 详细代码
     */
    @Column(name = "detail_code")
    private String detailCode;

    /**
     * 选择方式
     */
    @Column(name = "batch_tactic_detail_detail_value")
    private String batchTacticDetailDetailValue;
}
