package com.deer.wms.inventory.service;

/**
 * 库存管理反作用于拣货任务的连接器Service
 * 
 * @author xuesinuo
 *
 */
public interface PickTaskConnector {
    /**
     * 转移拣货任务（全部移库时，库存对应拣货任务一并转移到新货位）
     * 
     * @param inventoryId    库存ID
     * @param newInventoryId 新库存ID
     * @param cellCode       货位编号
     * @param wareId         仓库ID
     */
    void pickTaskAllMove(Integer inventoryId, Integer newInventoryId, String cellCode, Integer wareId);
}
