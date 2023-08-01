package com.deer.wms.inventory.model.Inventory;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询最近一次操作过的拣货位库存事务结果
 * 
 * @author xuesinuo
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ClosePickCellTansactResult extends InventoryTransact {
    /** fromCell的使用类型 */
    private Integer fromCellUseType;
    /** toCell的使用类型 */
    private Integer toCellUseType;
}
