package com.deer.wms.inventory.service;

/**
 * 库存管理反作用于库存冻结的连接器Service
 * 
 * @author xuesinuo
 *
 */
public interface FreezeConnector {
    /**
     * 转移冻结库存（全部移库时，库存对应冻结单一并转移到新货位）
     * 
     * @param inventoryId    库存ID
     * @param newInventoryId 新库存ID
     * @param cellCode       货位编号
     * @param wareId         仓库ID
     */
    void freezeAllMove(Integer inventoryId, Integer newInventoryId, String cellCode, Integer wareId);
}
