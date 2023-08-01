package com.deer.wms.busine.tactic.model;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 库存周转规则主表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "turnover_tactic")
public class TurnoverTactic {

    /**
     * 库存周转规则主表
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "turnover_master_id")
    private Integer turnoverMasterId;

    /**
     * 库存周转编码
     */
    @Column(name = "turnover_code")
    private String turnoverCode;

    /**
     * 库存周转规则描述
     */
    @Column(name = "turnover_description")
    private String turnoverDescription;

    /**
     * 批次规则编码
     */
    @Column(name = "batch_tactic_code")
    private String batchTacticCode;

    /**
     * 1-系统内置
     */
    @Column(name = "is_sys")
    private Integer isSys;
}
