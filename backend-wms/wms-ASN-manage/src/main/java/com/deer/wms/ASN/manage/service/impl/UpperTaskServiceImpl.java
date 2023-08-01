package com.deer.wms.ASN.manage.service.impl;

import com.deer.wms.ASN.manage.service.UpperTaskService;
import com.deer.wms.base.system.model.Cell.CellInfoCriteria;
import com.deer.wms.base.system.model.Cell.CellInfoDto;
import com.deer.wms.base.system.model.Item.ItemInfo;
import com.deer.wms.base.system.model.Item.ItemType;
import com.deer.wms.base.system.model.Pack.PackDetail;
import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.base.system.service.ItemTypeService;
import com.deer.wms.base.system.service.PackDetailService;
import com.deer.wms.busine.tactic.model.Upper.UpperTacticDetail;
import com.deer.wms.busine.tactic.model.Upper.UpperTacticDetailDto;
import com.deer.wms.busine.tactic.service.UpperTacticDetailService;
import com.deer.wms.busine.tactic.service.UpperTacticService;
import com.deer.wms.inventory.constant.InventoryConstant;
import com.deer.wms.ASN.manage.dao.UpperTaskMapper;
import com.deer.wms.inventory.model.InCellTask;
import com.deer.wms.inventory.model.Inventory.*;
import com.deer.wms.ASN.manage.model.upper.UpperTask;
import com.deer.wms.ASN.manage.model.upper.UpperTaskCriteria;
import com.deer.wms.ASN.manage.model.upper.UpperTaskInsert;
import com.deer.wms.inventory.service.*;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.system.manage.model.organization.Organization;
import com.deer.wms.system.manage.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guo on 2019/12/19.
 */
@Service
@Transactional
public class UpperTaskServiceImpl extends AbstractService<UpperTask, Integer> implements UpperTaskService {

    @Autowired
    private UpperTaskMapper upperTaskMapper;

    @Autowired
    private InventoryTransactService inventoryTransactService;

    @Autowired
    private UpperTaskService upperTaskService;

    @Autowired
    private UpperTacticService upperTacticService;

    @Autowired
    private UpperTacticDetailService upperTacticDetailService;

    @Autowired
    private ItemInfoService itemInfoService;

    @Autowired
    private ItemTypeService itemTypeService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PackDetailService packDetailService;

    @Autowired
    private CellInfoService cellInfoService;

    @Autowired
    private ItemBatchService itemBatchService;

    @Autowired
    private SerialNoService serialNoService;
    @Autowired
    private InCellTaskService inCellTaskService;

    @Override
    public List<UpperTask> findList(UpperTaskCriteria criteria) {
        return upperTaskMapper.findList(criteria);
    }

    @Override
    public void upperTask(UpperTaskInsert upperTaskInsert) {
        InventoryTransact inventoryTransact = new InventoryTransact();
//        BeanUtils.copyProperties(upperTaskInsert,inventoryTransact);
        inventoryTransact.setTransactType(InventoryConstant.TRANSACT_TYPE_UP);
        inventoryTransact.setCreateUserId(upperTaskInsert.getUpperTaskUserId());
        inventoryTransact.setCreateUserName(upperTaskInsert.getUpperTaskUserName());
        inventoryTransact.setCreateTime(upperTaskInsert.getCreateTime());
        inventoryTransact.setItemCode(upperTaskInsert.getItemCode());
        inventoryTransact.setItemName(upperTaskInsert.getItemName());
        inventoryTransact.setWareId(upperTaskInsert.getWareId());
        inventoryTransact.setWareName(upperTaskInsert.getWareName());
        inventoryTransact.setToWareId(upperTaskInsert.getWareId());
        inventoryTransact.setToWareName(upperTaskInsert.getWareName());
        inventoryTransact.setFromOrganizationId(upperTaskInsert.getOrganizationId());
        inventoryTransact.setFromOrganizationName(upperTaskInsert.getOrganizationName());
        inventoryTransact.setToOrganizationId(upperTaskInsert.getOrganizationId());
        inventoryTransact.setToOrganizationName(upperTaskInsert.getOrganizationName());
        inventoryTransact.setFromBatchId(upperTaskInsert.getBatchId());
        inventoryTransact.setFromBatchName(upperTaskInsert.getBatchName());
        inventoryTransact.setToBatchId(upperTaskInsert.getBatchId());
        inventoryTransact.setToBatchName(upperTaskInsert.getBatchName());
        inventoryTransact.setFromBoxCode(upperTaskInsert.getFromBoxCode());
        inventoryTransact.setToBoxCode(upperTaskInsert.getToBoxCode());
        inventoryTransact.setFromCellCode(upperTaskInsert.getFromCellCode());
        inventoryTransact.setToCellCode(upperTaskInsert.getToCellCode());
        inventoryTransact.setFromPackDetailId(upperTaskInsert.getPackDetailId());
        inventoryTransact.setFromPackDescribe(upperTaskInsert.getPackDescribe());
        inventoryTransact.setToPackDetailId(upperTaskInsert.getPackDetailId());
        inventoryTransact.setToPackDescribe(upperTaskInsert.getPackDescribe());
        inventoryTransact.setToTransRatio(upperTaskInsert.getTransRatio());
        inventoryTransact.setFromQuantity(upperTaskInsert.getQuantity());
        inventoryTransact.setToQuantity(upperTaskInsert.getToQuantity());

        int inventoryId = inventoryTransactService.run(inventoryTransact);

        List<String> serialNos = upperTaskInsert.getSerialNos();
        if (serialNos != null && serialNos.size() != 0) {
            serialNoService.serialNoRecored(upperTaskInsert.getSerialNos(), upperTaskInsert.getUpperTaskUserId(),
                    upperTaskInsert.getUpperTaskUserName(), null, inventoryId, InventoryConstant.SERIALNO_TRANSFER);
        }
    }

    @Override
    public String findUpperCell(Inventory inventory) {
        return findCellsForInventory(inventory).get(0).getCellCode();
    }

    private List<CellInfoDto> findCellsForInventory(Inventory inventory) {
        String itemCode = inventory.getItemCode();
        List<CellInfoDto> cellInfoDtos = new ArrayList<>();
//        cellInfoCriteria.setCellUseTypes(Arrays.asList(-1,0,2));
        //        这是一个bug

        /**
         * 找到上架策略
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////
         */
        ItemInfo itemInfo = itemInfoService.findBy("itemCode", itemCode);
        String upperTacticCode = itemInfo.getUpperTacticCode();
        List<UpperTacticDetailDto> list = upperTacticDetailService.findByUpperTacticCode(upperTacticCode);
        if (list.size() == 0) {
            Integer itemTypeId = itemInfo.getItemTypeId();
            ItemType itemType = itemTypeService.findById(itemTypeId);
            if (itemType != null) {
                list = upperTacticDetailService.findByUpperTacticCode(itemType.getUpperTacticCode());
            }
        }
        if (list.size() == 0) {
            Integer organizationId = inventory.getOrganizationId();
            Organization organization = organizationService.findById(organizationId);
            upperTacticCode = organization.getUpperTacticCode();
            list = upperTacticDetailService.findByUpperTacticCode(upperTacticCode);
            if (list.size() == 0) {
                throw new ServiceException(CommonCode.UPPER_TACTIC_ERROR);
            }
        }

        /**
         * 产品自身限制
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////
         */
        for (UpperTacticDetail upperTacticDetail : list) {
            Integer requestWareId = upperTacticDetail.getWareId();
            Integer packDetailId = inventory.getPackDetailId();
            PackDetail packDetail = packDetailService.findLevel(packDetailId);
            Integer packLevel = packDetail.getPackLevel();
            Integer requestPackLevel = upperTacticDetail.getPackLevel();
            Integer requestAbcLevel = upperTacticDetail.getAbcLevel();
            Integer abcLevel = itemInfo.getAbcLevel();

            //仓库不符合  过
            if (!inventory.getWareId().equals(requestWareId) && requestWareId != null) {
                continue;
            }
            //单位层级不符合  过
            if (packLevel != requestPackLevel && requestPackLevel != 0) {
                continue;
            }
            //abs不符合 过
            if (abcLevel != requestAbcLevel && requestAbcLevel != 0) {
                continue;
            }

//            ItemBatchCriteria criteria =new ItemBatchCriteria();
//            criteria.setItemCode(itemCode);
//            List<ItemBatchDto> itemBatches = itemBatchService.findList(criteria);
//            for(ItemBatch itemBatch :itemBatches){
//                Integer requestOrderType = upperTacticDetail.getOrderType();
//                Integer orderType = itemBatch.getOrderType();
//                if(orderType != requestOrderType  && requestOrderType != 0){
//                    continue;
//                }
//            }

            /**
             * upperTacticTypes: [
             *       {
             *         value: 1,
             *         label: '收货货位->目标货位'
             *       },
             *       {
             *         value: 2,
             *         label: '收货货位->目标库区'
             *       },
             *       {
             *         value: 3,
             *         label: '指定库区'
             *       },
             *       {
             *         value: 4,
             *         label: '目标库位'
             *       },
             *       {
             *         value: 30,
             *         label: '相同产品相邻储位'
             *       },
             *     ],
             */
            /**
             * 策略方式
             * //////////////////////////////////////////////////////////////////////////////////////////////////////////
             */

            CellInfoCriteria cellInfoCriteria = new CellInfoCriteria();

            Integer wareId = inventory.getWareId();
            cellInfoCriteria.setWareId(wareId);

            Integer upperType = upperTacticDetail.getUpperTacticType();
            switch (upperType) {
                //'收货货位->目标货位'
                case 1: {
                    String acceptCellCode = inventory.getCellCode();
                    String fromCellCode = upperTacticDetail.getFromCellCode();
                    String toCellCode = upperTacticDetail.getToCellCode();
                    if (!acceptCellCode.equals(fromCellCode) || toCellCode == null) {
                        continue;
                    } else {
                        cellInfoCriteria.setCellCode(toCellCode);
                    }

                }

                //'收货货位->目标库区'
                case 2: {
                    String acceptCellCode = inventory.getCellCode();
                    String fromCellCode = upperTacticDetail.getFromCellCode();
                    String toAreaCode = upperTacticDetail.getToAreaCode();
                    if (!acceptCellCode.equals(fromCellCode) || toAreaCode == null) {
                        continue;
                    } else {
                        cellInfoCriteria.setAreaCode(toAreaCode);
                    }

                }

                //'指定库区'
                case 3: {
                    String toAreaCode = upperTacticDetail.getToAreaCode();
                    if (toAreaCode == null) {
                        continue;
                    } else {
                        cellInfoCriteria.setAreaCode(toAreaCode);
                    }

                }

                //''目标库位''
                case 4: {
                    String toCellCode = upperTacticDetail.getToCellCode();
                    if (toCellCode == null) {
                        continue;
                    } else {
                        cellInfoCriteria.setCellCode(toCellCode);
                    }

                }

                //'相同产品相邻储位'
                case 30:

                    String borderItemCode = itemCode;
                    if (borderItemCode == null) {
                        continue;
                    } else {
                        cellInfoCriteria.setBorderItemCode(borderItemCode);
                    }
                    break;

            }

            /**
             * 货位限制
             * //////////////////////////////////////////////////////////////////////////////////////////////////////////
             */

            Integer isEmptyCell = upperTacticDetail.getIsEmptyCell();

            if (isEmptyCell == 1) {
                cellInfoCriteria.setIsEmptyCell(1);
            }

            if (upperTacticDetail.getCellType() != null && upperTacticDetail.getCellType() != 0) {
                cellInfoCriteria.setCellType(upperTacticDetail.getCellType());
            }
            if (upperTacticDetail.getCellUseType() != null && upperTacticDetail.getCellUseType() != 0) {
                cellInfoCriteria.setCellUseType(upperTacticDetail.getCellUseType());
            }
            cellInfoCriteria.setState("enable");

            if (upperTacticDetail.getCellAbcLevel() != null && upperTacticDetail.getCellAbcLevel() != 0) {
                cellInfoCriteria.setCellAbcLevel(upperTacticDetail.getCellAbcLevel());
            }

            cellInfoDtos = cellInfoService.findList(cellInfoCriteria);
            /**
             * 不要求空货位
             */
            if (isEmptyCell == 0) {
                /**
                 * 0-不允许混批
                 * 1-优先混批
                 * 2-不优先混批
                 */
                Integer hunBatch = upperTacticDetail.getNoConfusedBatch();
                /**
                 * 0-不允许混料
                 * 1-优先混料
                 * 2-不优先混料
                 */
                Integer hunItem = upperTacticDetail.getNoConfusedItem();
            }

            if (cellInfoDtos.size() > 0) {
                break;
            }

        }
        if (cellInfoDtos.size() == 0) {
            throw new ServiceException(CommonCode.UPPER_TACTIC_ERROR);
        }

        return cellInfoDtos;
    }

    @Override
    public void generateInCellTask(List<Inventory> inventories) {
        for (Inventory inventory : inventories) {
            List<CellInfoDto> cells = findCellsForInventory(inventory);
            Double inventoryQuantity = inventory.getQuantity();
            if (cells == null || cells.size() == 0) {
                throw new ServiceException(CommonCode.UPPER_TACTIC_ERROR);
            }
            CellInfoDto cell = cells.get(0);
            InCellTask task = new InCellTask();
            task.setBatchId(inventory.getBatchId());
            task.setBatchName(inventory.getBatchName());
            task.setCellCode(cell.getCellCode());
            task.setItemCode(inventory.getItemCode());
            task.setItemName(inventory.getItemName());
            task.setQuantity(inventory.getQuantity());
            inCellTaskService.save(task);
        }
    }
}