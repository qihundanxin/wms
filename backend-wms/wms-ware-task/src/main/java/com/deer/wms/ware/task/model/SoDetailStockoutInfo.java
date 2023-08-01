package com.deer.wms.ware.task.model;

import java.util.List;

import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.ware.task.model.SO.SoDetail;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SO-详情 + 未分配拣货的库存信息
 * 
 * @author xuesinuo
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SoDetailStockoutInfo extends SoDetail {
    /**
     * SKU图片
     */
    private String itemInfoImg;
    /**
     * SKU在拣货位上的库存
     */
    private List<Inventory> type1List;
    /**
     * SKU在仓储位上的库存
     */
    private List<Inventory> type2List;
}
