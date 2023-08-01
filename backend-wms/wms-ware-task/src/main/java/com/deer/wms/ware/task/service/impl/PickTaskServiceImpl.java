package com.deer.wms.ware.task.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.base.system.model.BoxInfo;
import com.deer.wms.base.system.model.Cell.CellInfo;
import com.deer.wms.base.system.service.BoxInfoService;
import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.busine.tactic.model.TurnoverTacticDetail;
import com.deer.wms.busine.tactic.model.TurnoverTacticDetailCriteria;
import com.deer.wms.busine.tactic.service.PickTacticService;
import com.deer.wms.busine.tactic.service.TurnoverTacticDetailService;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.constant.InventoryConstant;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.Inventory.InventoryCriteria;
import com.deer.wms.inventory.model.Inventory.InventorySelectDto;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.inventory.service.InventoryTransactService;
import com.deer.wms.project.root.constant.DataOperationLogModule;
import com.deer.wms.project.root.constant.DataOperationLogOperation;
import com.deer.wms.project.root.constant.LockName;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.model.QrEntity;
import com.deer.wms.project.root.sync.annotation.Synchronized;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DataOperationLogTool;
import com.deer.wms.project.root.util.DataOperationLogTool.DataOperationLog;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.project.root.util.SyncTool;
import com.deer.wms.project.root.util.SyncTool.Closer;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.service.BillRecordService;
import com.deer.wms.ureport.service.ReportOperateService;
import com.deer.wms.ware.task.dao.PickTaskMapper;
import com.deer.wms.ware.task.dao.WaveMasterMapper;
import com.deer.wms.ware.task.model.PickTaskEx;
import com.deer.wms.ware.task.model.PickTaskReturn;
import com.deer.wms.ware.task.model.SO.SoDetail;
import com.deer.wms.ware.task.model.SO.SoDetailDto;
import com.deer.wms.ware.task.model.SO.SoMaster;
import com.deer.wms.ware.task.model.Wave.WaveDetail;
import com.deer.wms.ware.task.model.Wave.WaveMaster;
import com.deer.wms.ware.task.model.pickTask.PickBatchException;
import com.deer.wms.ware.task.model.pickTask.PickBatchItem;
import com.deer.wms.ware.task.model.pickTask.PickParam;
import com.deer.wms.ware.task.model.pickTask.PickSkuQtyBySoParam;
import com.deer.wms.ware.task.model.pickTask.PickTask;
import com.deer.wms.ware.task.model.pickTask.PickTaskCriteria;
import com.deer.wms.ware.task.model.pickTask.PickTaskDto;
import com.deer.wms.ware.task.model.pickTask.PickTaskExecute;
import com.deer.wms.ware.task.model.pickTask.PickTaskMoveCriteria;
import com.deer.wms.ware.task.model.pickTask.PickTaskResetParam;
import com.deer.wms.ware.task.model.pickTask.SeedingWallAlertInfo;
import com.deer.wms.ware.task.service.PickBatchExceptionService;
import com.deer.wms.ware.task.service.PickBatchItemService;
import com.deer.wms.ware.task.service.PickTaskExService;
import com.deer.wms.ware.task.service.PickTaskExecuteService;
import com.deer.wms.ware.task.service.PickTaskReturnService;
import com.deer.wms.ware.task.service.PickTaskService;
import com.deer.wms.ware.task.service.SoDetailService;
import com.deer.wms.ware.task.service.SoMasterService;
import com.deer.wms.ware.task.service.WaveDetailService;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by guo on 2020/02/02.
 */
@Service
@Transactional
@Slf4j
public class PickTaskServiceImpl extends AbstractService<PickTask, Integer> implements PickTaskService {
    @Autowired
    private DataOperationLogTool doLogger;

    @Autowired
    private PickTaskMapper pickTaskMapper;
    @Autowired
    private PickTacticService pickTacticService;
    @Autowired
    private SoDetailService soDetailService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BoxInfoService boxInfoService;
    @Autowired
    private SoMasterService soMasterService;
    @Autowired
    private ReportOperateService reportOperateService;
    @Autowired
    private TurnoverTacticDetailService turnoverTacticDetailService;
    @Autowired
    private SyncTool syncTool;
    @Autowired
    private PickTaskExecuteService pickTaskExecuteService;

    @Autowired
    private InventoryTransactService inventoryTransactService;

    @Autowired
    private PickBatchExceptionService pickBatchExceptionService;

    @Autowired
    private PickBatchItemService pickBatchItemService;

    @Autowired
    private PickTaskService pickTaskService;

    @Override
    public List<PickTaskDto> findList(PickTaskCriteria criteria) {
        List<PickTaskDto> list = pickTaskMapper.findList(criteria);
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        Map<Integer, WaveMaster> collect = waveMasterMapper.findByWaveIds(list.stream().map(PickTaskDto::getWaveId).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(WaveMaster::getWaveId, Function.identity()));
        for (PickTaskDto pickTaskDto : list) {
            WaveMaster waveMaster = collect.get(pickTaskDto.getWaveId());
            if (waveMaster != null) {
                pickTaskDto.setAllotTime(waveMaster.getAllotTime());
                pickTaskDto.setAllotUserId(waveMaster.getAllotUserId());
                pickTaskDto.setAllotUserName(waveMaster.getAllotUserName());
            }

        }
        return list;
    }

    @Override
    public List<PickTaskDto> findByBillNo(PickTaskCriteria criteria) {
        return pickTaskMapper.findByBillNo(criteria);
    }

    @Override
    public List<PickTaskDto> findByWaveMasterId(PickTaskCriteria criteria) {
        return pickTaskMapper.findByWaveMasterId(criteria);
    }

    @Override
    public List<PickTaskDto> findRunningByBillNo(PickTaskCriteria criteria) {
        return pickTaskMapper.findRunningByBillNo(criteria);
    }

    @Override
    public List<PickTaskDto> findByBox(PickTaskCriteria criteria) {
        return pickTaskMapper.findByBox(criteria);
    }

    @Override
    public List<PickTaskDto> allotTask(PickTaskCriteria criteria) {
        return pickTaskMapper.allotTask(criteria);
    }

    @Override
    public List<PickTaskDto> findFirst(PickTaskCriteria criteria) {
        return pickTaskMapper.findFirst(criteria);
    }

    @Override
    public List<PickTaskDto> findBySo(String soNo) {
        return pickTaskMapper.findBySo(soNo);
    }

    @Override
    public List<PickTaskDto> findByWave(Integer waveId) {
        return pickTaskMapper.findByWave(waveId);
    }

    @Override
    public List<SeedingWallAlertInfo> getSeedingWallAlertInfo(PickTaskCriteria criteria) {
        return pickTaskMapper.getSeedingWallAlertInfo(criteria);
    }

    /**
     * 拣货任务合并
     * 
     * @param soDetail
     */
    @Override
    @Synchronized(lockName = LockName.PICK_TASK, value = "#soDetail.billNo")
    public void createPickTask(SoDetailDto soDetail) {
        List<InventorySelectDto> inventoryDtos = getAssignableInventory(soDetail.getTurnoverTacticCode(), soDetail.getItemCode(), soDetail.getWareId(), soDetail.getOrganizationId());
        InventorySelectDto recommendedInv = null;
        Double totalCanBeQuantity = 0.0;
        for (InventorySelectDto inventorySelectDto : inventoryDtos) {
            totalCanBeQuantity += inventorySelectDto.getQuantity() - inventorySelectDto.getAllotQuantity() - inventorySelectDto.getFreezeQuantity();
        }
        if (totalCanBeQuantity < soDetail.getOrderQuantity() - soDetail.getAllottedQuantity()) {
            recommendedInv = inventoryService.createOrGetRecommendedForPick(soDetail.getOrganizationId(), soDetail.getItemCode(), soDetail.getWareId());
            if (recommendedInv == null) {
                SoMaster soMaster = soMasterService.findBy("billNo", soDetail.getBillNo());
                if (soMaster.getState() != 3) {
                    soMaster.setState(3);
                    soMasterService.updateState(soMaster);
                }
                if (soDetail.getState() != 1) {
                    soDetail.setState(1);
                    soDetailService.update(soDetail);
                }
                return;
            } else {
                inventoryDtos = new ArrayList<>(inventoryDtos);
                inventoryDtos.add(recommendedInv);
            }
        }
        // 如果商品数量为0，则不处理
        if (soDetail.getOrderQuantity() < 0.0001) {
            soDetail.setState(-1);
            soDetailService.update(soDetail);
            return;
        }

        SoMaster soMaster = soMasterService.findBy("billNo", soDetail.getBillNo());
        for (InventorySelectDto inventorySelectDto : inventoryDtos) {
            Long time2 = System.currentTimeMillis();
            Double canBeQuantity = inventorySelectDto.getQuantity() - inventorySelectDto.getAllotQuantity() - inventorySelectDto.getFreezeQuantity();
            PickTask pickTask = new PickTask();
            pickTask.setSoDetailId(soDetail.getSoDetailId());
            pickTask.setSoMasterId(soDetail.getSoMasterId());
            pickTask.setWareId(soDetail.getWareId());
            pickTask.setWareName(soDetail.getWareName());
            pickTask.setOrganizationId(soDetail.getOrganizationId());
            pickTask.setOrganizationName(soDetail.getOrganizationName());
            pickTask.setAreaCode(inventorySelectDto.getAreaCode());
            pickTask.setAreaName(inventorySelectDto.getAreaName());
            pickTask.setShelfCode(inventorySelectDto.getShelfCode());
            pickTask.setShelfName(inventorySelectDto.getShelfName());
            pickTask.setCellCode(inventorySelectDto.getCellCode());
            pickTask.setCellName(inventorySelectDto.getCellName());
            pickTask.setItemCode(inventorySelectDto.getItemCode());
            pickTask.setItemName(inventorySelectDto.getItemName());
            pickTask.setBatchId(inventorySelectDto.getBatchId());
            pickTask.setBatchName(inventorySelectDto.getBatchName());
            pickTask.setBoxCode(inventorySelectDto.getBoxCode());
            pickTask.setLpn(inventorySelectDto.getLpn());
            pickTask.setPackDetailId(soDetail.getPackDetailId());
            pickTask.setPackDescribe(soDetail.getPackDescribe());
            pickTask.setPickQuantity(0.0);
            pickTask.setState(0);
            pickTask.setInventoryId(inventorySelectDto.getInventoryId());
            pickTask.setSource(2);
            pickTask.setImgUrl(inventorySelectDto.getImgUrl());
            if (canBeQuantity >= (soDetail.getOrderQuantity() - soDetail.getAllottedQuantity())
                    || "Recommended".equals(inventorySelectDto.getBatchName()) // TODO 推荐货位的批次可以为负数
            ) {
                pickTask.setPickQuantity(soDetail.getOrderQuantity() - soDetail.getAllottedQuantity());
                inventorySelectDto.setAllotQuantity(pickTask.getPickQuantity() + inventorySelectDto.getAllotQuantity());
                soDetail.setAllottedQuantity(soDetail.getAllottedQuantity() + pickTask.getPickQuantity());
                inventoryService.addAllotQuantity(inventorySelectDto.getInventoryId(), pickTask.getPickQuantity());
                if (pickTask.getPickQuantity() < 0) {
                    log.error("出现负数： createPickTask() pickTask.getPickQuantity()=" + pickTask.getPickQuantity());
                }
                if ((pickTask.getPickQuantity() + inventorySelectDto.getAllotQuantity()) < 0) {
                    log.error(inventorySelectDto.getInventoryId() + "invID. 出现负数： createPickTask() 2数相加小于零： 【pickTask.getPickQuantity() + inventorySelectDto.getAllotQuantity()】="
                            + (pickTask.getPickQuantity() + inventorySelectDto.getAllotQuantity()));
                }
                if ((inventorySelectDto.getAllotQuantity() - pickTask.getPickQuantity()) < 0) {
                    log.error(inventorySelectDto.getInventoryId() + "invID. 发现出现负数： createPickTask() 【getAllotQuantity-getPickQuantity】 =getAllotQuantity "
                            + inventorySelectDto.getAllotQuantity() + " - getPickQuantity " + pickTask.getPickQuantity());
                }
                save(pickTask);
                this.savePickTaskDoLog(pickTask, time2);
                break;
            } else {
                double quantity = canBeQuantity;
                if (quantity > 0) {
                    pickTask.setPickQuantity(quantity);
                    inventorySelectDto.setAllotQuantity(inventorySelectDto.getAllotQuantity() + quantity);
                    soDetail.setAllottedQuantity(soDetail.getAllottedQuantity() + quantity);
                    inventoryService.addAllotQuantity(inventorySelectDto.getInventoryId(), quantity);
                    if (pickTask.getPickQuantity() < 0) {
                        log.error("else出现负数： createPickTask() pickTask.getPickQuantity()=" + pickTask.getPickQuantity());
                    }
                    if ((pickTask.getPickQuantity() + inventorySelectDto.getAllotQuantity()) < 0) {
                        log.error(inventorySelectDto.getInventoryId() + "invID. else出现负数： createPickTask() 2数相加小于零： 【pickTask.getPickQuantity() + inventorySelectDto.getAllotQuantity()】="
                                + (pickTask.getPickQuantity() + inventorySelectDto.getAllotQuantity()));
                    }
                    if ((inventorySelectDto.getAllotQuantity() - pickTask.getPickQuantity()) < 0) {
                        log.error(inventorySelectDto.getInventoryId() + "invID. 发现出现负数： createPickTask() 【getAllotQuantity-quantity】 =getAllotQuantity " + inventorySelectDto.getAllotQuantity()
                                + " - quantity " + pickTask.getPickQuantity());
                    }
                    save(pickTask);
                    this.savePickTaskDoLog(pickTask, time2);
                }
            }
        }

        Double orderQuantity = soDetail.getOrderQuantity();
        Double allottedQuantity = soDetail.getAllottedQuantity();
        if (orderQuantity > allottedQuantity) {
            soDetail.setState(1);
        } else if (orderQuantity.equals(allottedQuantity)) {
            soDetail.setState(2);
        } else {
            SoDetail oldSoDetail = soDetailService.findById(soDetail.getSoDetailId());
            soDetail.setState(oldSoDetail.getState());
        }
        List<SoDetail> list = soDetailService.findByBillNo(soDetail.getBillNo());
        Boolean isEnd = true;
        for (SoDetail detail : list) {
            SoDetail newItem = detail;
            if (detail.getSoDetailId().equals(soDetail.getSoDetailId())) {
                newItem = soDetail;
            }
            if (newItem.getState() != -1 && newItem.getOrderQuantity() > newItem.getAllottedQuantity()) {
                isEnd = false;
                break;
            }
        }
        if (isEnd) {
            soMaster.setState(4); // 全部分配
            soMasterService.updateState(soMaster);
        } else {
            soMaster.setState(3); // 部分分配
            soMasterService.updateState(soMaster);
        }
        soDetailService.update(soDetail);
        // TODO xuesinuo soMaster soDetail更新的DoLog
    }

    @Override
    @Synchronized(lockName = LockName.PICK_TASK, value = "#soDetail.billNo")
    public void createPickTask3(SoDetailDto soDetail) {
        StringBuffer sort = new StringBuffer();
        String turnoverTacticCode = soDetail.getTurnoverTacticCode();
        if (turnoverTacticCode == null) {
            turnoverTacticCode = "sys_turnover";
        }
        List<TurnoverTacticDetail> turnoverTacticDetails = turnoverTacticDetailService.findList(new TurnoverTacticDetailCriteria(turnoverTacticCode));
        if (turnoverTacticDetails.size() > 0) {
            for (int i = 0; i < turnoverTacticDetails.size(); i++) {
                TurnoverTacticDetail turnoverTacticDetail = turnoverTacticDetails.get(i);
                if (turnoverTacticDetail.getBatchTacticDetailCode().equals("productDate")) {
                    sort.append("product_date");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("exDate")) {
                    sort.append("ex_date");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("inDate")) {
                    sort.append("in_date");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("supplierCode")) {
                    sort.append("supplier_code");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("supplierBatch")) {
                    sort.append("supplier_batch");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("qcState")) {
                    sort.append("qc_state");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("costPrice")) {
                    sort.append("cost_price");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("salePrice")) {
                    sort.append("sale_price");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("detailNo")) {
                    sort.append("detail_no");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("memo1")) {
                    sort.append("memo1");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("memo2")) {
                    sort.append("memo2");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("memo3")) {
                    sort.append("memo3");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("batchAttribute1")) {
                    sort.append("batch_attribute_1");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("batchAttribute1")) {
                    sort.append("batch_attribute_2");
                }
                // 判断升序或者降序
                if (turnoverTacticDetail.getSort().equals(1)) {
                    sort.append(" asc,");
                } else {
                    sort.append(" desc,");
                }
//                        if((i+1)==turnoverTacticDetails.size()&&sort.length()>0){
//                            sort.substring(0,sort.lastIndexOf(","));
//                        }
            }
        }
        String sortSql = sort.toString();
        sortSql = sortSql.substring(0, sortSql.lastIndexOf(','));
        // Double needAllottedQuantity = soDetail.getOrderQuantity() - soDetail.getAllottedQuantity();
        // PickTaskParams pickTaskParams = createPickTask(soDetail,1,1003,sortSql);
        InventoryCriteria inventoryCriteria = new InventoryCriteria();
        inventoryCriteria.setItemCode(soDetail.getItemCode());
        inventoryCriteria.setCellUseType(1);
        inventoryCriteria.setWareId(soDetail.getWareId());
        inventoryCriteria.setOrganizationId(soDetail.getOrganizationId());
        inventoryCriteria.setPageSize(200);
        // FIXME 此处调用方式已经不对了，此方法也不用了适时销毁
        List<InventorySelectDto> inventoryDtos = inventoryService.findCanBeOutOfStock(inventoryCriteria);
        // 如果库存不够 直接不给分配
        Double totalCanBeQuantity = 0.0;
        for (InventorySelectDto inventorySelectDto : inventoryDtos) {
            totalCanBeQuantity += inventorySelectDto.getQuantity() - inventorySelectDto.getAllotQuantity() - inventorySelectDto.getFreezeQuantity();
        }
        if (totalCanBeQuantity < soDetail.getOrderQuantity() - soDetail.getAllottedQuantity()) {
            if (soDetail.getState() != 1) {
                soDetailService.updateState2(soDetail.getSoDetailId(), 1, soDetail.getState());
            }

            SoMaster soMaster = soMasterService.findBy("billNo", soDetail.getBillNo());
            if (soMaster.getState() != 3) {
                soMaster.setState(3);
                soMasterService.updateState(soMaster);
            }

            return;
        }

        if (soDetail.getOrderQuantity() < 0.001) {
            if (soDetail.getState() != -1) {
                soDetailService.updateState2(soDetail.getSoDetailId(), -1, soDetail.getState());
            }
            return;
        }
        createPickTask3(soDetail, inventoryDtos);

        // 找出被取消订单的拣货任务，全部取消

        // 爆款产品，从验收位置直接生成拣货任务
//        List<Inventory> inventories = inventoryService.findBaoKuan(wareId);
//        for (Inventory in:inventories
//             ) {
//            createBaoKuanPickTask(in);
//        }
    }

    @Autowired
    private CellInfoService cellInfoService;

    @Override
    public void createBaoKuanPickTask(Inventory inventory, Integer acceptRecordId) {

        Integer inventoryId = inventory.getInventoryId();
        String fromCellCode = inventory.getCellCode();
        String toCellCode = cellInfoService.getSysJhCell(inventory.getWareId());
        Inventory newInventory = inventory;
        newInventory.setInventoryId(null);
        newInventory.setQuantity(0.0d);
        inventoryService.save(newInventory);
        Double quantity = inventory.getQuantity();
        Double allotQuantity = 0.0;
        Double dpAllotQuantity = 0.0;
        List<SoDetailDto> soDetailDtos = soDetailService.findBaoKuanByItemCode(inventory.getItemCode(), inventory.getWareId(), inventory.getOrganizationId());
        if (soDetailDtos.size() > 0) {
            for (SoDetailDto soDetail : soDetailDtos) {
                if (soDetail.getSoStructure() > 1) {
                    dpAllotQuantity += soDetail.getOrderQuantity() - soDetail.getAllottedQuantity();
                    continue;
                }
                if (soDetail.getAllottedQuantity() > 0) {
                    dpAllotQuantity += soDetail.getOrderQuantity() - soDetail.getAllottedQuantity();
                    continue;
                }
                if (quantity - allotQuantity - dpAllotQuantity >= soDetail.getOrderQuantity()) {
                    PickTask pickTask = new PickTask();
                    pickTask.setSoDetailId(soDetail.getSoDetailId());
                    pickTask.setSoMasterId(soDetail.getSoMasterId());
                    pickTask.setWareId(soDetail.getWareId());
                    pickTask.setWareName(soDetail.getWareName());
                    pickTask.setOrganizationId(soDetail.getOrganizationId());
                    pickTask.setOrganizationName(soDetail.getOrganizationName());
                    pickTask.setCellCode(fromCellCode);
                    pickTask.setItemCode(inventory.getItemCode());
                    pickTask.setItemName(inventory.getItemName());
                    pickTask.setBatchId(inventory.getBatchId());
                    pickTask.setBatchName(inventory.getBatchName());
                    pickTask.setBoxCode(inventory.getBoxCode());
                    pickTask.setLpn(inventory.getLpn());
                    pickTask.setPackDetailId(soDetail.getPackDetailId());
                    pickTask.setPackDescribe(soDetail.getPackDescribe());
                    pickTask.setPickQuantity(soDetail.getOrderQuantity());
                    pickTask.setState(2);
                    pickTask.setInventoryId(newInventory.getInventoryId());
                    pickTask.setSource(2);
                    pickTask.setImgUrl(inventory.getImgUrl());
                    pickTask.setToCellCode(toCellCode);

                    save(pickTask);
                    soDetail.setAllottedQuantity(pickTask.getPickQuantity());
                    soDetail.setPickTransRatio(1.0);
                    soDetail.setPickQuantity(pickTask.getPickQuantity());
                    soDetail.setState(2);
                    soDetailService.update(soDetail);
                    SoMaster soMaster = soMasterService.findBy("billNo", soDetail.getBillNo());
                    soMaster.setState(6); // 全部拣货
                    soMasterService.updateState(soMaster);
                    allotQuantity += pickTask.getPickQuantity();
                }
            }
        }

        newInventory.setCellCode(toCellCode);
        newInventory.setCellUseType(3); // 集货位
        newInventory.setQuantity(allotQuantity);
        newInventory.setBaoKuan(acceptRecordId);
        inventoryService.update(newInventory);

        inventory.setInventoryId(inventoryId);
        inventory.setCellCode(fromCellCode);
        inventory.setQuantity(quantity - allotQuantity);
        inventory.setAllotQuantity(0.0);
        inventory.setBaoKuan(0);
        inventory.setInventoryId(inventoryId);
        inventory.setCellCode(fromCellCode);
        inventory.setQuantity(quantity - allotQuantity);
        inventory.setAllotQuantity(0.0);
        inventory.setBaoKuan(0);
        inventoryService.update(inventory);

    }

    @Override
    public List<PickTask> findBySoDetailId(Integer soDetailId) {
        return pickTaskMapper.findBySoDetailId(soDetailId);
    }

    @Override
    public List<PickTaskDto> getChildTasksByParentId(Integer parentId) {
        return pickTaskMapper.getChildTasksByParentId(parentId);
    }

    @Override
    public PickTaskDto getTaskById(Integer taskId) {
        return pickTaskMapper.getTaskById(taskId);
    }

    @Override
    public List<PickTask> findBySoMasterId(Integer soMaterId) {
        return pickTaskMapper.findBySoMasterId(soMaterId);
    }

    @Autowired
    private WaveMasterMapper waveMasterMapper;
    @Autowired
    private WaveDetailService waveDetailService;
    @Autowired
    private PickTaskReturnService pickTaskReturnService;
    @Autowired
    private PickTaskExService pickTaskExService;
    @Autowired
    private BillRecordService billRecordService;

    @Override
    public void ex(Integer waveMasterId, String boxCode, Integer wareId, Integer pickTaskId, String toCellCode, Double pickQuantity, Double exQuantity, CurrentUser currentUser) {
        if (waveMasterId != null) {
            WaveMaster waveMaster = waveMasterMapper.findById(waveMasterId);
            waveMasterId = waveMasterId != null ? waveMasterId : Np.i(waveMaster).x(x -> x.getWaveId()).o();
            boxCode = boxCode != null ? boxCode : Np.i(waveMaster).x(x -> x.getBoxCode()).o();
        }
        if (toCellCode == null || toCellCode.trim().equals("")) {
            toCellCode = cellInfoService.getSysJhCell(wareId);
        } else {
            CellInfo cellInfo = cellInfoService.findByCodeAndWareId(toCellCode, wareId);
            if (cellInfo.getCellUseType() != 3) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "该货位不是集货位，不支持拣货");
            }
        }
        PickTask pickTask = findById(pickTaskId);
        if (pickTask == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到拣货任务！");
        }
        waveMasterId = waveMasterId != null ? waveMasterId : Np.i(pickTask).x(x -> x.getWaveMasterId()).o();
        boxCode = boxCode != null ? boxCode : Np.i(pickTask).x(x -> x.getBoxCode()).o();
        if (pickTask.getPickQuantity().equals(pickQuantity)) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "拣货数量满足拣货要求数量,无法执行异常！");
        }
        if (pickTask.getState() == -1 || pickTask.getPickQuantity().equals(0.0d)) {
            throw new ServiceException(CommonCode.PICK_TASK_CANCEL, "该任务已取消！");
        }
        if (pickTask.getState() == 2) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "该任务已经拣货完成！");
        }
        if (Objects.equals(pickTask.getState(), 4)) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "该任务已合并，不支持拣货");
        }
        // 主任务状态修改（这行代码一一定要在子任务处理之前）
        pickTask.setState(2);
        update(pickTask);

        List<PickTaskDto> pickTaskDtos = getChildTasksByParentId(pickTaskId);
        if (pickTaskDtos.size() == 0) {
            pickTaskDtos.add(getTaskById(pickTaskId));
        }
        if (pickTaskDtos.size() == 0) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未找到拣货任务！");
        }

        Double returnQuantity = 0.0;
        Double cancelQuantity = 0.0;
        Double commitQuantity = 0.0;
        for (PickTaskDto pickTaskDto : pickTaskDtos) {
            // 任务大于已经捡的减去分配到前面任务需要的，说明不够了
            if (pickQuantity - commitQuantity < pickTaskDto.getPickQuantity()) {
                // 这里走异常流程
                /**
                 * 查找订单 改变订单状态 把这个订单从该波次里面挑出去 查找订单所有拣货任务 还没有拣的任务 取消掉，恢复所分配的库存 已经拣过的任务，生成还货任务
                 */
                SoMaster soMaster = soMasterService.findById(pickTaskDto.getSoMasterId());
                soMaster.setState(1); // 待处理
                soMasterService.updateState(soMaster);

                soDetailService.cancel(soMaster.getBillNo());
                // 添加SO单跟踪信息
                BillRecord billRecord = new BillRecord();
                billRecord.setBillNo(soMaster.getBillNo());
                billRecord.setRecordType(-11);// 拣货退回订单
                billRecord.setCreateTime(DateUtils.getNowDateTimeString());
                billRecord.setCreateUserId(206);
                billRecord.setCreateUserName("xhctest");
                billRecordService.save(billRecord);
                WaveDetail waveDetail = waveDetailService.findBy("soMasterId", soMaster.getSoMasterId());
                if (waveDetail != null) {
                    waveDetailService.deleteById(waveDetail.getWaveDetailId());
                }

                // 查询订单下所有拣货任务，子任务且未取消
                List<PickTask> exPickTasks = findBySoMasterId(soMaster.getSoMasterId());
                for (PickTask exPickTask : exPickTasks) {
                    // 异常的任务就是当前任务 说明当前任务没有被合并 而且数量不够
                    if (exPickTask.getState() == 2 && exPickTask.getPickTaskId().equals(pickTask.getPickTaskId())) {
                        exPickTask.setState(-1);
                        exPickTask.setParentTaskId(null);
                        update(exPickTask);
                        Inventory inventory = inventoryService.findById(exPickTask.getInventoryId());
                        inventory.setAllotQuantity(inventory.getAllotQuantity() - exPickTask.getPickQuantity());
                        inventoryService.update(inventory);

                        if (pickQuantity > 0.0d) {

                            Inventory inventory1 = new Inventory();
                            inventory1.setOrganizationId(inventory.getOrganizationId());
                            inventory1.setOrganizationName(inventory.getOrganizationName());
                            inventory1.setWareId(inventory.getWareId());
                            inventory1.setWareName(inventory.getWareName());
                            inventory1.setCellCode(toCellCode);
                            inventory1.setCellUseType(3);

                            inventory1.setPackDetailId(inventory.getPackDetailId());
                            inventory1.setPackDescribe(inventory.getPackDescribe());
                            inventory1.setTransRatio(inventory.getTransRatio());
                            inventory1.setItemCode(inventory.getItemCode());
                            inventory1.setItemName(inventory.getItemName());
                            inventory1.setBatchId(inventory.getBatchId());
                            inventory1.setBatchName(inventory.getBatchName());
                            inventory1.setAllotQuantity(0.0);
                            inventory1.setQuantity(pickQuantity);
                            inventoryService.save(inventory1);

                            PickTaskReturn pickTaskReturn = new PickTaskReturn();
                            pickTaskReturn.setFromCellCode(exPickTask.getToCellCode());
                            pickTaskReturn.setWareId(exPickTask.getWareId());
                            pickTaskReturn.setOrgId(exPickTask.getOrganizationId());
                            pickTaskReturn.setItemCode(exPickTask.getItemCode());
                            pickTaskReturn.setItemName(exPickTask.getItemName());
                            pickTaskReturn.setImgUrl(exPickTask.getImgUrl());
                            pickTaskReturn.setFromInventoryId(inventory1.getInventoryId());
                            pickTaskReturn.setToCellCode(exPickTask.getCellCode());
                            pickTaskReturn.setFromCellCode(toCellCode);
                            pickTaskReturn.setQuantity(pickQuantity);
                            pickTaskReturn.setState(0);
                            pickTaskReturn.setType(1);
                            pickTaskReturn.setCreateUserId(currentUser.getUserId());
                            pickTaskReturn.setCreateUserName(currentUser.getUserName());
                            pickTaskReturn.setCreateTime(DateUtils.getNowDateTimeString());
                            pickTaskReturn.setWaveMasterId(waveMasterId);
                            pickTaskReturn.setBoxCode(boxCode);
                            pickTaskReturnService.save(pickTaskReturn);
                        }

                    }
                    // 还没拣货,而且不是父任务
                    else if (exPickTask.getState() == 0 && exPickTask.getIsParent() == 0) {
                        exPickTask.setState(-1);
                        update(exPickTask);
                        Inventory inventory = inventoryService.findById(exPickTask.getInventoryId());
                        inventory.setAllotQuantity(inventory.getAllotQuantity() - exPickTask.getPickQuantity());
                        inventoryService.update(inventory);

                    }
                    // 被合并 而且还没拣
                    else if (exPickTask.getState() == 4) {
                        PickTask parentTask = findById(exPickTask.getParentTaskId());
                        parentTask.setPickQuantity(parentTask.getPickQuantity() - exPickTask.getPickQuantity());
                        update(parentTask);
                        exPickTask.setState(-1);
                        exPickTask.setParentTaskId(null);
                        update(exPickTask);
                        Inventory inventory = inventoryService.findById(exPickTask.getInventoryId());
                        inventory.setAllotQuantity(inventory.getAllotQuantity() - exPickTask.getPickQuantity());
                        inventoryService.update(inventory);

                    }
                    // 已经拣过了
                    else if (exPickTask.getState() == 2) {
                        exPickTask.setState(-1);
                        exPickTask.setParentTaskId(null);
                        update(exPickTask);

                        PickTaskReturn pickTaskReturn = new PickTaskReturn();
                        pickTaskReturn.setFromCellCode(exPickTask.getToCellCode());
                        pickTaskReturn.setWareId(exPickTask.getWareId());
                        pickTaskReturn.setOrgId(exPickTask.getOrganizationId());
                        pickTaskReturn.setItemCode(exPickTask.getItemCode());
                        pickTaskReturn.setItemName(exPickTask.getItemName());
                        pickTaskReturn.setImgUrl(exPickTask.getImgUrl());
                        pickTaskReturn.setFromInventoryId(exPickTask.getInventoryId());
                        pickTaskReturn.setToCellCode(exPickTask.getCellCode());
                        pickTaskReturn.setFromCellCode(exPickTask.getToCellCode());
                        pickTaskReturn.setQuantity(exPickTask.getPickQuantity());
                        pickTaskReturn.setState(0);
                        pickTaskReturn.setType(1);
                        pickTaskReturn.setCreateUserId(currentUser.getUserId());
                        pickTaskReturn.setCreateUserName(currentUser.getUserName());
                        pickTaskReturn.setCreateTime(DateUtils.getNowDateTimeString());
                        pickTaskReturn.setWaveMasterId(waveMasterId);
                        pickTaskReturn.setBoxCode(boxCode);
                        pickTaskReturnService.save(pickTaskReturn);

                    }
                }
            } else {
                commitQuantity += pickTaskDto.getPickQuantity();
                updateBill(pickTaskDto.getSoDetailId(), pickTaskDto.getPickQuantity());
                Inventory inventory = inventoryService.findById(pickTaskDto.getInventoryId());
                // double quantity = MyUtils.calculateMultiplication(inventory.getPackDetailId(), pickTaskDto.getPackDetailId(), pickTaskDto.getPickQuantity(), pickTaskDto.getTransRatio());
                inventory.setAllotQuantity(inventory.getAllotQuantity() - pickTaskDto.getPickQuantity());
                inventory.setQuantity(inventory.getQuantity() - pickTaskDto.getPickQuantity());
                inventoryService.update(inventory);
                Inventory inventory1 = new Inventory();
                inventory1.setOrganizationId(inventory.getOrganizationId());
                inventory1.setOrganizationName(inventory.getOrganizationName());
                inventory1.setWareId(inventory.getWareId());
                inventory1.setWareName(inventory.getWareName());
                inventory1.setCellCode(toCellCode);
                inventory1.setCellUseType(3);

                inventory1.setPackDetailId(inventory.getPackDetailId());
                inventory1.setPackDescribe(inventory.getPackDescribe());
                inventory1.setTransRatio(inventory.getTransRatio());
                inventory1.setItemCode(inventory.getItemCode());
                inventory1.setItemName(inventory.getItemName());
                inventory1.setBatchId(inventory.getBatchId());
                inventory1.setBatchName(inventory.getBatchName());
                inventory1.setAllotQuantity(0.0);
                inventory1.setQuantity(pickTaskDto.getPickQuantity());
                inventoryService.save(inventory1);

                pickTaskDto.setPickTime(DateUtils.getNowDateTimeString());
                pickTaskDto.setPickUserId(currentUser.getUserId());
                pickTaskDto.setPickUserName(currentUser.getUserName());
                pickTaskDto.setToCellCode(toCellCode);
                pickTaskDto.setInventoryId(inventory1.getInventoryId());
                pickTaskDto.setState(2);
                pickTaskDto.setReviewQuantity(pickTaskDto.getPickQuantity());
                update(pickTaskDto);

            }

        }
        // 如果把已经拣的货分配给订单还有多余的 再生成还货任务
        if (pickQuantity - commitQuantity > 0) {
            Inventory inventory1 = new Inventory();
            inventory1.setOrganizationId(pickTask.getOrganizationId());
            inventory1.setOrganizationName(pickTask.getOrganizationName());
            inventory1.setWareId(pickTask.getWareId());
            inventory1.setWareName(pickTask.getWareName());
            inventory1.setCellCode(toCellCode);
            inventory1.setCellUseType(3);

            inventory1.setPackDetailId(pickTask.getPackDetailId());
            inventory1.setPackDescribe(pickTask.getPackDescribe());
            inventory1.setTransRatio(1.0);
            inventory1.setItemCode(pickTask.getItemCode());
            inventory1.setItemName(pickTask.getItemName());
            inventory1.setBatchId(pickTask.getBatchId());
            inventory1.setBatchName(pickTask.getBatchName());
            inventory1.setAllotQuantity(0.0);
            inventory1.setQuantity(pickQuantity - commitQuantity);
            inventoryService.save(inventory1);

            PickTaskReturn pickTaskReturn = new PickTaskReturn();
            pickTaskReturn.setFromCellCode(toCellCode);
            pickTaskReturn.setWareId(wareId);
            pickTaskReturn.setOrgId(pickTask.getOrganizationId());
            pickTaskReturn.setItemCode(pickTask.getItemCode());
            pickTaskReturn.setItemName(pickTask.getItemName());
            pickTaskReturn.setImgUrl(pickTask.getImgUrl());
            pickTaskReturn.setFromInventoryId(inventory1.getInventoryId());
            pickTaskReturn.setFromCellCode(toCellCode);
            pickTaskReturn.setToCellCode(pickTask.getCellCode());

            pickTaskReturn.setQuantity(pickQuantity - commitQuantity);
            pickTaskReturn.setState(0);
            pickTaskReturn.setType(1);
            pickTaskReturn.setCreateUserId(currentUser.getUserId());
            pickTaskReturn.setCreateUserName(currentUser.getUserName());
            pickTaskReturn.setCreateTime(DateUtils.getNowDateTimeString());
            pickTaskReturn.setWaveMasterId(waveMasterId);
            pickTaskReturn.setBoxCode(boxCode);
            pickTaskReturnService.save(pickTaskReturn);
        }

        // 这里先把相应的库存扣减掉，并留下记录
        Inventory exInventory = inventoryService.findById(pickTask.getInventoryId());
        double curFreezeQuantity = exQuantity;// 本次冻结数量 2022年10月14日之前的逻辑为：exInventory.getQuantity() - exInventory.getFreezeQuantity();
        exInventory.setFreezeQuantity(exInventory.getFreezeQuantity() + curFreezeQuantity);
        inventoryService.update(exInventory);

        PickTaskEx pickTaskEx = new PickTaskEx();
        pickTaskEx.setBoxCode(boxCode);
        pickTaskEx.setCellCode(pickTask.getCellCode());
        pickTaskEx.setImgUrl(pickTask.getImgUrl());
        pickTaskEx.setItemCode(pickTask.getItemCode());
        pickTaskEx.setItemName(pickTask.getItemName());
        pickTaskEx.setOrgId(pickTask.getOrganizationId());
        pickTaskEx.setPickTaskId(pickTask.getPickTaskId());
        pickTaskEx.setTotalQuantity(pickTask.getPickQuantity());
        pickTaskEx.setWareId(pickTask.getWareId());
        pickTaskEx.setWaveMasterId(pickTask.getWareId());
        pickTaskEx.setExQuantity(exQuantity);
        pickTaskEx.setPickQuantity(pickQuantity);
        pickTaskEx.setFreezeQuantity(curFreezeQuantity);
        pickTaskEx.setCreateUserId(currentUser.getUserId());
        pickTaskEx.setCreateUserName(currentUser.getUserName());
        pickTaskEx.setCreateTime(DateUtils.getNowDateTimeString());
        pickTaskEx.setInventoryId(exInventory.getInventoryId());
        pickTaskExService.save(pickTaskEx);
    }

    @Override
    public void deleteByIdVersion(Integer pickTaskId, Integer version) {
        int rowCount = pickTaskMapper.deleteByIdVersion(pickTaskId, version);
        if (rowCount == 0) {
            throw new ServiceException(CommonCode.PICK_TASK_VERSION, "请重试！");
        }
    }

    private void updateBill(Integer billDetailId, Double quantity) {
        SoDetail soDetail = soDetailService.findById(billDetailId);
        Double orderQuantity = soDetail.getOrderQuantity();
        soDetail.setPickQuantity(soDetail.getPickQuantity() + quantity);
        if (orderQuantity > soDetail.getPickQuantity()) {
            soDetail.setState(3);
        } else {
            soDetail.setState(4); // 数据库没有这个状态，改为了5
        }
        soDetailService.update(soDetail);

        SoMaster soMaster = soMasterService.findBy("billNo", soDetail.getBillNo());
        List<SoDetail> list = soDetailService.findByBillNo(soDetail.getBillNo());
        Boolean isEnd = true;
        for (SoDetail detail : list) {
            if (detail.getState() != 4) {
                isEnd = false;
                break;
            }
        }
        if (isEnd) {
            soMaster.setState(6);
            soMasterService.updateState(soMaster);
        }

    }

    private void savePickTaskDoLog(PickTask pickTask, Long time1) {
        // DoLog
        Long time2 = System.currentTimeMillis();
        DataOperationLog<PickTask> doLog = new DataOperationLogTool.DataOperationLog<>();
        // DoLog必填项
        doLog.setModule(DataOperationLogModule.PICK_TASK);
        doLog.setOperation(DataOperationLogOperation.PICK_TASK_ADD);
        doLog.setOperationUserId(-1L);
        doLog.setOperationUserName("系统自动触发");
        // DoLog选填项
        doLog.setAfterData(pickTask);
        doLog.setOperationId(pickTask.getPickTaskId().longValue());
        doLog.setTimer(time2 - time1);
        doLog.setWareId(pickTask.getWareId().longValue());
        doLogger.save(doLog);
    }

    // 订单数量增加之后的处理
    private void createPickTask3(SoDetailDto soDetail, List<InventorySelectDto> inventoryDtos) {

        // 如果库存不够 直接不给分配
        Double totalCanBeQuantity = 0.0;
        for (InventorySelectDto inventorySelectDto : inventoryDtos) {
            totalCanBeQuantity += inventorySelectDto.getQuantity() - inventorySelectDto.getAllotQuantity() - inventorySelectDto.getFreezeQuantity();
        }
//        if(totalCanBeQuantity<soDetail.getOrderQuantity()-soDetail.getAllottedQuantity()){
//            return;
//        }
        if (inventoryDtos.size() > 0) {
            InventoryQuantity: for (InventorySelectDto inventorySelectDto : inventoryDtos) {
                Double canBeQuantity = inventorySelectDto.getQuantity() - inventorySelectDto.getAllotQuantity() - inventorySelectDto.getFreezeQuantity();
                PickTask pickTask = new PickTask();
                pickTask.setSoDetailId(soDetail.getSoDetailId());
                pickTask.setSoMasterId(soDetail.getSoMasterId());
                pickTask.setWareId(soDetail.getWareId());
                pickTask.setWareName(soDetail.getWareName());
                pickTask.setOrganizationId(soDetail.getOrganizationId());
                pickTask.setOrganizationName(soDetail.getOrganizationName());
                pickTask.setAreaCode(inventorySelectDto.getAreaCode());
                pickTask.setAreaName(inventorySelectDto.getAreaName());
                pickTask.setShelfCode(inventorySelectDto.getShelfCode());
                pickTask.setShelfName(inventorySelectDto.getShelfName());
                pickTask.setCellCode(inventorySelectDto.getCellCode());
                pickTask.setCellName(inventorySelectDto.getCellName());
                pickTask.setItemCode(inventorySelectDto.getItemCode());
                pickTask.setItemName(inventorySelectDto.getItemName());
                pickTask.setBatchId(inventorySelectDto.getBatchId());
                pickTask.setBatchName(inventorySelectDto.getBatchName());
                pickTask.setBoxCode(inventorySelectDto.getBoxCode());
                pickTask.setLpn(inventorySelectDto.getLpn());
                pickTask.setPackDetailId(soDetail.getPackDetailId());
                pickTask.setPackDescribe(soDetail.getPackDescribe());
                pickTask.setPickQuantity(0.0);
                pickTask.setState(0);
                pickTask.setInventoryId(inventorySelectDto.getInventoryId());
                pickTask.setSource(2);
                pickTask.setImgUrl(inventorySelectDto.getImgUrl());
                if (canBeQuantity >= (soDetail.getOrderQuantity() - soDetail.getAllottedQuantity())) {
                    pickTask.setPickQuantity(soDetail.getOrderQuantity() - soDetail.getAllottedQuantity());
                    inventorySelectDto.setAllotQuantity(pickTask.getPickQuantity() + inventorySelectDto.getAllotQuantity());
                    soDetail.setAllottedQuantity(soDetail.getAllottedQuantity() + pickTask.getPickQuantity());
                    inventoryService.update(inventorySelectDto);
                    save(pickTask);
                    break InventoryQuantity;
                } else {
                    double quantity = inventorySelectDto.getQuantity() - inventorySelectDto.getAllotQuantity() - inventorySelectDto.getFreezeQuantity();
                    if (quantity > 0) {
                        pickTask.setPickQuantity(quantity);
                        inventorySelectDto.setAllotQuantity(inventorySelectDto.getAllotQuantity() + quantity);
                        soDetail.setAllottedQuantity(soDetail.getAllottedQuantity() + quantity);
                        inventoryService.update(inventorySelectDto);
                        save(pickTask);
                    }

                }
            }
        }

        Double orderQuantity = soDetail.getOrderQuantity();
        Double allottedQuantity = soDetail.getAllottedQuantity();

        if (orderQuantity > allottedQuantity) {
            soDetail.setState(1);
        } else if (orderQuantity.equals(allottedQuantity)) {
            soDetail.setState(2);
        }
        soDetailService.update(soDetail);
        SoMaster soMaster = soMasterService.findBy("billNo", soDetail.getBillNo());
        List<SoDetail> list = soDetailService.findByBillNo(soDetail.getBillNo());
        if (soMaster.getState() > 4) {
            soMaster.setState(5); // 部分拣货
            soMasterService.updateState(soMaster);
        }

    }

    private void cancelPickTask(String billNo) {
        List<SoDetail> soDetails = soDetailService.findByBillNo(billNo);
        for (SoDetail soDetail : soDetails) {
            if (soDetail.getState() == 0) {
                return;
            }
        }
        PickTaskCriteria criteria = new PickTaskCriteria();
        criteria.setBillNo(billNo);
        criteria.setState(0);
        List<PickTaskDto> tasks = findList(criteria);
        for (PickTaskDto pickTask : tasks) {
            Integer inventoryId = pickTask.getInventoryId();
            Inventory inventory = inventoryService.findById(inventoryId);
            Double quantity = pickTask.getPickQuantity();
            inventory.setAllotQuantity(inventory.getAllotQuantity() - quantity);
            inventoryService.update(inventory);
            super.deleteById(pickTask.getPickTaskId());
        }
        for (SoDetail soDetail : soDetails) {
            soDetail.setAllottedQuantity(0.0);
            soDetail.setState(0);
            soDetailService.update(soDetail);
        }

    }

    public int update(PickTask pickTask) {
        int rowCount = pickTaskMapper.update(pickTask);
        if (rowCount == 0) {
            throw new ServiceException(CommonCode.PICK_TASK_VERSION, "请重试！");
        }
        return rowCount;
    }

    @Override
    public void mergeTask() {
        // 合并拣货任务
        List<PickTaskDto> mergePickTasks = pickTaskMapper.mergeTask();
        mergePickTasks.forEach(item -> {
            // 边拣边分不需要合并
            if (item.getIsSeeding() == 0) {
                item.setWaveMasterId(item.getWaveId());
                pickTaskMapper.update(item);
            } else {
                List<PickTask> list = pickTaskMapper.findMergeTask(item);
                // 插入合并任务
                item.setPickTaskId(null);
                Double quantity = 0.0;
                for (PickTask pickTask : list) {
                    quantity += pickTask.getPickQuantity();
                }
                if (list.size() == 1) {
                    for (PickTask pickTask : list) {
                        // pickTask.setParentTaskId(item.getPickTaskId());
                        // pickTask.setState(4);
                        pickTask.setWaveMasterId(item.getWaveId());
                        pickTaskMapper.update(pickTask);
                    }
                } else {
                    item.setPickQuantity(quantity);
                    item.setIsParent(1);
                    item.setSoMasterId(null);
                    item.setSoDetailId(null);
                    item.setWaveMasterId(item.getWaveId());
                    save(item);
                    for (PickTask pickTask : list) {
                        pickTask.setParentTaskId(item.getPickTaskId());
                        pickTask.setState(4);
                        pickTask.setWaveMasterId(item.getWaveId());
                        update(pickTask);
                    }
                }
            }
        });
    }

    @Override
    public List<PickTask> findPickingByInventory(Integer inventoryId) {
        return pickTaskMapper.findPickingByInventory(inventoryId);
    }

    /**
     * 拣货任务移动
     * 
     * @param criteria
     */
    @Override
    @Transactional
    public void pickTaskMove(PickTaskMoveCriteria criteria, CurrentUser currentUser) {
        String cellCode = criteria.getCellCode();
        Integer wareId = criteria.getWareId();
        CellInfo cellInfo = cellInfoService.findByCodeAndWareId(cellCode, wareId);
        if (cellInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "货位号无效");
        }
        // 拣货任务，货位进行移动
        for (Integer id : criteria.getPickTaskIds()) {
            // 拣货任务状态必须是待拣货
            // 修改拣货任务的货位，拣货任务对应的库存id也需要修改为对应后的
            PickTask pickTask = this.findById(id);
            if (pickTask.getState() != 0) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "拣货任务状态必须为待拣货！");
            }
            pickTask.setCellCode(cellInfo.getCellCode());
            pickTask.setCellName(cellInfo.getCellName());
            Double quantity = pickTask.getPickQuantity();// 要移动过去的数量
            Inventory inventory = inventoryService.findById(pickTask.getInventoryId());
            Inventory to = new Inventory();
            // 查询要移动过去的货位，是否有相同的货位信息
            Inventory newInven = inventoryService.getSameInventory(wareId,
                    cellInfo.getCellCode(),
                    inventory.getBoxCode(),
                    inventory.getPackDetailId(),
                    inventory.getItemCode(),
                    inventory.getBatchId(),
                    inventory.getTransRatio());
            if (newInven != null) {
                newInven.setQuantity(newInven.getQuantity() + quantity);
                // 分配数量移植植
                newInven.setAllotQuantity(Np.i(newInven.getAllotQuantity()).o(0D) + quantity);
                inventoryService.update(newInven);// 修改移动过去的库存信息
            } else {
                newInven = new Inventory();
                BeanUtils.copyProperties(inventory, newInven);
                newInven.setInventoryId(null);
                newInven.setVersion(null);
                newInven.setQuantity(quantity);
                newInven.setAllotQuantity(quantity);
                newInven.setCellCode(cellInfo.getCellCode());
                newInven.setCellUseType(cellInfo.getCellUseType());
                inventoryService.save(newInven);
            }
            inventory.setQuantity(inventory.getQuantity() - quantity);
            inventory.setAllotQuantity(inventory.getAllotQuantity() - quantity);
            inventoryService.update(inventory);

            BeanUtils.copyProperties(newInven, to);
            to.setQuantity(quantity);
            inventoryTransactService.run(
                    inventory,
                    to,
                    InventoryConstant.PICK_MOVE, // 移库
                    null,
                    currentUser.getUserId(),
                    currentUser.getUserName());
            pickTask.setInventoryId(newInven.getInventoryId());
            update(pickTask);

        }

    }

    @Override
    public List<PickTaskDto> pickTaskMoveList(PickTaskCriteria criteria) {
        return pickTaskMapper.pickTaskMoveList(criteria);
    }

    /**
     * 拣货 验证1：
     *
     * 验证货位是否可成为拣货目标货位，如果没有，尝试按照系统设置返回一个默认值
     *
     * @param cellCode 参数中的CellCode
     * @param wareId   仓库ID
     * @return 验证通过的CellCode，验证不通过则抛出异常
     */
    private String cellCodeCheckAndDefault(String cellCode, Integer wareId) throws ServiceException {
        if (wareId == null) {
            throw new ServiceException(CommonCode.SYSTEM_ERROR, "缺少仓库ID，无法验证货位");
        }
        if (StrUtil.isBlank(cellCode)) {
            cellCode = cellInfoService.getSysJhCell(wareId);
            if (StrUtil.isBlank(cellCode)) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "缺少拣出的目标货位");
            }
        } else {
            CellInfo cellInfo = cellInfoService.findByCodeAndWareId(cellCode, wareId);
            if (cellInfo.getCellUseType() != 3) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "该货位不是集货位，不支持拣货");
            }
        }
        return cellCode;
    }

    /*
     * 确认拣货 {@link PickTaskService.pick}
     */
    // @Synchronized(lockName = LockName.PICK_AFFIRM, value = "#param.pickTaskId") // 改为了编程式
    @Transactional
    @Override
    public void pick(int type, PickParam param) {
        boolean bjbf = (type & 0x1) != 0;// 边拣边分（完成时直接变为已复核）
        boolean boxCodeNecessary = (type & 0x2) != 0;// 容器号必填
        boolean pickPart = (type & 0x4) != 0;// 允许部分拣货
        boolean ymyj = (type & 0x8) != 0;// 一码一拣
        boolean negativeQty = true;// 是否允许负库存
        // 拣货任务执行记录PickTaskExecute
        PickTaskExecute execute = new PickTaskExecute();
        execute.setHasSuccess(false);
        execute.setExecuteTime(new Date());
        execute.setReviewQuantity(param.getReviewQuantity());
        execute.setMemo(Np.i(param.getMemo()).ifBlank(null).o());
        execute.setExecuteUserId(param.getUserId());
        execute.setExecuteUserName(param.getUserName());
        execute.setQrCode(Np.i(param.getQrCode()).ifBlank(null).o());
        execute.setToCellCode(param.getToCellCode());
        execute.setPickTaskId(param.getPickTaskId());
        try (Closer lockCloser = syncTool.lock(LockName.PICK_AFFIRM, param.getPickTaskId().toString())) {
            PickTask pickTask = this.findById(param.getPickTaskId());
            if (pickTask == null) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "未匹配到拣货任务");
            }
            execute.setSoDetailId(pickTask.getSoDetailId());
            execute.setSoMasterId(pickTask.getSoMasterId());
            execute.setWareId(pickTask.getWareId());
            execute.setWareName(pickTask.getWareName());
            execute.setItemCode(pickTask.getItemCode());
            execute.setItemName(pickTask.getItemName());
            Integer fromInvId = Np.i(pickTask.getFromInventoryId()).o(pickTask.getInventoryId());// TODO 缕清From To
            // 货位验证、缺省值
            String cellCode = this.cellCodeCheckAndDefault(param.getToCellCode(), pickTask.getWareId());
            param.setToCellCode(cellCode);
            // 容器号
            String boxCode = Np.i(param.getBoxCode()).ifBlank(null).o();
            if (boxCode != null) {
                BoxInfo boxInfo = boxInfoService.findByBoxCode(boxCode, 1, pickTask.getWareId());
                if (boxInfo == null) {
                    throw new ServiceException(CommonCode.SERVICE_ERROR, "该容器号无效");
                }
            } else {
                if (boxCodeNecessary) {
                    throw new ServiceException(CommonCode.PARAMETER_ERROR, "缺少容器号");
                }
            }
            execute.setBoxCode(boxCode);
            // 验证拣货任务，更新拣货任务BoxCode与状态
            boolean isPickPart = Np.i(pickTask.getReviewQuantity()).ifNull(0D)
                    .x(x -> x + param.getReviewQuantity())
                    .notEq(pickTask.getPickQuantity());// 当前是部分拣货
            if (!pickPart && isPickPart) {
                throw new ServiceException(CommonCode.PICK_TASK_QUANTITY, "拣货任务数量已改变，请重新确认", pickTask);
            }
            if (pickTask.getState() == -1 || pickTask.getPickQuantity().equals(0.0d)) {
                throw new ServiceException(CommonCode.PICK_TASK_CANCEL, "该任务已取消！");
            }
            if (pickTask.getState() == 2) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "该任务已经拣货完成！");
            }
            if (Objects.equals(pickTask.getState(), 4)) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "该任务已合并，不支持拣货");// 已合并（4）的是子任务，需要操作住任务（parentPickTaskId）
            }
            // 【2023-05-23 废弃】原逻辑，将拣货单任务合并：1.父单中记录总数，2.子单中记录父单ID，3.标记父单完成，实际子单完成
            // 不再合并拣货，不再记录父单ID；如需合并，在查询时做合并，拣货时按SO+SKU提交，自动分配到具体拣货任务上
//            pickTask.setState(2);
//            pickTask.setBoxCode(param.getBoxCode());
//            this.update(pickTask);// 主任务状态修改（这行代码一一定要在子任务处理之前）
            List<PickTaskDto> pickTaskDtos = this.getChildTasksByParentId(param.getPickTaskId());// 子任务List
            if (!pickTaskDtos.isEmpty()) {
                throw new ServiceException(CommonCode.SYSTEM_ERROR, "合并拣货任务功能已废弃，此单无法拣货，请联系管理员处理");
            }
            // 验证一码一扫
            if (ymyj) {
                if (StrUtil.isBlank(param.getQrCode())) {
                    throw new ServiceException(CommonCode.SYSTEM_ERROR, "缺少二维码信息");
                }
                List<PickTaskExecute> qrPickTaskExecuteList = pickTaskExecuteService.getByQrCode(param.getQrCode());
                if (qrPickTaskExecuteList != null && !qrPickTaskExecuteList.isEmpty()) {
                    if (qrPickTaskExecuteList.stream().filter(x -> x.getHasSuccess()).count() > 0) {
                        throw new ServiceException(CommonCode.SYSTEM_ERROR, "此二维码已经扫描过");
                    }
                }
            }
            // SO单更新
            SoDetail soDetail = soDetailService.findById(pickTask.getSoDetailId());// 本拣货任务对应的详情
            Double orderQuantity = soDetail.getOrderQuantity();
            soDetail.setPickQuantity(soDetail.getPickQuantity() + param.getReviewQuantity());// 拣（拣货）
            if (bjbf) {
                soDetail.setCheckQuantity(soDetail.getPickQuantity());// 分（复核）：如果前几次拣货没有边拣边分，本次边拣边分将之前的只拣货一起分出来
            }
            if (orderQuantity > soDetail.getPickQuantity()) {
                soDetail.setState(3);// SO-部分拣货
            } else {
                soDetail.setState(bjbf ? 5 : 4);// SO-完成。边拣边分：直接变为复核；否则变为完成拣货
            }
            soDetailService.update(soDetail);
            SoMaster soMaster = soMasterService.findBy("billNo", soDetail.getBillNo());
            List<SoDetail> list = soDetailService.findByBillNo(soDetail.getBillNo());
            Boolean isEnd = true;
            for (SoDetail detail : list) {
                if (Np.i(detail.getState()).in(0, 1, 2, 3)) {
                    isEnd = false;
                    break;
                }
            }
            if (isEnd) {
                soMaster.setState(bjbf ? 7 : 6);// SO-完成。边拣边分：直接变为复核；否则变为完成拣货
                soMasterService.updateState(soMaster);
            }
            // 记录绩效
            if (!isPickPart) {
                reportOperateService.report(
                        soMaster.getWareId(),
                        param.getUserId(),
                        3,
                        1,
                        isEnd ? 1 : 0,
                        soDetail.getBillNo(),
                        soDetail.getItemCode(),
                        soDetail.getItemName(),
                        soDetail.getImgUrl());
            }
            // 库存更新 TODO 在inventoryService封装成移库通用方法即可简单调用
            // 减库存
            Inventory fromInventory = inventoryService.findById(fromInvId);
            if (null == fromInventory) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, fromInvId + "：InventoryId本商品库存记录为空" + " ErrorCode: PTC10");
            } else if (null == fromInventory.getAllotQuantity()) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "本商品分配数量为空" + " ErrorCode: PTC11");
            } else if (fromInventory.getAllotQuantity() - param.getReviewQuantity() < 0) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "分配数不足，无法拣货");
            }
            execute.setFromCellCode(fromInventory.getCellCode());
            fromInventory.setAllotQuantity(fromInventory.getAllotQuantity() - param.getReviewQuantity());
            fromInventory.setQuantity(fromInventory.getQuantity() - param.getReviewQuantity());
            if (!negativeQty && fromInventory.getQuantity() - fromInventory.getAllotQuantity() - fromInventory.getFreezeQuantity() < 0D) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, fromInventory.getItemName() + "库存不足，无法拣货");
            }
            inventoryService.update(fromInventory);
            // 增库存
            Inventory newInv = inventoryService.findById(pickTask.getInventoryId());
            if (newInv != null && newInv.getCellUseType() == 3) {// TODO 缕清From To 此处为甚要且？目前InventoryId存放的还是可变的，可能存放From。（首次拣货）From时不追加，（非首次拣货）To时才追加。
                newInv.setQuantity(newInv.getQuantity() + param.getReviewQuantity());
                inventoryService.update(newInv);
            } else {
                newInv = new Inventory();
                newInv.setOrganizationId(fromInventory.getOrganizationId());
                newInv.setOrganizationName(fromInventory.getOrganizationName());
                newInv.setWareId(fromInventory.getWareId());
                newInv.setWareName(fromInventory.getWareName());
                newInv.setCellCode(param.getToCellCode());
                newInv.setCellUseType(3);
                newInv.setPackDetailId(fromInventory.getPackDetailId());
                newInv.setPackDescribe(fromInventory.getPackDescribe());
                newInv.setTransRatio(fromInventory.getTransRatio());
                newInv.setItemCode(fromInventory.getItemCode());
                newInv.setItemName(fromInventory.getItemName());
                newInv.setBatchId(fromInventory.getBatchId());
                newInv.setBatchName(fromInventory.getBatchName());
                newInv.setAllotQuantity(0.0);
                newInv.setQuantity(pickTask.getPickQuantity());
                newInv.setBoxCode(boxCode);
                if (bjbf) {// TODO 为啥边拣边分时要用SoMasterId？搞清楚之后备注到Inventory
                    newInv.setSoMasterId(pickTask.getSoMasterId());
                }
                inventoryService.save(newInv);// 新库位增库存
            }
            // 拣货任务更新
            pickTask.setPickTime(DateUtils.getNowDateTimeString());
            pickTask.setPickUserId(param.getUserId());
            pickTask.setPickUserName(param.getUserName());
            pickTask.setMemo(Np.i(param.getMemo()).ifBlank(null).o());
            pickTask.setToCellCode(param.getToCellCode());
            pickTask.setInventoryId(newInv.getInventoryId());
            pickTask.setState(isPickPart ? 1 : 2);
            pickTask.setReviewQuantity(Np.i(pickTask.getReviewQuantity()).o(0D) + param.getReviewQuantity());
            pickTask.setFromInventoryId(fromInvId);
            this.update(pickTask);
            execute.setHasSuccess(true);
        } finally {
            pickTaskExecuteService.save(execute);
        }
    }

    @Synchronized(lockName = LockName.PICK_AFFIRM_SO_SKU, value = "#param.soBillNo + #param.itemCode")
    @Transactional
    @Override
    public void pickAsSoAndSku(int type, PickSkuQtyBySoParam param) {
        boolean pickPart = (type & 0x4) != 0;// 允许部分拣货
        // 拣货任务执行记录PickTaskExecute
        PickTaskExecute execute = new PickTaskExecute();
        execute.setHasSuccess(false);
        execute.setExecuteTime(new Date());
        execute.setReviewQuantity(param.getReviewQuantity());
        execute.setMemo(Np.i(param.getMemo()).ifBlank(null).o());
        execute.setExecuteUserId(param.getUserId());
        execute.setExecuteUserName(param.getUserName());
        execute.setQrCode(Np.i(param.getQrCode()).ifBlank(null).o());
        execute.setToCellCode(param.getToCellCode());
        execute.setFromCellCode(param.getCellCode());
        execute.setBoxCode(param.getBoxCode());
        execute.setItemCode(param.getItemCode());
        LinkedHashMap<Integer, Double> pickTaskId2Qty = new LinkedHashMap<>();// 为各个拣货任务分配数量的键值对
        try {
            SoMaster so = soMasterService.findByBillNo(param.getSoBillNo());
            execute.setWareId(Np.i(so).x(x -> x.getWareId()).o());
            execute.setWareName(Np.i(so).x(x -> x.getWareName()).o());
            if (Np.i(so).x(x -> x.getState()).notIn(3, 4, 5)) {// 3-部分分配 4-分配完成 5-部分拣货
                throw new ServiceException(CommonCode.SERVICE_ERROR, "该订单状态无需拣货，请核对订单状态与商品数量");
            }
            List<PickTask> pickTaskList = this.findBySoMasterId(so.getSoMasterId()).stream()
                    .filter(x -> x.getItemCode().equals(param.getItemCode()))
                    .filter(x -> x.getPickQuantity() > x.getReviewQuantity())
                    .collect(Collectors.toList());// 总拣货任务
            if (pickTaskList.isEmpty()) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "此SKU拣出的数量已足够，无需再拣货");
            }
            // 按顺序分配拣货任务。例如拣货任务有3、5、10，本次拣了10，会拆成{1号:3}、{2号:5}、{3号:2}
            // TODO 可以优化为，尽可能正好满足本次要求的拣货数量
            Double reviewQuantity = param.getReviewQuantity();// 本次想要拣出数量（剩余分配）
            for (PickTask pickTask : pickTaskList) {
                if (param.getCellCode() != null) {// 指定拣货的货位
                    if (!param.getCellCode().equals(pickTask.getCellCode())) {
                        continue;
                    }
                }
                Double taskQty = pickTask.getPickQuantity() - pickTask.getReviewQuantity();// 本任务需要拣出数量
                if (taskQty < reviewQuantity) {// 本任务分完 不足要求的数量
                    pickTaskId2Qty.put(pickTask.getPickTaskId(), taskQty);
                    reviewQuantity -= taskQty;
                    continue;
                } else {// 本任务分完 以满足要求的数量
                    pickTaskId2Qty.put(pickTask.getPickTaskId(), reviewQuantity);
                    reviewQuantity -= taskQty;
                    if (reviewQuantity < 0 && !pickPart) {// 本任务超出要求的数量
                        throw new ServiceException(CommonCode.SERVICE_ERROR, "不支持部分完成拣货任务");
                    }
                    break;
                }
            }
            if (reviewQuantity > 0) {
                if (reviewQuantity == param.getReviewQuantity()) {
                    if (param.getCellCode() != null) {
                        throw new ServiceException(CommonCode.SERVICE_ERROR, "当前订单在货位【" + param.getCellCode() + "】上已经完成了此商品的拣货");
                    } else {
                        throw new ServiceException(CommonCode.SERVICE_ERROR, "当前订单已经完成了此商品的拣货");
                    }
                }
                throw new ServiceException(CommonCode.SERVICE_ERROR, "此次拣出的数量过多，请核对数量");
            }
        } catch (Exception e) {
            pickTaskExecuteService.save(execute);
            throw e;
        }
        // 执行拣货任务
        for (Map.Entry<Integer, Double> item : pickTaskId2Qty.entrySet()) {
            Integer id = item.getKey();
            Double qty = item.getValue();
            PickParam pickParam = new PickParam();
            BeanUtils.copyProperties(param, pickParam);
            pickParam.setPickTaskId(id);
            pickParam.setReviewQuantity(qty);
            this.pick(type, pickParam);
        }
    }

    @Override
    public List<PickTaskDto> findListByParam(PickTaskCriteria criteria) {
        List<PickTaskDto> list = pickTaskMapper.findList(criteria);
        return list;
    }

    // 重置拣货任务
    @Override
    public void pickTaskReset(PickTaskResetParam param) {
        Set<Integer> soMasterIds = new HashSet<>();
        Set<Integer> soDetailIds = new HashSet<>();
        for (Integer id : param.getPickTaskIds()) {
            // 删除原有拣货任务
            PickTask pickTask = this.findById(id);
            if (pickTask.getState() != 0) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "拣货任务" + id + "不是待拣货，无法重置");
            }
            this.deleteByIdVersion(pickTask.getPickTaskId(), pickTask.getVersion());
            // 对应库存的分配数调整为未分配的状态
            Integer fromInventoryId = Np.i(pickTask.getFromInventoryId()).o(pickTask.getInventoryId());
            Inventory inventory = inventoryService.findById(fromInventoryId);
            if (inventory == null) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "拣货任务" + id + "对应的库存" + fromInventoryId + "已经不存在");
            }
            Double inventoryAllotQuantity = Np.i(inventory.getAllotQuantity()).o(0D);
            if (inventoryAllotQuantity < pickTask.getPickQuantity()) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "拣货任务" + id + "对应的库存" + inventory.getInventoryId() + "分配数不足，无法拣货");
            }
            inventory.setInventoryId(fromInventoryId);
            inventory.setAllotQuantity(inventoryAllotQuantity - pickTask.getPickQuantity());
            inventory.setVersion(inventory.getVersion());
            inventoryService.update(inventory);
            // 统计需要重新计算状态的SoMaster
            soMasterIds.add(pickTask.getSoMasterId());
            soDetailIds.add(pickTask.getSoDetailId());
        }
        // 重新计算SoDetail的状态
        for (Integer soDetailId : soDetailIds) {
            if (soDetailId != null) {
                SoDetail soDetail = soDetailService.findById(soDetailId);
                if (Np.i(soDetail).x(x -> x.getState()).notIn(0, 1, 2)) {
                    throw new ServiceException(CommonCode.SERVICE_ERROR, "SoDetail单" + soDetailId + "状态无法撤销分配");
                }
                List<PickTask> detailPickTasks = this.findBySoDetailId(soDetailId);
                if (detailPickTasks == null || detailPickTasks.isEmpty()) {
                    soDetail.setState(0);// 没有拣货任务 -> 新建
                    soDetail.setAllottedQuantity(0D);
                } else {
                    Double pickQuantitySum = detailPickTasks.stream().mapToDouble(x -> Np.i(x.getPickQuantity()).o(0D)).sum();
                    if (pickQuantitySum < soDetail.getOrderQuantity()) {
                        soDetail.setState(1);// 拣货任务不够详情单 -> 缺货
                    } else {
                        soDetail.setState(2);// 拣货任务够详情单 -> 分配完成
                    }
                    soDetail.setAllottedQuantity(pickQuantitySum);
                }
                soDetailService.update(soDetail);
            }
        }
        // 重新计算SoMaster的状态
        for (Integer soMasterId : soMasterIds) {
            if (soMasterId != null) {
                SoMaster soMaser = soMasterService.findById(soMasterId);
                if (Np.i(soMaser).x(x -> x.getState()).notIn(0, 1, 2, 3, 4)) {
                    throw new ServiceException(CommonCode.SERVICE_ERROR, "So单" + soMasterId + "状态无法撤销分配");
                }
                List<SoDetail> soDetails = soDetailService.findByMasterId(soMasterId);
                if (soDetails.stream().anyMatch(x -> Np.i(x.getState()).in(0, 1))) {
                    soMaser.setState(3);
                    soMasterService.update(soMaser);
                }
            }
        }
    }

    /**
     * 查询可分配库存
     * 
     * @author luolin
     * @param turnoverTacticCode 周转规则编码
     * @param itemCode           sku
     * @param wareId             仓库id
     * @param organizationId     货主id
     * @return java.util.List<com.deer.wms.inventory.model.Inventory.InventorySelectDto>
     **/
    public List<InventorySelectDto> getAssignableInventory(String turnoverTacticCode, String itemCode, Integer wareId, Integer organizationId) {
        StringBuffer sort = new StringBuffer();
        if (turnoverTacticCode == null) {
            turnoverTacticCode = "sys_turnover";
        }
        List<TurnoverTacticDetail> turnoverTacticDetails = turnoverTacticDetailService.findList(new TurnoverTacticDetailCriteria(turnoverTacticCode));
        if (turnoverTacticDetails.size() > 0) {
            for (int i = 0; i < turnoverTacticDetails.size(); i++) {
                TurnoverTacticDetail turnoverTacticDetail = turnoverTacticDetails.get(i);
                if (turnoverTacticDetail.getBatchTacticDetailCode().equals("productDate")) {
                    sort.append("product_date");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("exDate")) {
                    sort.append("ex_date");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("inDate")) {
                    sort.append("in_date");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("supplierCode")) {
                    sort.append("supplier_code");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("supplierBatch")) {
                    sort.append("supplier_batch");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("qcState")) {
                    sort.append("qc_state");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("costPrice")) {
                    sort.append("cost_price");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("salePrice")) {
                    sort.append("sale_price");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("detailNo")) {
                    sort.append("detail_no");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("memo1")) {
                    sort.append("memo1");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("memo2")) {
                    sort.append("memo2");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("memo3")) {
                    sort.append("memo3");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("batchAttribute1")) {
                    sort.append("batch_attribute_1");
                } else if (turnoverTacticDetail.getBatchTacticDetailCode().equals("batchAttribute1")) {
                    sort.append("batch_attribute_2");
                }
                // 判断升序或者降序
                if (turnoverTacticDetail.getSort().equals(1)) {
                    sort.append(" asc,");
                } else {
                    sort.append(" desc,");
                }
            }
        }
        String sortSql = sort.toString();
        sortSql = sortSql.substring(0, sortSql.lastIndexOf(','));
        InventoryCriteria inventoryCriteria = new InventoryCriteria();
        inventoryCriteria.setItemCode(itemCode);
        inventoryCriteria.setCellUseType(1);

        inventoryCriteria.setParam(1003);
        if (sort.length() > 0) {
            inventoryCriteria.setJointSql(sortSql);
        }
        inventoryCriteria.setWareId(wareId);
        inventoryCriteria.setOrganizationId(organizationId);
        List<InventorySelectDto> inventoryDtos = inventoryService.findCanBeOutOfStock(inventoryCriteria);
        List<PickBatchException> exceptionList = pickBatchExceptionService.getPickBatchExceptionList();
        // 筛选未报波次拣货异常的库存
        for (PickBatchException item : exceptionList) {
            inventoryDtos = inventoryDtos.stream()
                    .filter(x -> !(x.getCellCode().equals(item.getCellCode()) && x.getItemCode().equals(item.getItemCode())))
                    .collect(Collectors.toList());
        }
        return inventoryDtos;
    }

    @Data
    private static class AssignableItem {
        private String billNo;
        private String itemCode;
        private Double qty;
    }

    @Override
    public void createBatchExceptionPickTask() {
        List<PickBatchItem> batchItemList = pickBatchItemService.getExceptionItem();
        List<PickBatchItem> noRepeatBatchItemList = batchItemList.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<PickBatchItem>(Comparator.comparing(o -> o.getQrCode().substring(0, o.getQrCode().indexOf("!"))))),
                ArrayList::new));
        List<AssignableItem> assignableItemList = new ArrayList<>(); // 需要重新分配拣货任务的数据
        // 根据so单号和itemCode重组数据
        for (PickBatchItem item : noRepeatBatchItemList) {
            // Double soDetailExceptionQty = 0.0;
            AssignableItem assignableItem = new AssignableItem();
            QrEntity qrEntity = QrEntity.stringToEntity(QrEntity.QrStringType.SIMPLE_STRING, item.getQrCode());
            assignableItem.setBillNo(qrEntity.getSo());
            assignableItem.setItemCode(qrEntity.getSku());
            assignableItem.setQty(0.0);
            for (PickBatchItem item1 : batchItemList) {
                QrEntity qrEntity1 = QrEntity.stringToEntity(QrEntity.QrStringType.SIMPLE_STRING, item.getQrCode());
                if (qrEntity.getSo().equals(qrEntity1.getSo()) && item1.getItemCode().equals(item.getItemCode())) {
                    assignableItem.setQty(assignableItem.getQty() + 1);
                }
            }
            assignableItemList.add(assignableItem);
        }
        for (AssignableItem ai : assignableItemList) {
            SoMaster soMaster = soMasterService.findByBillNo(ai.getBillNo());
            List<InventorySelectDto> inventoryDtos = getAssignableInventory(null, ai.getItemCode(), soMaster.getWareId(), soMaster.getOrganizationId());
            // FIXME 2023-07-28 库存不足时，使用推荐货位创建拣货任务
            // 库存数
            Double totalCanBeQuantity = 0.0;
            // 异常数
            Double soDetailExceptionQty = ai.getQty();
            for (InventorySelectDto inventorySelectDto : inventoryDtos) {
                totalCanBeQuantity += inventorySelectDto.getQuantity() - inventorySelectDto.getAllotQuantity() - inventorySelectDto.getFreezeQuantity();
            }
            // 库存数小于异常数不分配
            if (totalCanBeQuantity < soDetailExceptionQty) {
                return;
            }
            // 需要修改状态的item
            List<PickBatchItem> updateBatchItemList = batchItemList.stream()
                    .filter(x -> x.getQrCode().indexOf(ai.getBillNo()) != -1)
                    .filter(x -> x.getItemCode().equals(ai.getItemCode()))
                    .collect(Collectors.toList());
            List<SoDetail> soDetailList = soDetailService.findByBillNo(ai.getBillNo()).stream()
                    .filter(x -> x.getItemCode().equals(ai.getItemCode()))
                    .collect(Collectors.toList());
            for (SoDetail soDetail : soDetailList) {
                if (soDetailExceptionQty < 0) {
                    break;
                }
                List<PickTask> pickTaskList = pickTaskService.findBySoDetailId(soDetail.getSoDetailId()).stream()
                        .filter(x -> x.getItemCode().equals(ai.getItemCode()))
                        .filter(x -> x.getPickQuantity() > x.getReviewQuantity())
                        .collect(Collectors.toList());
                if (pickTaskList.isEmpty()) {
                    log.error("createBatchExceptionPickTask() so_detail:" + soDetail.getSoDetailId() + "的sku(" + ai.getItemCode() + ")拣出的数量已足够，无需再拣货");
                    break;
                }
                if (inventoryDtos.size() > 0) {
                    InventoryQuantity: for (InventorySelectDto inventorySelectDto : inventoryDtos) {
                        Long time2 = System.currentTimeMillis();
                        Double canBeQuantity = inventorySelectDto.getQuantity() - inventorySelectDto.getAllotQuantity() - inventorySelectDto.getFreezeQuantity();
                        PickTask pickTask = new PickTask();
                        pickTask.setSoDetailId(soDetail.getSoDetailId());
                        pickTask.setSoMasterId(soMaster.getSoMasterId());
                        pickTask.setWareId(soMaster.getWareId());
                        pickTask.setWareName(soMaster.getWareName());
                        pickTask.setOrganizationId(soMaster.getOrganizationId());
                        pickTask.setOrganizationName(soMaster.getOrganizationName());
                        pickTask.setAreaCode(inventorySelectDto.getAreaCode());
                        pickTask.setAreaName(inventorySelectDto.getAreaName());
                        pickTask.setShelfCode(inventorySelectDto.getShelfCode());
                        pickTask.setShelfName(inventorySelectDto.getShelfName());
                        pickTask.setCellCode(inventorySelectDto.getCellCode());
                        pickTask.setCellName(inventorySelectDto.getCellName());
                        pickTask.setItemCode(inventorySelectDto.getItemCode());
                        pickTask.setItemName(inventorySelectDto.getItemName());
                        pickTask.setBatchId(inventorySelectDto.getBatchId());
                        pickTask.setBatchName(inventorySelectDto.getBatchName());
                        pickTask.setBoxCode(inventorySelectDto.getBoxCode());
                        pickTask.setLpn(inventorySelectDto.getLpn());
                        pickTask.setPackDetailId(soDetail.getPackDetailId());
                        pickTask.setPackDescribe(soDetail.getPackDescribe());
                        pickTask.setPickQuantity(0.0);
                        pickTask.setState(0);
                        pickTask.setInventoryId(inventorySelectDto.getInventoryId());
                        pickTask.setSource(2);
                        pickTask.setImgUrl(inventorySelectDto.getImgUrl());
                        if (canBeQuantity >= soDetailExceptionQty) {
                            pickTask.setPickQuantity(soDetailExceptionQty);
                            inventorySelectDto.setAllotQuantity(pickTask.getPickQuantity() + inventorySelectDto.getAllotQuantity());
                            // soDetail.setAllottedQuantity(soDetail.getAllottedQuantity() + pickTask.getPickQuantity());
                            inventoryService.addAllotQuantity(inventorySelectDto.getInventoryId(), pickTask.getPickQuantity());
                            if (pickTask.getPickQuantity() < 0) {
                                log.error("出现负数： createBatchExceptionPickTask() pickTask.getPickQuantity()=" + pickTask.getPickQuantity());
                            }
                            if ((pickTask.getPickQuantity() + inventorySelectDto.getAllotQuantity()) < 0) {
                                log.error(inventorySelectDto.getInventoryId()
                                        + "invID. 出现负数： createBatchExceptionPickTask() 2数相加小于零： 【pickTask.getPickQuantity() + inventorySelectDto.getAllotQuantity()】="
                                        + (pickTask.getPickQuantity() + inventorySelectDto.getAllotQuantity()));
                            }
                            if ((inventorySelectDto.getAllotQuantity() - pickTask.getPickQuantity()) < 0) {
                                log.error(inventorySelectDto.getInventoryId() + "invID. 发现出现负数： createBatchExceptionPickTask() 【getAllotQuantity-getPickQuantity】 =getAllotQuantity "
                                        + inventorySelectDto.getAllotQuantity() + " - getPickQuantity " + pickTask.getPickQuantity());
                            }
                            save(pickTask);
                            for (PickTask pt : pickTaskList) {
                                Double taskQty = pt.getPickQuantity() - pt.getReviewQuantity();// 本任务未拣数量
                                if (pt.getReviewQuantity() > 0) {
                                    if ((soDetailExceptionQty - taskQty) < 0) {
                                        pt.setPickQuantity(pt.getPickQuantity() - soDetailExceptionQty);
                                        pt.setState(1);
                                    } else {
                                        pt.setPickQuantity(pt.getReviewQuantity());
                                        pt.setState(2);
                                    }
                                } else {
                                    if ((soDetailExceptionQty - taskQty) < 0) {
                                        pt.setPickQuantity(pt.getPickQuantity() - soDetailExceptionQty);
                                        pt.setState(0);
                                    } else {
                                        pt.setPickQuantity(0D);
                                        pt.setState(3);
                                    }
                                }
                                soDetailExceptionQty -= taskQty;
                                pickTaskService.update(pt);
                            }
                            for (PickBatchItem item : updateBatchItemList) {
                                item.setStatus(-1);
                            }
                            // 修改item状态为-1
                            pickBatchItemService.batchUpdateStatusById(updateBatchItemList);
                            // 新增pick_batch_item
                            for (PickBatchItem item : updateBatchItemList) {
                                item.setCellCode(pickTask.getCellCode());
                                item.setCreateTime(DateUtils.now());
                                item.setCreateUserId(206);
                                item.setCreateUserName("xhctest");
                                item.setStatus(0);
                            }
                            pickBatchItemService.insertList(updateBatchItemList);
                            this.savePickTaskDoLog(pickTask, time2);
                            break InventoryQuantity;
                        } else {
                            double quantity = canBeQuantity;
                            if (quantity > 0) {
                                pickTask.setPickQuantity(quantity);
                                inventorySelectDto.setAllotQuantity(inventorySelectDto.getAllotQuantity() + quantity);
                                // soDetail.setAllottedQuantity(soDetail.getAllottedQuantity() + quantity);
                                inventoryService.addAllotQuantity(inventorySelectDto.getInventoryId(), quantity);
                                if (pickTask.getPickQuantity() < 0) {
                                    log.error("else出现负数： createBatchExceptionPickTask() pickTask.getPickQuantity()=" + pickTask.getPickQuantity());
                                }
                                if ((pickTask.getPickQuantity() + inventorySelectDto.getAllotQuantity()) < 0) {
                                    log.error(inventorySelectDto.getInventoryId()
                                            + "invID. else出现负数： createBatchExceptionPickTask() 2数相加小于零： 【pickTask.getPickQuantity() + inventorySelectDto.getAllotQuantity()】="
                                            + (pickTask.getPickQuantity() + inventorySelectDto.getAllotQuantity()));
                                }
                                if ((inventorySelectDto.getAllotQuantity() - pickTask.getPickQuantity()) < 0) {
                                    log.error(inventorySelectDto.getInventoryId() + "invID. 发现出现负数： createBatchExceptionPickTask() 【getAllotQuantity-quantity】 =getAllotQuantity "
                                            + inventorySelectDto.getAllotQuantity()
                                            + " - quantity " + pickTask.getPickQuantity());
                                }
                                save(pickTask);
                                for (PickTask pt : pickTaskList) {
                                    if (quantity < 0) {
                                        break;
                                    }
                                    Double taskQty = pt.getPickQuantity() - pt.getReviewQuantity();// 本任务未拣数量
                                    if (pt.getReviewQuantity() > 0) {
                                        if ((quantity - taskQty) < 0) {
                                            pt.setPickQuantity(pt.getPickQuantity() - quantity);
                                            pt.setState(1);
                                        } else {
                                            pt.setPickQuantity(pt.getReviewQuantity());
                                            pt.setState(2);
                                        }
                                    } else {
                                        if ((quantity - taskQty) < 0) {
                                            pt.setPickQuantity(pt.getPickQuantity() - quantity);
                                            pt.setState(0);
                                        } else {
                                            pt.setPickQuantity(0D);
                                            pt.setState(3);
                                        }
                                    }
                                    soDetailExceptionQty -= quantity;
                                    pickTaskService.update(pt);
                                }
                                List<PickBatchItem> list = updateBatchItemList.subList(0, (int) quantity);
                                for (PickBatchItem item : list) {
                                    item.setStatus(-1);
                                }
                                // 修改item状态为-1
                                pickBatchItemService.batchUpdateStatusById(list);
                                // 新增pick_batch_item
                                for (PickBatchItem item : list) {
                                    item.setCellCode(pickTask.getCellCode());
                                    item.setCreateTime(DateUtils.now());
                                    item.setCreateUserId(206);
                                    item.setCreateUserName("xhctest");
                                    item.setStatus(0);
                                }
                                pickBatchItemService.insertList(list);
                                // 未重新分配拣货任务的pick_batch_item
                                updateBatchItemList = updateBatchItemList.subList((int) (quantity - 1), updateBatchItemList.size());
                                this.savePickTaskDoLog(pickTask, time2);
                            }
                        }
                        if (soDetailExceptionQty < 0) {
                            break;
                        }
                    }
                }
            }
        }
    }
}
