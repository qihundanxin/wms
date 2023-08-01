package com.deer.wms.system.manage.model.organization;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 货主、仓库、上架策略
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "organization_tactic")
public class OrganizationTactic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 货主id
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 仓库ID
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 仓库名称
     */
    @Column(name = "ware_name")
    private String wareName;

    /**
     * 上架编码
     */
    @Column(name = "upper_tactic_code")
    private String upperTacticCode;

    /**
     * 上架策略名称
     */
    @Column(name = "upper_tactic_name")
    private String upperTacticName;
}
