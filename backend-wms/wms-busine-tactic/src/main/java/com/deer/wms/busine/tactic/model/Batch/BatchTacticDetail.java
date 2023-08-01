package com.deer.wms.busine.tactic.model.Batch;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 批次策略详情表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "batch_tactic_detail")
public class BatchTacticDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_tactic_detail_id")
    private Integer batchTacticDetailId;

    /**
     * 批次策略代码
     */
    @Column(name = "batch_tactic_code")
    private String batchTacticCode;

    /**
     * 详细代码
     */
    @Column(name = "detail_code")
    private String detailCode;

    /**
     * 批次名称
     */
    @Column(name = "detail_name")
    private String detailName;

    /**
     * PDA批次名称
     */
    @Column(name = "detail_rf_name")
    private String detailRfName;

    /**
     * 是否显示 0-不显示  1-显示
     */
    @Column(name = "is_show")
    private Integer isShow;

    /**
     * 0-不启用  1-启用
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 0-可输入  1-必须输入
     */
    @Column(name = "is_required")
    private Integer isRequired;

    /**
     * 格式 0-字符 1-数字 2-日期 3-日期时间  4-选择
     */
    @Column(name = "format")
    private Integer format;
}
