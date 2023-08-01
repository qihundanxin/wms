package com.deer.wms.inventory.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deer.wms.inventory.model.Inventory.GroupByWareOrgItem;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.Inventory.InventoryCriteria;
import com.deer.wms.inventory.model.Inventory.InventoryDto;
import com.deer.wms.inventory.model.Inventory.InventorySelectDto;
import com.deer.wms.inventory.model.stockTake.StockTakeMaster;
import com.deer.wms.project.root.core.mapper.Mapper;

// TODO 进一步对关联查询进行优化
public interface InventoryMapper extends Mapper<Inventory> {

    List<InventoryDto> findList(InventoryCriteria criteria);

    /**
     * 查询库存信息，但不区分批次号
     * @return
     */
    List<InventoryDto> findSummaryList(InventoryCriteria criteria);

    Inventory findSameInventory(InventoryCriteria criteria);

    Inventory findSummarySameInventory(InventoryCriteria criteria);

    Inventory findByItemWareOrg(@Param("itemCode") String itemCode, @Param("wareId") Integer wareId, @Param("orgId") Integer orgId, @Param("cellCode") String cellCode);

    Inventory findBatchId(@Param("batchId") Integer batchId);

    List<Inventory> getStockInventory(InventoryCriteria criteria);

    List<InventorySelectDto> findCanBeOutOfStock(InventoryCriteria criteria);

    List<InventoryDto> canOut(InventoryCriteria criteria);

    List<Inventory> findSame(InventoryCriteria criteria);

    List<Inventory> findByCellCode(InventoryCriteria criteria);

    List<Inventory> findBatchInventory(InventoryCriteria criteria);

    List<Inventory> checkList(InventoryCriteria criteria);

    List inventorySafetyList(InventoryCriteria criteria);

    Double findByItemWareOrgGroupByItem(@Param("wareId") Integer wareId, @Param("orgId") Integer orgId, @Param("itemCode") String itemCode);

    List<Inventory> palletsToCombine(InventoryCriteria criteria);

    List<Inventory> inventoriesWithBoxCode(@Param("boxCode") String boxCode);

    List<GroupByWareOrgItem> findGroupByWareOrgItem(InventoryCriteria criteria);

    String findSameItemCell(@Param("itemCode") String itemCode, @Param("wareId") Integer wareId, @Param("orgId") Integer orgId);

    void sendOut(@Param("billNo") String billNo, @Param("wareId") Integer wareId);

    List<Inventory> findBaoKuan(@Param("wareId") Integer wareId);

    List<Inventory> getInventoryByFromInfo(StockTakeMaster stockTakeMaster);

    Inventory getAccept(@Param("inventoryId") Integer inventoryId, @Param("wareId") Integer wareId);

    List<InventoryDto> getInventoryByCellType(@Param("itemCode") String itemCode, @Param("wareId") Integer wareId, @Param("orgId") Integer orgId);
    List<Inventory> canBh(@Param("itemCode") String itemCode, @Param("wareId") Integer wareId, @Param("organizationId") Integer organizationId);
    String findPickCellCode(@Param("itemCode") String itemCode, @Param("wareId") Integer wareId, @Param("organizationId") Integer organizationId);

    int update(Inventory inventory);

    // FIXME 2023-07-28 允许负库存
    int addAllotQuantity(@Param("id") Integer id,@Param("quantity") Double quantity);

    int deleteByIdVersion(@Param("id") Integer id, @Param("version") Integer version);

    int cellCount(@Param("cellCode") String cellCode, @Param("wareId") Integer wareId);

    /**
     * 根据最小ID查询
     * 
     * @param minId 最小ID（含）
     * @param limit 限制条数
     * @return 查询结果
     */
    List<Inventory> findListByMinId(@Param("minId") int minId, @Param("limit") int limit);

    //查询要修改的库存(from)
    List<Inventory> findInvToBeModified(Integer wareId, String formCellCode, String itemCode);

    /**
     * 查询推荐的拣货位库存信息
     * 
     * @param orgId    货主
     * @param itemCode SKU
     * @param wareId   仓库
     * @param cellCode 货位
     */
    InventorySelectDto findRecommendedForPick(@Param("orgId") Integer orgId, @Param("itemCode") String itemCode, @Param("wareId") Integer wareId, @Param("cellCode") String cellCode);
}
