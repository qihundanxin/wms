package com.deer.wms.ware.task.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.deer.wms.ware.task.model.Send.SendDetail;
import com.deer.wms.ware.task.service.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.base.system.model.Item.ItemInfo;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.Inventory.InventoryCriteria;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.ware.task.dao.SoDetailMapper;
import com.deer.wms.ware.task.model.PickTaskReturn;
import com.deer.wms.ware.task.model.SoDetailStockoutInfo;
import com.deer.wms.ware.task.model.Return.SoLogItemDto;
import com.deer.wms.ware.task.model.SO.SoDayList;
import com.deer.wms.ware.task.model.SO.SoDetail;
import com.deer.wms.ware.task.model.SO.SoDetailCriteria;
import com.deer.wms.ware.task.model.SO.SoDetailDto;
import com.deer.wms.ware.task.model.SO.SoDetailQh;
import com.deer.wms.ware.task.model.SO.SoMaster;
import com.deer.wms.ware.task.model.Wave.WaveDetail;
import com.deer.wms.ware.task.model.Wave.WaveMaster;
import com.deer.wms.ware.task.model.pickTask.PickTask;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by guo on 2020/02/02.
 */
@Service
@Transactional
@Slf4j
public class SoDetailServiceImpl extends AbstractService<SoDetail, Integer> implements SoDetailService {

    @Autowired
    private SoDetailMapper soDetailMapper;

    @Autowired
    private ItemInfoService itemInfoService;

    @Autowired
    private SendDetailService sendDetailService;


    @Override
    public void updateState(SoDetail soDetail) {
        int rowCount = soDetailMapper.updateState(soDetail);
        if (rowCount == 0) {
            throw new ServiceException(CommonCode.SO_VERSION, "update soDetail state更新了0条数据：" + soDetail.getSoDetailId() + "," + soDetail.getVersion());
        }

    }

    @Override
    public void updateState2(@Param("id") Integer id, @Param("newState") Integer newState, @Param("oldState") Integer oldState) {
        int rowCount = soDetailMapper.updateState2(id, newState, oldState);
        if (rowCount == 0) {
            throw new ServiceException(CommonCode.SO_VERSION, "update soDetail state2更新了0条数据：" + id + "," + oldState + "->" + newState);
        }
    }

    @Override
    public void cancel(String billNo) {
        soDetailMapper.cancel(billNo);
    }

    @Override
    public List<SoDetailDto> findList(SoDetailCriteria criteria) {
        return soDetailMapper.findList(criteria);
    }

    @Override
    public List<SoDetailDto> findBaoKuanByItemCode(String itemCode, Integer wareId, Integer orgId) {
        return soDetailMapper.findBaoKuanByItemCode(itemCode, wareId, orgId);
    }

    @Override
    public List<SoDetail> findByBillNo(String billNo) {
        return soDetailMapper.findByBillNo(billNo);
    }

    @Override
    public List<SoDetail> findSelfByBillNo(String billNo) {
        return soDetailMapper.findSelfByBillNo(billNo);
    }

    @Override
    public List<SoDetail> pickupfindByBillNo(String billNo) {
        return soDetailMapper.pickupfindByBillNo(billNo);
    }

    @Override
    public List<SoDetailDto> findListAndMaster(SoDetailCriteria criteria) {
        // 在Time<1668441600时，Detail状态数据有问题，采用4ChaoticState。之后的查询采用正常方式。
        Long startCreateTime = Np.i(criteria).x(x -> x.getStartCreateTime())
                .x(x -> DateUtils.strToDate(criteria.getStartCreateTime(), DateUtils.DEFAULT_DATE_FORMAT))
                .x(x -> x.getTime()).o();
        if (startCreateTime == null || startCreateTime < 1668441600) {
            return soDetailMapper.findListAndMaster4ChaoticState(criteria);
        }

        // 采取正常方式进行查询
        return soDetailMapper.findListAndMaster(criteria);
    }

    @Override
    public List<SoDetailQh> findQhQuantity() {
        return soDetailMapper.findQhQuantity();
    }

    @Override
    public List<SoDetailDto> findUndistributedSoDetail(Integer wareId) {
        return soDetailMapper.findUndistributedSoDetail(wareId);
    }

    @Override
    public List<SoDetailDto> findUndistributedSoDetail2(Integer wareId) {
        return soDetailMapper.findUndistributedSoDetail2(wareId);
    }

    @Override
    public List<SoDetailDto> findUndistributedSoDetail3(Integer wareId) {
        return soDetailMapper.findUndistributedSoDetail3(wareId);
    }

    @Override
    public List<SoDetailDto> findUndistributedSoDetail4(Integer wareId) {
        return soDetailMapper.findUndistributedSoDetail4(wareId);
    }

    @Override
    public List<SoDetailDto> findUndistributedSoDetail5(Integer wareId) {
        return soDetailMapper.findUndistributedSoDetail5(wareId);
    }

    @Autowired
    private SoMasterService soMasterService;
    @Autowired
    private WaveMasterService waveMasterService;
    @Autowired
    private PickTaskService pickTaskService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private PickTaskReturnService pickTaskReturnService;

    @Override
    public void reduceSkuQuantity(SoDetail soDetail) {

        SoMaster soMaster = soMasterService.findBy("billNo", soDetail.getBillNo());
        WaveDetail waveDetail = null;
        WaveMaster waveMaster = null;
        if (waveDetail != null) {
            waveMaster = waveMasterService.findById(waveDetail.getWaveMasterId());
        }
        List<PickTask> exPickTasks = pickTaskService.findBySoMasterId(soMaster.getSoMasterId());
        Double quantity1 = soDetail.getAllottedQuantity() - soDetail.getOrderQuantity();
        if (quantity1 > 0) {
            for (PickTask exPickTask : exPickTasks) {
                if (!exPickTask.getItemCode().equals(soDetail.getItemCode())) {
                    continue;
                }
                if (quantity1 == 0.0) {
                    break;// 提高效率 continue
                }
                // 还没拣货,而且不是父任务
                if (exPickTask.getState() == 0 && exPickTask.getIsParent() == 0) {
                    Inventory inventory = inventoryService.findById(exPickTask.getInventoryId());
                    // BERWIN modify logic
                    if (exPickTask.getPickQuantity() <= quantity1) {
                        exPickTask.setState(-1);
                        exPickTask.setWaveMasterId(null);
                        quantity1 -= exPickTask.getPickQuantity();
                        if (inventory != null) {
                            Double oldAllotQuantity = inventory.getAllotQuantity();
                            Double pickQuantity = exPickTask.getPickQuantity();
                            Double newAllotQuantity = oldAllotQuantity - pickQuantity;
                            inventory.setAllotQuantity(newAllotQuantity);
                            inventoryService.update(inventory);
                            // 2022 berwin
                            if (newAllotQuantity < 0) {
                                log.error("出现负数： SoDetailServiceImpl.reduceSkuQuantity() inventory.setAllotQuantity()= oldAllotQuantity " + oldAllotQuantity + " - pickQuantity " + pickQuantity);
                            }
                        }
                        exPickTask.setPickQuantity(0.0);
                        // quantity1=0.0;
                    } else { // exPickTask.getPickQuantity() > quantity1
                        if (inventory != null) {
                            Double oldAllotQuantity = inventory.getAllotQuantity();
                            // Double pickQuantity = exPickTask.getPickQuantity();
                            Double newAllotQuantity = oldAllotQuantity - quantity1;
                            inventory.setAllotQuantity(newAllotQuantity);
                            inventoryService.update(inventory);
                            // 2022 berwin
                            if (newAllotQuantity < 0) {
                                log.error("出现负数： SoDetailServiceImpl.reduceSkuQuantity() inventory.setAllotQuantity()= oldAllotQuantity " + oldAllotQuantity + " - quantity1 " + quantity1);
                            }
                        }
                        exPickTask.setPickQuantity(exPickTask.getPickQuantity() - quantity1);
                        quantity1 = 0.0;
                    }
                    pickTaskService.update(exPickTask);
                }
                // 被合并 而且还没拣---目前没这个情况 所以暂时不看这个代码
                else if (exPickTask.getState() == 4) {

                    Inventory inventory = inventoryService.findById(exPickTask.getInventoryId());
                    PickTask parent = pickTaskService.getTaskById(exPickTask.getParentTaskId());
                    if (exPickTask.getPickQuantity() <= quantity1) {
                        exPickTask.setState(-1);
                        exPickTask.setWaveMasterId(null);
                        quantity1 -= exPickTask.getPickQuantity();
                        inventory.setAllotQuantity(inventory.getAllotQuantity() - exPickTask.getPickQuantity());
                        parent.setPickQuantity(parent.getPickQuantity() - exPickTask.getPickQuantity());
                    } else {
                        exPickTask.setPickQuantity(exPickTask.getPickQuantity() - quantity1);
                        inventory.setAllotQuantity(inventory.getAllotQuantity() - quantity1);
                        parent.setPickQuantity(parent.getPickQuantity() - quantity1);
                        quantity1 = 0.0;
                    }
                    pickTaskService.update(exPickTask);
                    inventoryService.update(inventory);
                }
                // 已经拣过了
                else if (exPickTask.getState() == 2) {
                    Double returnQuantity = 0.0;
                    if (exPickTask.getPickQuantity() <= quantity1) {
                        exPickTask.setState(-1);
                        exPickTask.setWaveMasterId(null);
                        quantity1 -= exPickTask.getPickQuantity();
                        returnQuantity = exPickTask.getPickQuantity();

                    } else {
                        exPickTask.setPickQuantity(exPickTask.getPickQuantity() - quantity1);
                        returnQuantity = Double.valueOf(quantity1.toString());
                        quantity1 = 0.0;

                    }
                    pickTaskService.update(exPickTask);
                    PickTaskReturn pickTaskReturn = new PickTaskReturn();
                    pickTaskReturn.setFromCellCode(exPickTask.getToCellCode());
                    pickTaskReturn.setWareId(exPickTask.getWareId());
                    pickTaskReturn.setOrgId(exPickTask.getOrganizationId());
                    pickTaskReturn.setItemCode(exPickTask.getItemCode());
                    pickTaskReturn.setItemName(exPickTask.getItemName());
                    pickTaskReturn.setImgUrl(exPickTask.getImgUrl());
                    pickTaskReturn.setFromInventoryId(exPickTask.getInventoryId());
                    pickTaskReturn.setToCellCode(exPickTask.getCellCode());
                    // pickTaskReturn.setFromCellCode(exPickTask.getCellCode());
                    pickTaskReturn.setQuantity(returnQuantity);
                    pickTaskReturn.setState(0);
                    pickTaskReturn.setType(2);
                    if (false) {

                    } else {
                        pickTaskReturn.setCreateUserName("系统取消");
                    }
                    pickTaskReturn.setCreateTime(DateUtils.getNowDateTimeString());
                    pickTaskReturn.setWaveMasterId(exPickTask.getWaveMasterId());
                    if (waveDetail != null && waveMaster != null) {
                        pickTaskReturn.setWaveMasterId(waveDetail.getWaveMasterId());
                        pickTaskReturn.setBoxCode(waveMaster.getBoxCode());
                    }
                    if (exPickTask.getBoxCode() != null) {
                        pickTaskReturn.setBoxCode(exPickTask.getBoxCode());
                    }
                    pickTaskReturnService.save(pickTaskReturn);

                    soDetail.setPickQuantity(soDetail.getPickQuantity() - returnQuantity);
                    soDetail.setCheckQuantity(soDetail.getCheckQuantity() - returnQuantity);

                }

//                //已经发运了
//                else if (exPickTask.getState() == 5) {
//                    exPickTask.setState(-1);
//                    exPickTask.setParentTaskId(null);
//                    pickTaskService.update(exPickTask);
//
//                    PickTaskReturn pickTaskReturn = new PickTaskReturn();
//                    pickTaskReturn.setFromCellCode(exPickTask.getToCellCode());
//                    pickTaskReturn.setWareId(exPickTask.getWareId());
//                    pickTaskReturn.setOrgId(exPickTask.getOrganizationId());
//                    pickTaskReturn.setItemCode(exPickTask.getItemCode());
//                    pickTaskReturn.setItemName(exPickTask.getItemName());
//                    pickTaskReturn.setImgUrl(exPickTask.getImgUrl());
//                    //pickTaskReturn.setFromInventoryId(exPickTask.getInventoryId());
//                    pickTaskReturn.setToCellCode(exPickTask.getCellCode());
//                    pickTaskReturn.setQuantity(exPickTask.getPickQuantity());
//                    pickTaskReturn.setState(0);
//                    pickTaskReturn.setType(3);
//                    pickTaskReturn.setBillNo(soMaster.getBillNo());
//                    pickTaskReturn.setShipBillNo(soMaster.getShipBillCode());
//                    if(false){
//
//                    }else {
//                        pickTaskReturn.setCreateUserName("系统取消");
//                    }
//                    pickTaskReturn.setCreateTime(DateUtils.getNowDateTimeString());
//                    if (waveDetail != null && waveMaster != null) {
//                        pickTaskReturn.setWaveMasterId(waveDetail.getWaveMasterId());
//                        pickTaskReturn.setBoxCode(waveMaster.getBoxCode());
//                    }
//
//                    pickTaskReturnService.save(pickTaskReturn);
//                }

            }

            soDetail.setAllottedQuantity(soDetail.getOrderQuantity());

            if (soDetail.getOrderQuantity() < 0.0001) {
                soDetail.setState(-1);
            }
            update(soDetail);

            List<SoDetail> list = findByBillNo(soDetail.getBillNo());
            Boolean isCancel = true;
            // 判断是否整单取消
            for (SoDetail detail : list) {
                if (detail.getOrderQuantity() > 0.001) {
                    isCancel = false;
                    break;
                }
            }
            if (isCancel) {
                // 2022-7-21 根据最新产品设计 将状态改为 -2： 关闭
                soMaster.setState(-2);
                soMasterService.updateState(soMaster);
            } else {
                // berwin 判断主单真实状态
                Boolean isPorcess = false;
                Boolean isPick = true;
                for (SoDetail detail : list) {
                    if (detail.getState() != -1 && detail.getPickQuantity() < detail.getOrderQuantity()) {
                        isPick = false;
                        break;
                    }
                }
                if (isPick) {
                    soMaster.setState(6); // 没有对应的状态
                    isPorcess = true;
                }
                // berwin
                Boolean isPickAll = true;// 详情中是否全拣货了
                if (!isPorcess) {
                    Boolean isAllot = true;// 分配完成了
                    for (SoDetail detail : list) {
                        // Detail -1：取消 0：新建 1：部分分配 2：全部分配 3：部分拣货 4：全部拣货 5：复核完成 6：完全发运
                        if (null == detail.getOrderQuantity() || detail.getOrderQuantity() == 0) {
                            continue;
                        }
                        if (detail.getState() != 4 && detail.getState() != 5 && detail.getState() != 6) {
                            isPickAll = false;
                        }
                        if (detail.getState() != 2 && detail.getState() != 3 && detail.getState() != 4 && detail.getState() != 5 && detail.getState() != 6) {
                            isAllot = false;
                        }
                    }
                    if (isPickAll) {
                        soMaster.setState(7);
                    } else if (isAllot) {
                        soMaster.setState(4);
                    }
                }
                soMasterService.updateState(soMaster);
                // 如果主单已复核，明细单也为已复核
                if (isPickAll) {
                    for (SoDetail detail : list) {
                        this.updateState2(detail.getSoDetailId(), 5, detail.getState());
                    }
                }
            }
        }
    }

    @Override
    // berwin process q and qa = 0
    public void reduceSkuQuantityFor0And0(SoDetail soDetail) {

        SoMaster soMaster = soMasterService.findBy("billNo", soDetail.getBillNo());

        if (soDetail.getAllottedQuantity() == 0 && soDetail.getOrderQuantity() == 0) {

            List<SoDetail> list = findByBillNo(soDetail.getBillNo());
            Boolean isCancel = true;
            // 判断是否整单取消
            for (SoDetail detail : list) {
                if (detail.getOrderQuantity() > 0.001) {
                    isCancel = false;
                    break;
                }
            }
            // 调用该方法前都进行了修改明细为取消状态
            if (isCancel) {
                soMaster.setState(-1);
                soMasterService.updateState(soMaster);
            } else {
                // berwin 判断主单真实状态
                Boolean isPorcess = false;
                Boolean isPick = true;
                for (SoDetail detail : list) {
                    if (detail.getState() != -1 && detail.getPickQuantity() < detail.getOrderQuantity()) {
                        isPick = false;
                        break;
                    }
                }
                if (isPick) {
                    soMaster.setState(6);
                    isPorcess = true;
                }
                Boolean isCkeck = true;
                for (SoDetail detail : list) {
                    if ((detail.getState() != -1 && detail.getCheckQuantity() < detail.getOrderQuantity()) || detail.getCheckQuantity() == 0) {
                        isCkeck = false;
                        break;
                    }
                }
                if (isCkeck) {
                    soMaster.setState(7);
                    isPorcess = true;
                }
                // berwin
                Boolean isPickAll = true;
                if (!isPorcess) {
                    Boolean isAllot = true;
                    for (SoDetail detail : list) {
                        if (null == detail.getOrderQuantity() || detail.getOrderQuantity() == 0) {
                            continue;
                        }
                        if (detail.getState() != 4 && detail.getState() != 5 && detail.getState() != 6) {
                            isPickAll = false;
                        }
                        if (detail.getState() != 2 && detail.getState() != 3 && detail.getState() != 4 && detail.getState() != 5 && detail.getState() != 6) {
                            isAllot = false;
                        }
                    }
                    if (isPickAll) {
                        soMaster.setState(7);
                    } else if (isAllot) {
                        soMaster.setState(4);
                    }
                }
                soMasterService.updateState(soMaster);
                // 如果主单已复核，明细单也为已复核
                if (isCkeck || isPickAll) {
                    for (SoDetail detail : list) {
                        this.updateState2(detail.getSoDetailId(), 5, detail.getState());
                    }
                }
            }
        }
    }

    @Override
    public List<SoDetail> findByMasterId(Integer soId) {
        return soDetailMapper.findByMasterId(soId);
    }

    @Override
    public List<SoDetail> findPickUPOrderByMasterId(Integer soId) {
        return soDetailMapper.findPickUPOrderByMasterId(soId);
    }

    @Override
    public List<SoDetailDto> getSeedingByItemCode(String itemCode, Integer wareId, Integer waveId) {
        return soDetailMapper.getSeedingByItemCode(itemCode, wareId, waveId);
    }

    @Override
    public List<SoDayList> findDayList(SoDetailCriteria criteria) {
        return soDetailMapper.findDayList(criteria);
    }

    @Override
    public List<SoLogItemDto> findLogItemsByBillNos(List<String> billNos) {
        return soDetailMapper.findLogItemsByBillNos(billNos);
    }

    @Override
    public List<SoDetailDto> findDetailsByWaveId(Integer wareId, Integer waveId) {
        return soDetailMapper.findDetailsByWaveId(wareId, waveId);
    }

    @Override
    public List<SoDetailDto> getSeedingYdYpByWaveMasterId(Integer wareId, Integer waveId) {
        return soDetailMapper.getSeedingYdYpByWaveMasterId(wareId, waveId);
    }

    public int update(SoDetail soDetail) {
        int rowCount = soDetailMapper.update(soDetail);
        if (rowCount == 0) {
            throw new ServiceException(CommonCode.SO_VERSION, "update soDetail更新了0条数据：" + soDetail.getSoDetailId() + "," + soDetail.getVersion());
        }
        return rowCount;
    }

    @Override
    public List<SoDetailDto> findForUpdatePickTask(Integer wareId, LocalDateTime dateTime) {
        return soDetailMapper.findForUpdatePickTask(wareId, dateTime);
    }

    @Override
    public void cancelSoDetail(SoDetailDto soDetailDto) {
        reduceSkuQuantityFor0And0(soDetailDto);
        if (soDetailDto.getState() >= 0) {
            soDetailDto.setState(-1);
            this.updateState(soDetailDto);
        }
    }

    @Override
    public List<SoDetailStockoutInfo> getStockoutInfo(String billNo) {
        List<SoDetailStockoutInfo> result = new ArrayList<>();
        SoMaster soMaster = soMasterService.findByBillNo(billNo);
        if (soMaster == null) {
            throw new ServiceException(CommonCode.SO_VERSION, "单号不存在！");
        }
        if (Np.i(soMaster.getState()).notEq(3)) {// 部分分配（缺货）
            return result;
        }
        List<SoDetail> detailList = soDetailMapper.findSelfByBillNo(billNo);
        Integer wareId = soMaster.getWareId();
        for (SoDetail soDetail : detailList) {
            SoDetailStockoutInfo resultItem = new SoDetailStockoutInfo();
            BeanUtils.copyBeanProp(resultItem, soDetail);
            String itemCode = soDetail.getItemCode();
            // img
            ItemInfo itemInfo = itemInfoService.findByItemCode(itemCode);
            resultItem.setItemInfoImg(Np.i(itemInfo).x(x -> x.getImgUrl()).o());
            // inv info
            InventoryCriteria ic = new InventoryCriteria();
            ic.setItemCode(itemCode);
            ic.setWareId(wareId);
            List<Inventory> invList = inventoryService.findInventoryList(ic);
            List<Inventory> type1List = invList.stream().filter(inv -> Np.i(inv.getCellUseType()).eq(1)).collect(Collectors.toList());// 拣货位库存
            List<Inventory> type2List = invList.stream().filter(inv -> Np.i(inv.getCellUseType()).eq(2)).collect(Collectors.toList());// 仓储位库存
            resultItem.setType1List(type1List);
            resultItem.setType2List(type2List);
            result.add(resultItem);
        }
        return result;
    }

    @Override
    public SoDetailDto findByBillNoAndItemCode(String billNo, String itemCode) {
        return soDetailMapper.findByBillNoAndItemCode(billNo,itemCode);
    }

    /**
     * 查询出库单详情及发运情况
     * @param criteria
     * @return
     */
    @Override
    public List<SoDetailDto> findListAndSend(SoDetailCriteria criteria) {
       List<SoDetailDto>  result = this.findList(criteria);
       for(SoDetailDto detail:result){
           SendDetail send = sendDetailService.findByBillNoAndSku(detail.getBillNo(),detail.getItemCode());
           if(send!=null){
               detail.setSendQuantity(send.getSendQuantity());
               detail.setSendTime(send.getSendTime());
               detail.setSendUserName(send.getSendUserName());
           }
       }
       return result;

    }


}
