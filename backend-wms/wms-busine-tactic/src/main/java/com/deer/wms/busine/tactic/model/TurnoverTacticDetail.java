package com.deer.wms.busine.tactic.model;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 库存周转规则详情表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "turnover_tactic_detail")
public class TurnoverTacticDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "turnover_tactic_detail_id")
    private Integer turnoverTacticDetailId;

    /**
     * 周转规则编码
     */
    @Column(name = "turnover_code")
    private String turnoverCode;

    /**
     * 批次规则属性
     */
    @Column(name = "batch_tactic_detail_code")
    private String batchTacticDetailCode;

    /**
     * 优先级 （1最优先）
     */
    @Column(name = "sort_priority")
    private Integer sortPriority;

    /**
     * 1-升序 2-降序
     */
    @Column(name = "sort")
    private Integer sort;
}
