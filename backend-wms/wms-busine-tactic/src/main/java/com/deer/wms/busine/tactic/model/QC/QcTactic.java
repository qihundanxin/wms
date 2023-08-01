package com.deer.wms.busine.tactic.model.QC;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 质检策略表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "qc_tactic")
public class QcTactic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qc_tactic_id")
    private Integer qcTacticId;

    /**
     * 质检编码
     */
    @Column(name = "qc_tactic_code")
    private String qcTacticCode;

    /**
     * 质检描述
     */
    @Column(name = "qc_tactic_name")
    private String qcTacticName;

    /**
     * 质检类型
     */
    @Column(name = "qc_type")
    private Integer qcType;
}
