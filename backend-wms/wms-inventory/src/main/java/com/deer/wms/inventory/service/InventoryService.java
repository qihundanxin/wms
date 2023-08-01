package com.deer.wms.inventory.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.deer.wms.base.system.event.UpdateItemInfoEvent;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.Inventory.GroupByWareOrgItem;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.Inventory.InventoryCriteria;
import com.deer.wms.inventory.model.Inventory.InventoryDto;
import com.deer.wms.inventory.model.Inventory.InventoryRecoveryCriteria;
import com.deer.wms.inventory.model.Inventory.InventorySelectDto;
import com.deer.wms.inventory.model.stockTake.StockTakeMaster;
import com.deer.wms.inventory.web.InventoryController;
import com.deer.wms.project.root.core.service.Service;

/**
 * Created by 郭靖勋 on 2019/12/18.
 */
public interface InventoryService extends Service<Inventory, Integer> {

    /**
     * 不允许删除（抹除Service接口的默认实现）<br>
     * 2023-07-29起，不再允许直接删除，需要调用修改，由此方法判断修改后的结果是否符合删除条件
     * 
     * @author xuesinuo
     */
    void deleteById(Integer id);

    /**
     * 不允许删除（抹除Service接口的默认实现）<br>
     * 2023-07-29起，不再允许直接删除，需要调用修改，由此方法判断修改后的结果是否符合删除条件
     * 
     * @author xuesinuo
     */
    void deleteByIds(String ids);

    Inventory move(InventoryController.InventoryMoveRequestBean param, CurrentUser currentUser) throws InvocationTargetException, IllegalAccessException;

    /**
     * 汇总移库
     * 2023/6/30 PDA移库不区分批次号
     */
    Inventory summaryMove(Double quantity, String toCellCode, Integer wareId,String itemCode,String formCellCode, CurrentUser currentUser) throws InvocationTargetException, IllegalAccessException;

    /**
     * 简单的查询库存信息
     */
    List<InventoryDto> findList(InventoryCriteria criteria);

    /**
     * 查询库存信息，但不区分批次号
     */
    List<InventoryDto> findSummaryList(InventoryCriteria criteria);
    /**
     * 简单的查询库存信息，封装为Inventory
     */
    List<Inventory> findInventoryList(InventoryCriteria criteria);

    /**
     * 查询库存信息，并查询关联信息
     */
    List<InventoryDto> findListMoreInfo(InventoryCriteria criteria);

    Inventory getSameInventory(Integer wareId, String cellCode, String boxCode, Integer packDetailId, String itemCode, Integer batchId, Double transRatio);

    Inventory getAccept(Integer inventoryId, Integer wareId);

    Inventory getAsnInventory(Integer wareId, String boxCode, Integer packDetailId, String itemCode, Integer batchId);

    List<InventorySelectDto> findCanBeOutOfStock(InventoryCriteria criteria);

    List<InventoryDto> canOut(InventoryCriteria criteria);

    List<Inventory> getStockInventory(Integer wareId, String cellCode, String boxCode, Integer organizationId, String itemCode);

    List<Inventory> findSame(InventoryCriteria criteria);

    List<Inventory> findByCellCode(InventoryCriteria criteria);

    List<Inventory> findByCellCodeAndState(InventoryCriteria criteria);

    List<Inventory> checkList(InventoryCriteria criteria);

    List<Inventory> findBaoKuan(Integer wareId);

    List<Inventory> findBatchInventory(InventoryCriteria criteria);

    Inventory findBatchId(Integer batchId);

    Inventory findByItemWareOrg(String itemCode, Integer wareId, Integer orgId, String toCellCode);

    List<InventoryDto> getInventoryByCellType(String itemCode, Integer wareId, Integer orgId);

    Double findByItemWareOrgGroupByItem(Integer wareId, Integer orgId, String itemCode);

    List inventorySafetyList(InventoryCriteria criteria);

    List<Inventory> palletsToCombine(InventoryCriteria criteria);

    List<Inventory> inventoriesWithBoxCode(String boxCode);

    List<GroupByWareOrgItem> findGroupByWareOrgItem(InventoryCriteria criteria);

    String findSameItemCell(String itemCode, Integer wareId, Integer orgId);

    void sendOut(String itemCode, Integer wareId);

    List<Inventory> getInventoryByFromInfo(StockTakeMaster stockTakeMaster);

    int update(Inventory inventory);

    void save(Inventory inventory);

    void updateCellStatus(String cellCode, Integer wareId);

    String findPickCellCode(String itemCode, Integer wareId, Integer organizationId);

    List<Inventory> canBh(String itemCode, Integer wareId, Integer organizationId);

    int addAllotQuantity(Integer id, Double quantity);

    void inventoryRecovery(InventoryRecoveryCriteria criteria, CurrentUser currentUser);

    boolean updateInventoryByItemUpdate(UpdateItemInfoEvent event);

    /**
     * 创建或查询一个推荐的拣货位库存
     */
    InventorySelectDto createOrGetRecommendedForPick(Integer orgId, String itemCode, Integer wareId);
}
