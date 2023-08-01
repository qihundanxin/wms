package com.deer.wms.inventory.service.impl;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.base.system.event.UpdateCellInfoEvent;
import com.deer.wms.base.system.event.UpdateItemInfoEvent;
import com.deer.wms.base.system.model.ItemUpc;
import com.deer.wms.base.system.model.Cell.CellInfo;
import com.deer.wms.base.system.model.Item.ItemInfo;
import com.deer.wms.base.system.model.Item.ItemType;
import com.deer.wms.base.system.model.Pack.PackMasterDto;
import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.base.system.service.ItemTypeService;
import com.deer.wms.base.system.service.ItemUpcService;
import com.deer.wms.base.system.service.PackMasterService;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.constant.InventoryConstant;
import com.deer.wms.inventory.dao.InventoryMapper;
import com.deer.wms.inventory.model.Inventory.GroupByWareOrgItem;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.Inventory.InventoryCriteria;
import com.deer.wms.inventory.model.Inventory.InventoryDto;
import com.deer.wms.inventory.model.Inventory.InventoryRecoveryCriteria;
import com.deer.wms.inventory.model.Inventory.InventorySelectDto;
import com.deer.wms.inventory.model.Inventory.InventoryTransactRunCriteria;
import com.deer.wms.inventory.model.Inventory.ItemBatch;
import com.deer.wms.inventory.model.stockTake.StockTakeMaster;
import com.deer.wms.inventory.service.FreezeConnector;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.inventory.service.InventoryTransactService;
import com.deer.wms.inventory.service.ItemBatchService;
import com.deer.wms.inventory.service.PickTaskConnector;
import com.deer.wms.inventory.web.InventoryController;
import com.deer.wms.project.root.constant.LockName;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.sync.annotation.Synchronized;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.project.root.util.SyncTool;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by 郭靖勋 on 2019/12/18.
 */
@Service
@Transactional
@Slf4j
public class InventoryServiceImpl extends AbstractService<Inventory, Integer> implements InventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private CellInfoService cellInfoService;
    @Autowired
    private ItemInfoService itemInfoService;
    @Autowired
    private ItemUpcService itemUpcService;
    @Autowired
    private PackMasterService packMasterService;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private ItemBatchService itemBatchService;

    @Autowired
    @Lazy
    private InventoryTransactService inventoryTransactService;

    @Autowired
    private PickTaskConnector pickTaskConnector;
    @Autowired
    private FreezeConnector freezeConnector;

    @Autowired
    private SyncTool syncTool;

    // TODO 是否支持负库存，全局搜索negativeQty，替换为配置
    private static final boolean negativeQty = true;

    @Override
    public void deleteById(Integer id) {
        throw new ServiceException(CommonCode.SYSTEM_ERROR, "this method is Deprecated");
    }

    @Override
    public void deleteByIds(String ids) {
        throw new ServiceException(CommonCode.SYSTEM_ERROR, "this method is Deprecated");
    }

    @Override
    public Inventory move(InventoryController.InventoryMoveRequestBean param, CurrentUser currentUser) throws InvocationTargetException, IllegalAccessException {
        Integer inventoryId = param.getInventoryId();
        Double quantity = param.getQuantity();
        String cellCode = param.getCellCode();
        Integer wareId = param.getWareId();
        CellInfo cellInfo = cellInfoService.findByCodeAndWareId(cellCode, wareId);
        if (cellInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "货位号无效");
        }
        cellCode = cellInfo.getCellCode();
        if (cellInfo.getCellUseType() != 2 && cellInfo.getCellUseType() != 1 && cellInfo.getCellUseType() != -1) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "货位使用类型必须是仓储位或者拣货位");
        }
        Inventory inventory = this.findById(inventoryId);
        // 移动前后均需判断，是否是垃圾货位
        if (Np.i(inventory).x(x -> x.getCellUseType()).eq(-1)) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "此货位不可进行基础移库移出操作");
        }
        if (inventory == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "库存无效");
        }
        if (Np.i(inventory).x(x -> x.getCellCode()).eq(cellCode)) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "库位未发生改变");
        }
        if (quantity > inventory.getQuantity()) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "移库数量不应超过" + inventory.getQuantity());
        }
        if (quantity < inventory.getQuantity() && quantity > inventory.getQuantity() - inventory.getAllotQuantity() - inventory.getFreezeQuantity()) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "部分移库数量不应超过" + (inventory.getQuantity() - inventory.getAllotQuantity() - inventory.getFreezeQuantity()));
        }
        if (Np.i(inventory.getCellUseType()).eq("3")) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "集货位不可操作基础移库");
        }
        return toMoveInv(inventory, quantity, wareId, cellInfo, currentUser, param.getTransactImgs());
    }

    /**
     * 汇总移库
     * 2023/6/30 PDA移库不区分批次号
     */
    @Override
    public Inventory summaryMove( Double quantity, String toCellCode, Integer wareId, String itemCode, String formCellCode, CurrentUser currentUser) throws InvocationTargetException, IllegalAccessException {
        CellInfo cellInfo = cellInfoService.findByCodeAndWareId(toCellCode, wareId);
        if (cellInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "货位号无效");
        }
        if (cellInfo.getCellUseType() != 2 && cellInfo.getCellUseType() != 1 && cellInfo.getCellUseType() != -1) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "货位使用类型必须是仓储位或者拣货位");
        }
        // 获取待修改的库存
        List<Inventory> invToBeModified = inventoryMapper.findInvToBeModified(wareId, formCellCode, itemCode);
        if (invToBeModified.isEmpty()){
            throw new ServiceException(CommonCode.SERVICE_ERROR,"该货位下没有查到相对应的商品信息");
        }
        Inventory inventory = new Inventory();
        for (Inventory inv : invToBeModified) {
            inventory.setQuantity(Np.i(inventory.getQuantity()).o(0.0) + inv.getQuantity());
            inventory.setAllotQuantity(Np.i(inventory.getAllotQuantity()).o(0.0)+inv.getAllotQuantity());
            inventory.setFreezeQuantity(Np.i(inventory.getFreezeQuantity()).o(0.0)+inv.getFreezeQuantity());
            inventory.setCellCode(inv.getCellCode());
            inventory.setItemCode(inv.getItemCode());
            inventory.setPackDescribe(inv.getPackDescribe());
            inventory.setItemName(inv.getItemName());
            inventory.setWareName(inv.getWareName());
            inventory.setOrganizationName(inv.getOrganizationName());
            inventory.setCellUseType(inv.getCellUseType());
        }
        // 移动前后均需判断，是否是垃圾货位
        if (Np.i(inventory).x(x -> x.getCellUseType()).eq(-1)) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "此货位不可进行基础移库移出操作");
        }
        if (Np.i(inventory).x(x -> x.getCellCode()).eq(toCellCode)) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "库位未发生改变");
        }
        if (quantity > inventory.getQuantity()) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "移库数量不应超过" + inventory.getQuantity());
        }
        if (quantity < inventory.getQuantity() && quantity > inventory.getQuantity() - inventory.getAllotQuantity() - inventory.getFreezeQuantity()) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "部分移库数量不应超过" + (inventory.getQuantity() - inventory.getAllotQuantity() - inventory.getFreezeQuantity()));
        }
        if (Np.i(inventory.getCellUseType()).eq("3")) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "集货位不可操作基础移库");
        }
        inventory.setQuantity(inventory.getQuantity() - quantity);
        if (inventory.getQuantity() < 0.0001) {
            // 移动整个库存（包括已分配和冻结的数量）
            for (Inventory allMoveInv : invToBeModified) {
                this.toMoveInv(allMoveInv, allMoveInv.getQuantity(), wareId, cellInfo, currentUser, null);
                inventory = null;
            }
        } else {
            double quantityToAllocate = quantity;
            // 遍历每个批次的库存进行分配
            for (Inventory inv : invToBeModified) {
                //减去已分配和已冻结数量
                double unFrozenQuantity = inv.getQuantity() - (inv.getAllotQuantity() + inv.getFreezeQuantity());
                // 判断可用数量是否足够分配
                if (unFrozenQuantity > 0) {
                    if (unFrozenQuantity >= quantityToAllocate) {
                        this.toMoveInv(inv, quantityToAllocate, wareId, cellInfo, currentUser, null);
                        break;
                    } else {
                        this.toMoveInv(inv, unFrozenQuantity, wareId, cellInfo, currentUser, null);
                        quantityToAllocate -= unFrozenQuantity;
                        if (quantityToAllocate == 0) {
                            break;
                        }
                    }
                }
            }
        }

        return inventory;
    }


    /**
     * 移库操作 并且保存事务
     * 
     * @param inventory   被移动的库存（必须从数据库查询的）
     * @param quantity    移动的数量
     * @param wareId      仓库
     * @param toCell      移动到的货位信息
     * @param currentUser 当前用户
     * @return 移库后，原库存信息被更新成为了什么样（全部移库返回null）
     */
    private Inventory toMoveInv(Inventory inventory, Double quantity, Integer wareId, CellInfo toCell, CurrentUser currentUser, String transactImgs) {
      try (SyncTool.Closer closer = syncTool.lock(LockName.INVENTORY,inventory.getInventoryId().toString())) {
          Inventory from = new Inventory();
          BeanUtils.copyProperties(inventory, from);
          Inventory to = new Inventory();
          String cellCode = toCell.getCellCode();
          Integer cellUseType = toCell.getCellUseType();
          Inventory newInven = getSameInventory(wareId,
                  cellCode,
                  inventory.getBoxCode(),
                  inventory.getPackDetailId(),
                  inventory.getItemCode(),
                  inventory.getBatchId(),
                  inventory.getTransRatio());
          if (newInven != null) {
              newInven.setQuantity(newInven.getQuantity() + quantity);
              // 分配数量、冻结数量移植
              if (inventory.getQuantity() - quantity < 0.0001) {// 相等
                  newInven.setAllotQuantity(Np.i(newInven.getAllotQuantity()).o(0D) + inventory.getAllotQuantity());
                  newInven.setFreezeQuantity(Np.i(newInven.getFreezeQuantity()).o(0D) + inventory.getFreezeQuantity());
              }
              this.update(newInven);
          } else {
              newInven = new Inventory();
              BeanUtils.copyProperties(inventory, newInven);
              newInven.setInventoryId(null);
              newInven.setVersion(null);
              newInven.setQuantity(quantity);
              // 分配数量、冻结数量移植
              if (inventory.getQuantity() - quantity < 0.0001) {// 相等
                  newInven.setAllotQuantity(inventory.getAllotQuantity());
                  newInven.setFreezeQuantity(inventory.getFreezeQuantity());
              } else {
                  newInven.setAllotQuantity(0.0);
                  newInven.setFreezeQuantity(0.0);
              }
              newInven.setCellCode(cellCode);
              newInven.setCellUseType(cellUseType);
              this.save(newInven);
          }
          inventory.setQuantity(inventory.getQuantity() - quantity);
          if (inventory.getQuantity() < 0.0001) {
              // 将此库存全部移走时，移库带有分配数量和冻结数量
              pickTaskConnector.pickTaskAllMove(inventory.getInventoryId(), newInven.getInventoryId(), cellCode, wareId);
              freezeConnector.freezeAllMove(inventory.getInventoryId(), newInven.getInventoryId(), cellCode, wareId);
              inventoryMapper.deleteByIdVersion(inventory.getInventoryId(), inventory.getVersion());
              inventory = null;
          } else {
              this.update(inventory);
          }

          BeanUtils.copyProperties(newInven, to);
          to.setQuantity(quantity);
          InventoryTransactRunCriteria runCriteria = new InventoryTransactRunCriteria();
          runCriteria.setFromInventory(from);
          runCriteria.setToInventory(to);
          runCriteria.setTransactType(InventoryConstant.YK);
          runCriteria.setCreateUserId(currentUser.getUserId());
          runCriteria.setCreateUserName(currentUser.getUserName());
          runCriteria.setTransactImgs(transactImgs);
          inventoryTransactService.run(runCriteria);
      }
      return inventory;
    }
    @Override
    public List<InventoryDto> findList(InventoryCriteria criteria) {
        return inventoryMapper.findList(criteria);
    }


    /**
     * 查询库存信息，但不区分批次号
     * @return
     */
    @Override
    public List<InventoryDto> findSummaryList(InventoryCriteria criteria) {
        return inventoryMapper.findSummaryList(criteria);
    }
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Inventory> findInventoryList(InventoryCriteria criteria) {
        return (List) this.findList(criteria);
    }

    @Override
    public List<InventoryDto> findListMoreInfo(InventoryCriteria criteria) {
        // 2023-04-12 xuesinuo : 初次优化库存查询语句，将库存查询InventoryDto拆分成单表查询，不再进行Join查询
        List<InventoryDto> resultList = this.findList(criteria);
        for (InventoryDto resultItem : resultList) {
            if(StrUtil.isNotBlank(resultItem.getItemCode())) {
                ItemInfo itemInfo = itemInfoService.findByItemCode(resultItem.getItemCode());// JOIN item_info
                if (itemInfo != null) {
                    // FIXME 【item表-item.id->upc表】改为了【item表-itemCode->upc表】是否OK？
                    // FIXME 缕清UPC与SKU的关系（N-1还是1-1，关联通过ID还是Code），缕清数据，缕清构造，缕清用途
                    List<ItemUpc> itemUpcList = itemUpcService.findByItemCode(itemInfo.getItemCode());
                    resultItem.setUpcs(itemUpcList);// upcs
                    if (StrUtil.isNotBlank(itemInfo.getPackCode())) {
                        List<PackMasterDto> packList = packMasterService.findByPackCode(itemInfo.getPackCode());
                        resultItem.setPackCode(Np.i(packList).iff(x -> !x.isEmpty()).x(x -> x.get(0)).x(x -> x.getPackCode()).o());// packCode
                        resultItem.setPackName(Np.i(packList).iff(x -> !x.isEmpty()).x(x -> x.get(0)).x(x -> x.getPackName()).o());// packName
                    }
                    if (itemInfo.getItemTypeId() != null) {
                        ItemType itemType = itemTypeService.findById(itemInfo.getItemTypeId());
                        resultItem.setItemTypeName(Np.i(itemType).x(x -> x.getItemTypeName()).o());// itemTypeName
                    }
                }
            }
        }
        return resultList;
    }

    public Inventory getSameInventory(Integer wareId, String cellCode, String boxCode, Integer packDetailId, String itemCode, Integer batchId, Double transRatio) {

        InventoryCriteria inventoryCriteria = new InventoryCriteria();
        inventoryCriteria.setWareId(wareId);
        inventoryCriteria.setCellCode(cellCode);
        inventoryCriteria.setBoxCode(boxCode);
        inventoryCriteria.setPackDetailId(packDetailId);
        inventoryCriteria.setItemCode(itemCode);
        inventoryCriteria.setBatchId(batchId);
        inventoryCriteria.setTransRatio(transRatio);
        inventoryCriteria.setPageNum(null);
        inventoryCriteria.setPageSize(null);
        Inventory inventory = inventoryMapper.findSameInventory(inventoryCriteria);
        return inventory;
    }

    public Inventory getAccept(Integer inventoryId, Integer wareId) {
        Inventory inventory = inventoryMapper.getAccept(inventoryId, wareId);
        return inventory;
    }

    public Inventory getAsnInventory(Integer wareId, String boxCode, Integer packDetailId, String itemCode, Integer batchId) {

        InventoryCriteria inventoryCriteria = new InventoryCriteria();
        inventoryCriteria.setWareId(wareId);
        inventoryCriteria.setBoxCode(boxCode);
        inventoryCriteria.setPackDetailId(packDetailId);
        inventoryCriteria.setItemCode(itemCode);
        inventoryCriteria.setBatchId(batchId);
        Inventory inventory = inventoryMapper.findSameInventory(inventoryCriteria);
        return inventory;
    }

    public Inventory findBatchId(Integer batchId) {
        return inventoryMapper.findBatchId(batchId);
    }

    @Override
    public List<InventorySelectDto> findCanBeOutOfStock(InventoryCriteria criteria) {
        return inventoryMapper.findCanBeOutOfStock(criteria);
    }

    @Override
    public List<InventoryDto> canOut(InventoryCriteria criteria) {
        return inventoryMapper.canOut(criteria);
    }

    @Override
    public List<Inventory> getStockInventory(Integer wareId, String cellCode, String boxCode, Integer organizationId, String itemCode) {
        InventoryCriteria inventoryCriteria = new InventoryCriteria();
        inventoryCriteria.setWareId(wareId);
        inventoryCriteria.setCellCode(cellCode);
        inventoryCriteria.setBoxCode(boxCode);
        inventoryCriteria.setItemCode(itemCode);
        inventoryCriteria.setOrganizationId(organizationId);
        List<Inventory> list = inventoryMapper.getStockInventory(inventoryCriteria);
        return list;
    }

    @Override
    public List<Inventory> findSame(InventoryCriteria criteria) {
        List<Inventory> list = inventoryMapper.findSame(criteria);
        return list;
    }

    @Override
    public List<Inventory> findByCellCode(InventoryCriteria criteria) {
        return inventoryMapper.findByCellCode(criteria);
    }

    @Override
    public List<Inventory> findByCellCodeAndState(InventoryCriteria criteria) {
        return inventoryMapper.findByCellCode(criteria);
    }

    @Override
    public List<Inventory> checkList(InventoryCriteria criteria) {
        return inventoryMapper.checkList(criteria);
    }

    @Override
    public List<Inventory> findBaoKuan(Integer wareId) {
        return inventoryMapper.findBaoKuan(wareId);
    }

    @Override
    public List<Inventory> findBatchInventory(InventoryCriteria criteria) {
        return inventoryMapper.findBatchInventory(criteria);
    }

    @Override
    public Double findByItemWareOrgGroupByItem(Integer wareId, Integer orgId, String itemCode) {
        return inventoryMapper.findByItemWareOrgGroupByItem(wareId, orgId, itemCode);
    }

    @Override
    public List inventorySafetyList(InventoryCriteria criteria) {
        return inventoryMapper.inventorySafetyList(criteria);
    }

    @Override
    public List<Inventory> palletsToCombine(InventoryCriteria criteria) {
        return inventoryMapper.palletsToCombine(criteria);
    }

    @Override
    public List<Inventory> inventoriesWithBoxCode(String boxCode) {
        return inventoryMapper.inventoriesWithBoxCode(boxCode);
    }

    @Override
    public List<GroupByWareOrgItem> findGroupByWareOrgItem(InventoryCriteria criteria) {
        return inventoryMapper.findGroupByWareOrgItem(criteria);
    }

    // 不考虑批次，单位，跟踪号等等
    @Override
    public String findSameItemCell(String itemCode, Integer wareId, Integer orgId) {
        return inventoryMapper.findSameItemCell(itemCode, wareId, orgId);
    }

    @Override
    public void sendOut(String itemCode, Integer wareId) {
        inventoryMapper.sendOut(itemCode, wareId);
    }

    @Override
    public List<Inventory> getInventoryByFromInfo(StockTakeMaster stockTakeMaster) {
        return inventoryMapper.getInventoryByFromInfo(stockTakeMaster);
    }

    @Override
    public String findPickCellCode(String itemCode, Integer wareId, Integer organizationId) {
        return inventoryMapper.findPickCellCode(itemCode, wareId, organizationId);
    }

    @Override
    public List<Inventory> canBh(String itemCode, Integer wareId, Integer organizationId) {
        return inventoryMapper.canBh(itemCode, wareId, organizationId);
    }

    @Synchronized(lockName = LockName.INVENTORY, value = "#inventory.inventoryId")
    @Override
    // TODO 查看一下引用，看看哪些场景会更新库存，换成一种新的更新方式（增减更新）
    public int update(Inventory inventory) {
        if (!negativeQty && inventory.getQuantity() < (inventory.getFreezeQuantity() + inventory.getAllotQuantity())) {
            throw new ServiceException(CommonCode.INVENTORY_VERSION, "库存不足！库存号：" + inventory.getInventoryId());
        }
        int rowCount;
        if (inventory.getQuantity() == 0 && inventory.getFreezeQuantity() == 0 && inventory.getAllotQuantity() == 0) {
            rowCount = inventoryMapper.deleteByIdVersion(inventory.getInventoryId(), inventory.getVersion());
        } else {
            rowCount = inventoryMapper.update(inventory);
        }
        if (rowCount == 0) {
            throw new ServiceException(CommonCode.INVENTORY_VERSION, "update inventory更新了0条数据：" + inventory.getInventoryId() + "," + inventory.getVersion());
        }
        updateCellStatus(inventory.getCellCode(), inventory.getWareId());
        return rowCount;
    }

    @Override
    public int addAllotQuantity(Integer id, Double quantity) {
        int rowCount = inventoryMapper.addAllotQuantity(id, quantity);
        if (rowCount == 0) {
            throw new ServiceException(CommonCode.INVENTORY_VERSION, "inv addAllotQty更新了0条数据：" + id + "," + quantity);
        }
        return rowCount;
    }

    @Override
    public void save(Inventory inventory) {
        inventory.setInventoryId(null);
        if (Np.i(null).in(inventory.getWareId(), inventory.getOrganizationId(), inventory.getCellCode(), inventory.getItemCode(), inventory.getQuantity(), inventory.getState())) {
//            throw new ServiceException(CommonCode.SYSTEM_ERROR, "新建库存缺少必要的参数");
            log.error("新建库存缺少必要的参数: "
                    + Np.i(inventory.getWareId()).toString() + " "
                    + Np.i(inventory.getOrganizationId()).toString() + " "
                    + Np.i(inventory.getCellCode()).toString() + " "
                    + Np.i(inventory.getItemCode()).toString() + " "
                    + Np.i(inventory.getQuantity()).toString() + " "
                    + Np.i(inventory.getState()).toString());
        }
        InventoryCriteria ic = new InventoryCriteria();
        ic.setCellCode(inventory.getCellCode());
        ic.setWareId(inventory.getWareId());
        List<InventoryDto> existList = inventoryMapper.findList(ic);
        Set<String> existSkus = existList.stream().map(x -> x.getItemCode()).collect(Collectors.toSet());
        if (!existSkus.contains(inventory.getItemCode())) {
            CellInfo cell = cellInfoService.findByCodeAndWareId(inventory.getCellCode(), inventory.getWareId());
            if (cell.getSkuLimit() != null && cell.getSkuLimit() <= existSkus.size()) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "货位" + cell.getCellCode() + "，SKU种类已达上限：" + cell.getSkuLimit());
            }
        }
        super.save(inventory);// this 死循环
        updateCellStatus(inventory.getCellCode(), inventory.getWareId());
    }

    @Override
    public void save(List<Inventory> inventoryList) {
        for (Inventory inventory : inventoryList) {
            this.save(inventory);
        }
    }

    @Override
    // No @Async !
    public void updateCellStatus(String cellCode, Integer wareId) {
        // TODO 含负库存视为有效货位，那0呢？0什么时候不会删除？是否需要删除？是否需要过滤一遍数据？
        int cellCount = inventoryMapper.cellCount(cellCode, wareId);
        CellInfo cellInfoUpdater = new CellInfo();
        cellInfoUpdater.setCellCode(cellCode);
        cellInfoUpdater.setStatus(cellCount > 0 ? 1 : 0);
        cellInfoService.updateCellInfo(cellInfoUpdater, wareId);
    }

    @Override
    public Inventory findByItemWareOrg(String itemCode, Integer wareId, Integer orgId, String cellCode) {
        return inventoryMapper.findByItemWareOrg(itemCode, wareId, orgId, cellCode);
    }

    @Override
    public List<InventoryDto> getInventoryByCellType(String itemCode, Integer wareId, Integer orgId) {
        return inventoryMapper.getInventoryByCellType(itemCode, wareId, orgId);
    }

    /**
     * 库存回收
     *
     * @param criteria
     */
    @Override
    public void inventoryRecovery(InventoryRecoveryCriteria criteria, CurrentUser currentUser) {
        List<Integer> inventoryIds = criteria.getInventoryIds();
        for (Integer id : inventoryIds) {
            Inventory inventory1 = findBy("inventoryId", id);
            if (Np.i(inventory1.getCellUseType()).notEq(3)) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "仅可移动集货位上的货物！");
            }
            Inventory inventory = new Inventory();
            inventory.setInventoryId(id);
            inventory.setState(-2);
            inventory.setRecoveryTime(new Date());
            inventory.setRecoveryUserId(currentUser.getUserId());
            inventory.setRecoveryUserName(currentUser.getUserName());
            inventory.setRecoveryReason(criteria.getRecoveryReason());
            inventory.setVersion(inventory1.getVersion());
            inventoryMapper.update(inventory);
        }
    }

    /**
     * FIXME 用完记得删除
     * 
     * 用于刷新库存表中的CellUseType字段
     */
    @Deprecated
    public void inventoryRefreshCellUseType() {
        // 货位Code转类型
        List<CellInfo> cellList = cellInfoService.findAll();
        Map<String, Integer> cellCodeToCellUseType = cellList.stream().collect(Collectors.toMap(x -> x.getCellCode().toLowerCase(), x -> x.getCellUseType()));
        Map<String, String> cellCodeToCellCode = cellList.stream().collect(Collectors.toMap(x -> x.getCellCode().toLowerCase(), x -> x.getCellCode()));
        int minId = 0;// 查询的最小ID（不含）
        for (;;) {
            List<Inventory> invList = inventoryMapper.findListByMinId(minId + 1, 1000);
            for (Inventory inv : invList) {
                if (inv != null && inv.getCellCode() != null
                        && cellCodeToCellUseType.get(inv.getCellCode().toLowerCase()) != null
                        && (Np.i(inv.getCellUseType()).notEq(cellCodeToCellUseType.get(inv.getCellCode().toLowerCase()))
                                || Np.i(inv.getCellCode()).notEq(cellCodeToCellCode.get(inv.getCellCode().toLowerCase())))) {
                    // 需要更新货位类型
                    Inventory entityUpper = new Inventory();
                    entityUpper.setInventoryId(inv.getInventoryId());
                    entityUpper.setVersion(inv.getVersion());
                    entityUpper.setCellUseType(cellCodeToCellUseType.get(inv.getCellCode().toLowerCase()));// 更正货位使用类型
                    entityUpper.setCellCode(cellCodeToCellCode.get(inv.getCellCode().toLowerCase()));// 更正货位大小写
//                    if (entityUpper.getCellUseType().equals(-1)) {// 垃圾货位 状态更新为回收站
//                        entityUpper.setState(-2);
//                    } else {// 更新为正常 TODO 整理state：部分冻结、全部冻结
//                        entityUpper.setState(0);
//                    }
                    inventoryMapper.update(entityUpper);
                }
                if (inv != null && inv.getInventoryId() > minId) {
                    minId = inv.getInventoryId();
                }
            }
            if (invList == null || invList.isEmpty()) {
                break;
            }
        }
    }


    @Override
    @EventListener
    @Transactional(propagation = REQUIRES_NEW)
    public boolean updateInventoryByItemUpdate(UpdateItemInfoEvent event) {
        ItemInfo info = event.getItemInfo();
        InventoryCriteria criteria = new InventoryCriteria();
        criteria.setItemCode(info.getItemCode());
        List<InventoryDto> inventoryDtos = findList(criteria);
        for(InventoryDto inventoryDto:inventoryDtos){
            Inventory inventory = new Inventory();
            boolean isUpdate = false;
            inventory.setInventoryId(inventoryDto.getInventoryId());
            inventory.setVersion(inventoryDto.getVersion());
            if(Np.i(inventoryDto.getItemName()).notEq(info.getItemName())){
                inventory.setItemName(info.getItemName());
                isUpdate =true;
            }
            if(Np.i(inventoryDto.getImgUrl()).notEq(info.getImgUrl())){
                inventory.setImgUrl(info.getImgUrl());
                isUpdate =true;
            }
            if(Np.i(inventoryDto.getModel()).notEq(info.getModel())){
                inventory.setModel(info.getModel());
                isUpdate =true;
            }
            if(Np.i(inventoryDto.getSpec()).notEq(info.getSpec())){
                inventory.setSpec(info.getSpec());
                isUpdate =true;
            }
            if(isUpdate){
                inventoryMapper.update(inventory);
            }
        }
        return true;
    }

    @EventListener
    @Transactional
    public void updateCellInfoByEvent(UpdateCellInfoEvent event) {
//        CellInfo source = event.getSource();
//        CellInfo target = event.getTarget();
        this.inventoryRefreshCellUseTypeByCellCode(event.getTarget());
    }


    /**
     * FIXME 用完记得删除
     *
     * 用于刷新库存表中的sku 信息
     */
    @Deprecated
    @Transactional(rollbackFor = Exception.class,propagation = REQUIRES_NEW)
    public void inventoryRefreshItemInfo() {
        // 货位Code转类型
        Map<String,ItemInfo> itemInfoMap  = itemInfoService.findAll().stream().collect(Collectors.toMap(x->x.getItemCode(),x->x));
        int minId = 0;// 查询的最小ID（不含）
        for (;;) {
            InventoryCriteria ic = new InventoryCriteria();
            ic.setIdMin(minId + 1);
            ic.setPageSize(100);
            List<Inventory> invList = this.findInventoryList(ic);
            for (Inventory inv : invList) {
                //如果商品信息和库存中的信息不匹配，则进行修改
                ItemInfo info = itemInfoMap.get(inv.getItemCode());//info 可能为空
                if(info==null){
                    continue;
                }
                boolean isUpdate = false;
                Inventory entityUpper = new Inventory();
                entityUpper.setInventoryId(inv.getInventoryId());
                entityUpper.setVersion(inv.getVersion());
                if(Np.i(inv.getItemName()).notEq(info.getItemName())){
                    entityUpper.setItemName(info.getItemName());
                    isUpdate =true;
                }
                if(Np.i(inv.getImgUrl()).notEq(info.getImgUrl())){
                    entityUpper.setImgUrl(info.getImgUrl());
                    isUpdate =true;
                }
                if(Np.i(inv.getModel()).notEq(info.getModel())){
                    entityUpper.setModel(info.getModel());
                    isUpdate =true;
                }
                if(Np.i(inv.getSpec()).notEq(info.getSpec())){
                    entityUpper.setSpec(info.getSpec());
                    isUpdate =true;
                }
                if(isUpdate){
                    inventoryMapper.update(entityUpper);
                }
                if (inv.getInventoryId() > minId) {
                    minId = inv.getInventoryId();
                }
            }
            if (invList == null || invList.isEmpty()) {
                break;
            }
        }
    }

    /**
     * @Author luolin
     * @Description 根据货位编码修改库存表的货位使用类型
     * @Date 10:44 2023/6/28
     * @param cellInfo 修改后的货位信息
     * @return
     **/
    public void inventoryRefreshCellUseTypeByCellCode(CellInfo cellInfo) {
        InventoryCriteria inventoryCriteria = new InventoryCriteria();
        inventoryCriteria.setCellCode(cellInfo.getCellCode());
        List<Inventory> invList = inventoryMapper.findByCellCode(inventoryCriteria);
        if (invList != null && invList.size() > 0){
            for (Inventory inv : invList) {
                // 需要更新货位类型
                Inventory entityUpper = new Inventory();
                entityUpper.setInventoryId(inv.getInventoryId());
                entityUpper.setVersion(inv.getVersion());
                entityUpper.setCellUseType(cellInfo.getCellUseType());// 更正货位使用类型
                inventoryMapper.update(entityUpper);
            }
        }
    }

    @Override
    public InventorySelectDto createOrGetRecommendedForPick(Integer orgId, String itemCode, Integer wareId) {
        String cellCode = inventoryTransactService.findHistoryPickCellBySku(itemCode, wareId);
        if (cellCode == null) {
            return null;
        }
        InventorySelectDto recommended = inventoryMapper.findRecommendedForPick(orgId, itemCode, wareId, cellCode);
        if (recommended == null) {
            ItemInfo itemInfo = itemInfoService.findByCode(itemCode, orgId);
            CellInfo cellInfo = cellInfoService.findByCodeAndWareId(cellCode, wareId);
            // 创建一个新的Inv
            ItemBatch itemBatch = new ItemBatch();
            itemBatch.setBatchName("Recommended");
            itemBatch.setItemCode(itemCode);
            itemBatch.setOrganizationId(orgId);
            itemBatch.setItemName(itemInfo.getItemName());
            itemBatchService.save(itemBatch);

            Inventory inv = new Inventory();
            inv.setWareId(wareId);
            inv.setOrganizationId(orgId);
            inv.setCellCode(cellCode);
            inv.setCellUseType(cellInfo.getCellUseType());
            inv.setItemCode(itemCode);
            inv.setItemName(itemInfo.getItemName());
            inv.setState(0);
            inv.setQuantity(0D);
            inv.setAllotQuantity(0D);
            inv.setFreezeQuantity(0D);
            inv.setPackDetailId(166);
            inv.setPackDescribe("件");
            inv.setTransRatio(1D);
            inv.setAllotTransRatio(1D);
            inv.setFreezeTransRatio(1D);
            inv.setBatchId(itemBatch.getBatchId());
            inv.setBatchName("Recommended");
            this.save(inv);
            return inventoryMapper.findRecommendedForPick(orgId, itemCode, wareId, cellCode);
        }
        return recommended;
    }
}
