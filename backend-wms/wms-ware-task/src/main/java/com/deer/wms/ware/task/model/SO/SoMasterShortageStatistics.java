package com.deer.wms.ware.task.model.SO;

import com.deer.wms.inventory.model.Inventory.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lixuehe
 * @date 2023/1/29
 * 订单商品缺货清单统计
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoMasterShortageStatistics {

    /**
     * SKU
     */
    private String itemCode;
    /**
     * SKU名称
     */
    private String itemName;
    /**
     * 拣货位缺货商品数量
     */
    private Integer itemShortageQuantity;
    /**
     * 拣货位缺货订单数量
     */
    private Integer orderShortageQuantity;

    /**
     * 拣货位
     */
    private List<Inventory> PickCells;

    /**
     * 仓储位
     */
    private String storageCell;
}
