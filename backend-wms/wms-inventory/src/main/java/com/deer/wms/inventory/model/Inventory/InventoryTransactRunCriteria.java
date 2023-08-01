package com.deer.wms.inventory.model.Inventory;

import lombok.Data;

/**
 * @author lixuehe
 * @date 2023/5/16
 * 生成库存事务
 */
@Data
public class InventoryTransactRunCriteria extends  InventoryTransact {

    /**
     * 库存来源
     */
    private Inventory fromInventory;

    /**
     * 库存
     */
    private Inventory toInventory;




}
