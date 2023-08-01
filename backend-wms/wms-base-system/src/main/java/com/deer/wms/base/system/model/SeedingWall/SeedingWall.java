package com.deer.wms.base.system.model.SeedingWall;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 播种墙
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "seeding_wall")
public class SeedingWall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 编码
     */
    @Column(name = "code")
    private String code;

    /**
     * 行数
     */
    @Column(name = "row_count")
    private Integer rowCount;

    /**
     * 列数
     */
    @Column(name = "column_count")
    private Integer columnCount;

    /**
     * 仓库id
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 仓库名称
     */
    @Column(name = "ware_name")
    private String wareName;

    /**
     * 货主ID
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 波次masterId
     */
    @Column(name = "wave_master_id")
    private Integer waveMasterId;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;
}
