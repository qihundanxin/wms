package com.deer.wms.busine.tactic.model.Upper;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 上架策略表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "upper_tactic")
public class UpperTactic {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upper_tactic_id")
    private Integer upperTacticId;

    /**
     * 上架规则编码
     */
    @Column(name = "upper_tactic_code")
    private String upperTacticCode;

    /**
     * 上架规则描述
     */
    @Column(name = "upper_tactic_name")
    private String upperTacticName;

    /**
     * 仓库
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 货主   如果不设置，则适用于所有货主
     */
    @Column(name = "organization_id")
    private Integer organizationId;
}
