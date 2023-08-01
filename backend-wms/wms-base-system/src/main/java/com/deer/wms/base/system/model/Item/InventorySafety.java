package com.deer.wms.base.system.model.Item;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 库存安全
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "inventory_safety")
public class InventorySafety {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 上限
     */
    @Column(name = "ceiling")
    private Double ceiling;

    /**
     * 下限
     */
    @Column(name = "floor")
    private Double floor;

    /**
     * 补货范围 - 上限
     */
    @Column(name = "cpfr_ceiling")
    private Double cpfrCeiling;

    /**
     * 补货范围 - 下限
     */
    @Column(name = "cpfr_floor")
    private Double cpfrFloor;

    /**
     * 包装id
     */
    @Column(name = "pack_detail_id")
    private Integer packDetailId;

    /**
     * 包装描述
     */
    @Column(name = "pack_describe")
    private String packDescribe;

    /**
     * 物料编码
     */
    @Column(name = "item_code")
    private String itemCode;

    /**
     * 物料名称
     */
    @Column(name = "item_name")
    private String itemName;

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
     * 货主id
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 货主名称
     */
    @Column(name = "organization_name")
    private String organizationName;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;
}
