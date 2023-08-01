package com.deer.wms.inventory.model.Inventory;

import java.util.Date;
import java.util.List;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 郭靖勋 on 2019/12/18.
 */
@ToString
@Getter
@Setter
public class InventoryCriteria extends QueryCriteria {

    private Double maxQuantity;
    private Double maxWeight;
    private Double maxSize;

    private String cellCode;
    private String boxCode;
    private Integer packDetailId;
    private Double transRatio;
    private String itemCode;
    private String waveCode;
    private String billNo;
    private String itemName;
    private Integer batchId;
    private Integer wareId;

    private Integer inventoryId;
    private Double allotQuantity;
    private Double freezeQuantity;

    private Integer itemTypeId;
    private String itemTypeName;
    private Integer cellUseType;

    private Integer param;

    private String lpn;
    private String supplierBatch;

    private List<Integer> states;
    private Integer qcState;
    private Integer itemOrgId;

    private Integer hasInventoryLimit;
    private Integer inventorySafetyState; // 1-超储 -1-短缺

    private String jointSql;
    private String upc;
    /**
     * 回收时间-开始时间
     */
    private Date startRecoveryTime;
    /**
     * 回收时间-结束时间
     */
    private Date endRecoveryTime;

    /**
     * 状态 -2 放入回收站 其他均为正常
     */
    private Integer state;

    /**
     * 移入货位时间-开始时间
     */
    private Date startModifyTime;
    /**
     * 移入货位时间-结束时间
     */
    private Date endModifyTime;

    /**
     * 精准查询货位
     */
    private String eqCellCode;

    /**
     * 精准查询批次
     */
    private String eqBatchName;

    /**
     * 排序字段
     */
    private String sort;

    /**
     * 货位类型不等于
     */
    private Integer cellUseTypeNotEq;

    /**
     * id范围最小值（含）
     */
    private Integer idMin;
    /**
     * id范围最大值（不含）
     */
    private Integer idMax;

    public InventoryCriteria() {
    }

    public InventoryCriteria(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }
    
    public InventoryCriteria(String itemCode, Integer cellUseType) {
        this.itemCode = itemCode;
        this.cellUseType = cellUseType;
    }

    public InventoryCriteria(String itemCode, Integer cellUseType, Integer orderBy) {
        this.itemCode = itemCode;
        this.cellUseType = cellUseType;
        this.param = orderBy;
    }
}
