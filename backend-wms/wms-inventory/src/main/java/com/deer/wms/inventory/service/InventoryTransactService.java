package com.deer.wms.inventory.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.Inventory.InventoryTransact;
import com.deer.wms.inventory.model.Inventory.InventoryTransactCriteria;
import com.deer.wms.inventory.model.Inventory.InventoryTransactDto;
import com.deer.wms.inventory.model.Inventory.*;
import com.deer.wms.inventory.model.Inventory.InventoryTransactRunCriteria;
import com.deer.wms.project.root.core.service.Service;

/**
 * Created by 郭靖勋 on 2019/12/18.
 */
public interface InventoryTransactService extends Service<InventoryTransact, Integer> {

    List<InventoryTransactDto> findList(InventoryTransactCriteria criteria);

    int run(InventoryTransact inventoryTransact);

    Map run(InventoryTransactRunCriteria criteria);

    // FIXME 本方法被 run(InventoryTransactRunCriteria criteria) 替代
    Map run(Inventory fromInventory, Inventory toInventory, Integer transactType, String detailNo, Integer createUserId, String createUserName);

    /**
     * 尝试再仓库中查询出一个放着此SKU或最近放过此SKU的拣货位
     *
     * @param itemCode SKU
     * @param wareId   仓库
     * @return 货位编号
     */
    @Nullable
    String findHistoryPickCellBySku(String itemCode, Integer wareId);
}
