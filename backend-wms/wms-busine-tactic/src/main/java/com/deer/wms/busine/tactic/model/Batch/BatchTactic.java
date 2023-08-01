package com.deer.wms.busine.tactic.model.Batch;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 批次策略信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "batch_tactic")
public class BatchTactic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_tactic_id")
    private Integer batchTacticId;

    /**
     * 批次策略代码
     */
    @Column(name = "batch_tactic_code")
    private String batchTacticCode;

    /**
     * 批次策略名称
     */
    @Column(name = "batch_tactic_name")
    private String batchTacticName;

    /**
     * 1-系统内置 0-正常
     */
    @Column(name = "is_sys")
    private Integer isSys;
}
