package com.deer.wms.inventory.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.base.system.model.Cell.CellInfo;
import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.inventory.constant.InventoryConstant;
import com.deer.wms.inventory.dao.ErpRecordMapper;
import com.deer.wms.inventory.dao.InventoryTransactMapper;
import com.deer.wms.inventory.model.Inventory.ClosePickCellTansactResult;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.Inventory.InventoryTransact;
import com.deer.wms.inventory.model.Inventory.InventoryTransactCriteria;
import com.deer.wms.inventory.model.Inventory.InventoryTransactDto;
import com.deer.wms.inventory.model.Inventory.InventoryTransactRunCriteria;
import com.deer.wms.inventory.model.stockTake.StockTakeMaster;
import com.deer.wms.inventory.service.InventoryAdjustDetailService;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.inventory.service.InventoryTransactService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.system.manage.service.OrganizationService;
import com.deer.wms.system.manage.service.WareInfoService;

/**
 * Created by 郭靖勋 on 2019/12/18.
 */
@Service
@Transactional
public class InventoryTransactServiceImpl extends AbstractService<InventoryTransact, Integer> implements InventoryTransactService {

    @Autowired
    private InventoryTransactMapper inventoryTransactMapper;

    @Autowired
    private InventoryService inventoryService;

    @Resource
    private InventoryAdjustDetailService inventoryAdjustDetailService;

    @Resource
    private WareInfoService wareInfoService;

    @Resource
    private ErpRecordMapper erpRecordMapper;

    @Resource
    private OrganizationService organizationService;

    @Autowired
    private CellInfoService cellInfoService;


    @Override
    public List<InventoryTransactDto> findList(InventoryTransactCriteria criteria) {
        return inventoryTransactMapper.findList(criteria);
    }

    @Override
    public int run(InventoryTransact inventoryTransact) {

        Integer transactType = inventoryTransact.getTransactType();

        /**
         * 库存事务-  或者 盲收
         */
        if (transactType.equals(InventoryConstant.TRANSACT_TYPE_ACCEPT) || transactType.equals(InventoryConstant.TRANSACT_TYPE_BLIND)) {
            CellInfo cellInfo = cellInfoService.findByCodeAndWareId(
                    inventoryTransact.getToCellCode(),
                    inventoryTransact.getWareId());
            if(cellInfo==null){
                throw new ServiceException(CommonCode.SERVICE_ERROR,"货位号无效");
            }
            super.save(inventoryTransact);
            inventoryTransact.setState(1);
            super.update(inventoryTransact);
            Integer wareId = inventoryTransact.getWareId();
            String cellCode = inventoryTransact.getToCellCode();
            String boxCode = inventoryTransact.getToBoxCode();
            Integer packDetailId = inventoryTransact.getToPackDetailId();
            Double transRatio = inventoryTransact.getToTransRatio();
            Double quantity = inventoryTransact.getToQuantity();
            String itemCode = inventoryTransact.getItemCode();
            Integer batchId = inventoryTransact.getToBatchId();
            String batchName = inventoryTransact.getToBatchName();
            Inventory inventory = inventoryService.getSameInventory(wareId, cellCode, boxCode, packDetailId, itemCode, batchId, transRatio);
            if (inventory == null || !inventory.getBillNo().equals(inventoryTransact.getBillNo())) {
                inventory = new Inventory();
                inventory.setOrganizationId(inventoryTransact.getToOrganizationId());
                inventory.setOrganizationName(inventoryTransact.getToOrganizationName());
                inventory.setWareId(wareId);
                inventory.setWareName(inventoryTransact.getWareName());
                inventory.setCellCode(cellCode);
                inventory.setCellUseType(cellInfo.getCellUseType());
                inventory.setBoxCode(boxCode);
                inventory.setPackDetailId(packDetailId);
                inventory.setPackDescribe(inventoryTransact.getToPackDescribe());
                inventory.setItemCode(itemCode);
                inventory.setItemName(inventoryTransact.getItemName());
                inventory.setBatchId(batchId);
                inventory.setBatchName(batchName);
                inventory.setQuantity(quantity);
                inventory.setTransRatio(inventoryTransact.getToTransRatio());
                inventory.setBillNo(inventoryTransact.getBillNo());
                inventory.setWareEntryTime(DateUtils.getNowDateTime());
                inventoryService.save(inventory);

            } else {
                inventory.setQuantity(inventory.getQuantity() + quantity);
                inventoryService.update(inventory);
            }
            return inventory.getInventoryId();
        }
        /**
         * 库存事务-上架
         */
        if (transactType.equals(InventoryConstant.TRANSACT_TYPE_UP)) {
            CellInfo cellInfo = cellInfoService.findByCodeAndWareId(
                    inventoryTransact.getToCellCode(),
                    inventoryTransact.getToWareId());
            super.save(inventoryTransact);
            Integer wareId = inventoryTransact.getWareId();
            String cellCode = inventoryTransact.getFromCellCode();
            String boxCode = inventoryTransact.getFromBoxCode();
            Integer packDetailId = inventoryTransact.getFromPackDetailId();
            Double transRatio = inventoryTransact.getToTransRatio();
            Double quantity = inventoryTransact.getFromQuantity();
            String itemCode = inventoryTransact.getItemCode();
            Integer batchId = inventoryTransact.getFromBatchId();
            Inventory inventory = inventoryService.getSameInventory(wareId, cellCode, boxCode, packDetailId, itemCode, batchId, transRatio);
            if (inventory != null) {
                inventory.setQuantity(inventory.getQuantity() - inventoryTransact.getToQuantity());
                inventoryService.update(inventory);
                //库存为0 直接删除库存
//                if (inventory.getQuantity() == 0) {
//                    inventoryService.deleteByIdVersion(inventory.getInventoryId(), inventory.getVersion());
//                }
            }

            Integer wareId1 = inventoryTransact.getWareId();
            String cellCode1 = inventoryTransact.getToCellCode();
            String boxCode1 = inventoryTransact.getToBoxCode();
            Integer packDetailId1 = inventoryTransact.getToPackDetailId();
            Double transRatio1 = inventoryTransact.getToTransRatio();
            Double quantity1 = inventoryTransact.getToQuantity();
            String itemCode1 = inventoryTransact.getItemCode();
            Integer batchId1 = inventoryTransact.getToBatchId();
            Inventory inventory1 = inventoryService.getSameInventory(wareId1, cellCode1, boxCode1, packDetailId1, itemCode1, batchId1, transRatio1);
            if (inventory1 == null) {
                inventory1 = new Inventory();
                inventory1.setOrganizationId(inventoryTransact.getToOrganizationId());
                inventory1.setOrganizationName(inventoryTransact.getToOrganizationName());
                inventory1.setWareId(inventoryTransact.getToWareId());
                inventory1.setWareName(inventoryTransact.getToWareName());
                inventory1.setCellCode(inventoryTransact.getToCellCode());
                inventory.setCellUseType(cellInfo.getCellUseType());
                inventory1.setBoxCode(inventoryTransact.getToBoxCode());
                inventory1.setPackDetailId(inventoryTransact.getToPackDetailId());
                inventory1.setPackDescribe(inventoryTransact.getToPackDescribe());
                inventory1.setItemCode(itemCode1);
                inventory1.setItemName(inventoryTransact.getItemName());
                inventory1.setBatchId(inventoryTransact.getToBatchId());
                inventory1.setBatchName(inventoryTransact.getToBatchName());
                inventory1.setQuantity(inventoryTransact.getToQuantity());
                inventory1.setTransRatio(inventoryTransact.getToTransRatio());
                inventoryService.save(inventory1);
            } else {
                inventory1.setQuantity(inventory1.getQuantity() + quantity1);
                inventoryService.update(inventory1);
            }
            return inventory1.getInventoryId();
        }
        /**
         * 库存事务-拣货(弃用)
         */

        /**
         * 库存事务-拣货 废弃
         */
        if (transactType.equals(InventoryConstant.PICK_TASK)) {
            CellInfo cellInfo = cellInfoService.findByCodeAndWareId(
                    inventoryTransact.getToCellCode(),
                    inventoryTransact.getToWareId());
            Inventory inventory = inventoryService.findById(inventoryTransact.getFromInventoryId());
            Integer wareId = inventory.getWareId();
            String cellCode = inventory.getCellCode();
            String boxCode = inventory.getBoxCode();
            Integer packDetailId = inventory.getPackDetailId();
            String packDetailName = inventory.getFreezePackDescribe();
            Double transRatio = inventory.getTransRatio();
            Double quantity = inventory.getQuantity();
            String itemCode = inventory.getItemCode();
            Integer batchId = inventory.getBatchId();
            String batchName = inventory.getBatchName();

            inventory.setQuantity(inventory.getQuantity() - inventoryTransact.getToQuantity());
            inventoryService.update(inventory);
            //库存为0 直接删除库存
//            if (inventory.getQuantity() == 0) {
//                inventoryService.deleteByIdVersion(inventory.getInventoryId(), inventory.getVersion());
//            }

            Integer batchId1 = inventoryTransact.getToBatchId();

            //这是到及集货区的货，不和其他库存合并
            Inventory inventory1 = new Inventory();
            inventory1.setOrganizationId(inventoryTransact.getToOrganizationId());
            inventory1.setOrganizationName(inventoryTransact.getToOrganizationName());
            inventory1.setWareId(inventoryTransact.getToWareId());
            inventory1.setWareName(inventoryTransact.getToWareName());
            inventory1.setCellCode(inventoryTransact.getToCellCode());
            inventory.setCellUseType(cellInfo.getCellUseType());
            inventory1.setBoxCode(inventoryTransact.getToBoxCode());
            inventory1.setPackDetailId(inventoryTransact.getToPackDetailId());
            inventory1.setPackDescribe(inventoryTransact.getToPackDescribe());
            inventory1.setTransRatio(inventoryTransact.getToTransRatio());
            inventory1.setItemCode(itemCode);
            inventory1.setItemName(inventoryTransact.getItemName());
            inventory1.setBatchId(inventoryTransact.getToBatchId());
            inventory1.setBatchName(inventoryTransact.getToBatchName());
            inventory1.setAllotQuantity(inventoryTransact.getToQuantity());
            inventory1.setQuantity(inventoryTransact.getToQuantity());
            inventory1.setBillNo(inventoryTransact.getDetailNo());
            inventoryService.save(inventory1);
            return inventory1.getInventoryId();
        }

        /**
         * 库存事务-ASN入库 || ASN 扫描验收
         */
        if (transactType.equals(InventoryConstant.TRANSACT_TYPE_ASN) || transactType.equals(InventoryConstant.TRANSACT_TYPE_SCAN)) {

            inventoryTransact.setState(1);
            super.save(inventoryTransact);
            String toCellCode = null;
            if(inventoryTransact.getToCellCode()==null){
                toCellCode = cellInfoService.getSysAcceptCell(inventoryTransact.getWareId());
            }
            CellInfo cellInfo = cellInfoService.findByCodeAndWareId(
                    inventoryTransact.getToCellCode(),
                    inventoryTransact.getWareId());
            if(cellInfo==null){
                toCellCode = cellInfoService.getSysAcceptCell(inventoryTransact.getWareId());
                cellInfo = cellInfoService.findByCodeAndWareId(
                        toCellCode,
                        inventoryTransact.getWareId());
            }
            Integer wareId = inventoryTransact.getWareId();
            String cellCode = inventoryTransact.getToCellCode();
            String boxCode = inventoryTransact.getToBoxCode();
            Integer packDetailId = inventoryTransact.getToPackDetailId();
            Double transRatio = inventoryTransact.getToTransRatio();
            Double quantity = inventoryTransact.getToQuantity();
            String itemCode = inventoryTransact.getItemCode();
            Integer batchId = inventoryTransact.getToBatchId();
            String batchName = inventoryTransact.getToBatchName();
            Inventory inventory = inventoryService.getSameInventory(wareId, cellCode, boxCode, packDetailId, itemCode, batchId, transRatio);
            if (inventory == null) {
                inventory = new Inventory();
                inventory.setOrganizationId(inventoryTransact.getToOrganizationId());
                inventory.setOrganizationName(inventoryTransact.getToOrganizationName());
                inventory.setWareId(wareId);
                inventory.setWareName(inventoryTransact.getWareName());
                inventory.setCellCode(cellCode);
                inventory.setCellUseType(cellInfo.getCellUseType());
                inventory.setBoxCode(boxCode);
                inventory.setPackDetailId(packDetailId);
                inventory.setPackDescribe(inventoryTransact.getToPackDescribe());
                inventory.setTransRatio(transRatio);
                inventory.setItemCode(itemCode);
                inventory.setItemName(inventoryTransact.getItemName());
                inventory.setSpec(inventoryTransact.getSpec());
                inventory.setModel(inventoryTransact.getModel());
                inventory.setImgUrl(inventoryTransact.getImgUrl());
                inventory.setBatchId(batchId);
                inventory.setBatchName(batchName);
                inventory.setQuantity(quantity);
                inventory.setBillNo(inventoryTransact.getBillNo());
                inventoryService.save(inventory);

            } else {
                inventory.setQuantity(inventory.getQuantity() + quantity);
                inventoryService.update(inventory);

            }
            return inventory.getInventoryId();
        }

        /**
         * 库存事务-库存调整
         */
        if (transactType.equals(InventoryConstant.INVENTORY_ADJUST)) {
            super.save(inventoryTransact);
            List<Inventory> inventoryList = inventoryService.getInventoryByFromInfo(new StockTakeMaster() {{
                setWareId(inventoryTransact.getWareId());
                setOrganizationId(inventoryTransact.getFromOrganizationId());
                setItemCode(inventoryTransact.getItemCode());
                setFromCellCode(inventoryTransact.getFromCellCode());
            }});
            Inventory inventory = inventoryList.get(0);
            inventory.setQuantity(inventoryTransact.getToQuantity());
            inventoryService.update(inventory);
            //库存为0 直接删除库存
//            if (Objects.equals(nvl(inventory.getQuantity()), 0.0d)) {
//                inventoryService.deleteByIdVersion(inventory.getInventoryId(), inventory.getVersion());
//            }
            return inventory.getInventoryId();
        }
        return 0;
    }

//    private void synErpQuantity(InventoryTransact inventoryTransact, Inventory inventory) {
//        // 查询item各库位的库存及库位
//        List<InventoryDto> inventoryCells = inventoryService.getInventoryByCellType(inventoryTransact.getItemCode(), inventoryTransact.getWareId(), inventoryTransact.getFromOrganizationId());
//        JSONObject request = new JSONObject();
//        request.put("itemCode", inventoryTransact.getItemCode());
//        WareInfo wareInfo = wareInfoService.findById(inventoryTransact.getWareId());
//        request.put("wareCode", wareInfo.getWareCode());
//        Organization organization = organizationService.findById(inventory.getOrganizationId());
//        request.put("orgCode", organization.getOrganizationCode());
//        request.put("serialNo", "InventoryTransact-" + inventoryTransact.getInventoryTransactId());
//        JSONArray inventoryListJson = new JSONArray();
//        InventoryAdjustDetail inventoryAdjust = inventoryAdjustDetailService.findBy("detailNo", inventoryTransact.getDetailNo());
//        inventoryCells.forEach(item -> {
//            JSONObject itemJson = new JSONObject().fluentPut("cellCode", item.getCellCode()).fluentPut("quantity", item.getSumQuantity());
//            if (Objects.equals(item.getInventoryId(), inventory.getInventoryId())) {
//                itemJson.put("beforeQuantity", nvl(inventoryAdjust.getAdjustQuantity()) + nvl(inventoryAdjust.getDifferQuantity()));
//            }
//            inventoryListJson.add(itemJson);
//        });
//        if (Objects.equals(nvl(inventory.getQuantity()), 0.0d)) {
//            inventoryListJson.add(new JSONObject().fluentPut("cellCode", inventory.getCellCode()).fluentPut("quantity", 0)
//                    .fluentPut("beforeQuantity", nvl(inventoryAdjust.getAdjustQuantity()) + nvl(inventoryAdjust.getDifferQuantity())));
//        }
//        request.put("inventoryList", inventoryListJson);
//        ErpRecord erpRecord = new ErpRecord();
//        erpRecord.setType(5);
//        erpRecord.setCreateTime(DateUtils.getNowDateTimeString());
//        // 发送请求
//        try {
//            String params = request.toJSONString();
//            erpRecord.setParam(params);
//            erpRecord.setRequestTime(DateUtils.getNowDateTimeString());
//            Map<String, String> map = HttpClient.requestERP(params, baseUrl + synStockUrl, HttpMethod.POST, token);
//            erpRecord.setReturnTime(DateUtils.getNowDateTimeString());
//            if (StringUtils.equals("true", map.get("success"))) {
//                erpRecord.setCode(map.get("code"));
//                erpRecord.setMsg(map.get("msg"));
//                erpRecord.setState(2);
//
//            } else {
//                erpRecord.setCode(map.get("code"));
//                erpRecord.setMsg(map.get("msg"));
//                erpRecord.setState(1);
//            }
//        } catch (Exception e) {
//            erpRecord.setReturnTime(DateUtils.getNowDateTimeString());
//            erpRecord.setState(1);
//            erpRecord.setCode("500");
//            erpRecord.setMsg(e.getMessage());
//        }
//        erpRecordMapper.insert(erpRecord);
//    }

    /**
     * 这个接口用于update或者save inventory
     * @return
     */
    @Override
    public Map run(InventoryTransactRunCriteria criteria) {
        Inventory fromInventory = criteria.getFromInventory();
        Inventory toInventory = criteria.getToInventory();
        Integer transactType = criteria.getTransactType();
        String detailNo = criteria.getDetailNo();
        Integer createUserId = criteria.getCreateUserId();
        String createUserName = criteria.getCreateUserName();
        String transactImgs = criteria.getTransactImgs();

        InventoryTransact inventoryTransact = new InventoryTransact();
        inventoryTransact.setDetailNo(detailNo);
        inventoryTransact.setTransactType(transactType);
        inventoryTransact.setCreateTime(DateUtils.getNowDateTimeString());
        inventoryTransact.setCreateUserId(createUserId);
        inventoryTransact.setCreateUserName(createUserName);
        inventoryTransact.setWareId(toInventory.getWareId());
        inventoryTransact.setWareName(toInventory.getWareName());
        inventoryTransact.setItemCode(toInventory.getItemCode());
        inventoryTransact.setItemName(toInventory.getItemName());
        inventoryTransact.setTransactImgs(transactImgs);
        inventoryTransact.setState(1);

        if(fromInventory!=null)
        {
            inventoryTransact.setFromCellCode(fromInventory.getCellCode());
            inventoryTransact.setFromBoxCode(fromInventory.getBoxCode());
            inventoryTransact.setFromOrganizationId(fromInventory.getOrganizationId());
            inventoryTransact.setFromOrganizationName(fromInventory.getOrganizationName());
            inventoryTransact.setFromBatchId(fromInventory.getBatchId());
            inventoryTransact.setFromBatchName(fromInventory.getBatchName());
            inventoryTransact.setFromPackDetailId(fromInventory.getPackDetailId());
            inventoryTransact.setFromPackDescribe(fromInventory.getPackDescribe());
            inventoryTransact.setFromQuantity(fromInventory.getQuantity());
            inventoryTransact.setFromTransRatio(fromInventory.getTransRatio());
        }

        inventoryTransact.setToBatchId(toInventory.getBatchId());
        inventoryTransact.setToBatchName(toInventory.getBatchName());
        inventoryTransact.setToBoxCode(toInventory.getBoxCode());
        inventoryTransact.setToCellCode(toInventory.getCellCode());
        inventoryTransact.setToOrganizationId(toInventory.getOrganizationId());
        inventoryTransact.setToOrganizationName(toInventory.getOrganizationName());
        inventoryTransact.setToPackDescribe(toInventory.getPackDescribe());
        inventoryTransact.setToPackDetailId(toInventory.getPackDetailId());
        inventoryTransact.setToQuantity(toInventory.getQuantity());
        inventoryTransact.setToTransRatio(toInventory.getTransRatio());


        super.save(inventoryTransact);

        Map map = new HashMap();
        map.put("inventory", fromInventory);
        map.put("inventory", toInventory);
        map.put("inventoryTransact", inventoryTransact);

        return map;
    }

    // FIXME 本方法被 run(InventoryTransactRunCriteria criteria) 替代
    @Override
    public Map run(Inventory fromInventory, Inventory toInventory, Integer transactType, String detailNo, Integer createUserId, String createUserName) {
        InventoryTransact inventoryTransact = new InventoryTransact();

        inventoryTransact.setDetailNo(detailNo);
        inventoryTransact.setTransactType(transactType);
        inventoryTransact.setCreateTime(DateUtils.getNowDateTimeString());
        inventoryTransact.setCreateUserId(createUserId);
        inventoryTransact.setCreateUserName(createUserName);
        inventoryTransact.setWareId(toInventory.getWareId());
        inventoryTransact.setWareName(toInventory.getWareName());
        inventoryTransact.setItemCode(toInventory.getItemCode());
        inventoryTransact.setItemName(toInventory.getItemName());
        inventoryTransact.setState(1);

        if(fromInventory!=null)
        {
            inventoryTransact.setFromCellCode(fromInventory.getCellCode());
            inventoryTransact.setFromBoxCode(fromInventory.getBoxCode());
            inventoryTransact.setFromOrganizationId(fromInventory.getOrganizationId());
            inventoryTransact.setFromOrganizationName(fromInventory.getOrganizationName());
            inventoryTransact.setFromBatchId(fromInventory.getBatchId());
            inventoryTransact.setFromBatchName(fromInventory.getBatchName());
            inventoryTransact.setFromPackDetailId(fromInventory.getPackDetailId());
            inventoryTransact.setFromPackDescribe(fromInventory.getPackDescribe());
            inventoryTransact.setFromQuantity(fromInventory.getQuantity());
            inventoryTransact.setFromTransRatio(fromInventory.getTransRatio());
        }

        inventoryTransact.setToBatchId(toInventory.getBatchId());
        inventoryTransact.setToBatchName(toInventory.getBatchName());
        inventoryTransact.setToBoxCode(toInventory.getBoxCode());
        inventoryTransact.setToCellCode(toInventory.getCellCode());
        inventoryTransact.setToOrganizationId(toInventory.getOrganizationId());
        inventoryTransact.setToOrganizationName(toInventory.getOrganizationName());
        inventoryTransact.setToPackDescribe(toInventory.getPackDescribe());
        inventoryTransact.setToPackDetailId(toInventory.getPackDetailId());
        inventoryTransact.setToQuantity(toInventory.getQuantity());
        inventoryTransact.setToTransRatio(toInventory.getTransRatio());


        super.save(inventoryTransact);

        Map map = new HashMap();
        map.put("inventory", fromInventory);
        map.put("inventory", toInventory);
        map.put("inventoryTransact", inventoryTransact);

        return map;
    }

    @Override
    public String findHistoryPickCellBySku(String itemCode, Integer wareId) {
//        // 从已有库存中查拣货位
//        Inventory pickCellInv = inventoryService.findPickCellBySku(itemCode, wareId);
//        if (Np.i(pickCellInv).x(x -> x.getCellCode()).notNull()) {
//            return pickCellInv.getCellCode();
//        }
        // 从库存事务查拣货位
        ClosePickCellTansactResult closePickTransact = inventoryTransactMapper.findClosePickCellTansactBySku(itemCode, wareId);
        if(closePickTransact != null) {
            if (Np.i(closePickTransact.getToCellUseType()).eq(1)) {
                return closePickTransact.getToCellCode();
            }
        }
        return null;
    }

}
