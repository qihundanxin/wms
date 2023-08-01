package com.deer.wms.inventory.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 货权转移详情
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "iv_trans_org_detail")
public class IvTransOrgDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * master id
     */
    @Column(name = "master_id")
    private Integer masterId;

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
     * 库存id
     */
    @Column(name = "inventory_id")
    private Integer inventoryId;

    /**
     * 转移数量
     */
    @Column(name = "quantity")
    private Double quantity;
}
