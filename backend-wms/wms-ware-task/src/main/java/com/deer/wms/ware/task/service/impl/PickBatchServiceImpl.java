package com.deer.wms.ware.task.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.base.system.service.ItemInfoService.StorageConditions;
import com.deer.wms.project.root.constant.LockName;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.sync.annotation.Synchronized;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.ware.task.dao.PickBatchMapper;
import com.deer.wms.ware.task.model.SO.SoMaster;
import com.deer.wms.ware.task.model.pickTask.MyBatchKanbanRespVO;
import com.deer.wms.ware.task.model.pickTask.PickBatch;
import com.deer.wms.ware.task.model.pickTask.PickBatchDto;
import com.deer.wms.ware.task.model.pickTask.PickBatchGroup;
import com.deer.wms.ware.task.model.pickTask.PickBatchItem;
import com.deer.wms.ware.task.model.pickTask.PickBatchParam;
import com.deer.wms.ware.task.model.pickTask.PickBatchSearchParam;
import com.deer.wms.ware.task.model.pickTask.PickParam;
import com.deer.wms.ware.task.model.pickTask.PickTask;
import com.deer.wms.ware.task.model.pickTask.PickTaskExecute;
import com.deer.wms.ware.task.service.PickBatchDetailService;
import com.deer.wms.ware.task.service.PickBatchGroupService;
import com.deer.wms.ware.task.service.PickBatchItemService;
import com.deer.wms.ware.task.service.PickBatchService;
import com.deer.wms.ware.task.service.PickTaskExecuteService;
import com.deer.wms.ware.task.service.PickTaskService;
import com.deer.wms.ware.task.service.SoMasterService;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

/**
 * @author lixuehe
 * @date 2023/6/1
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PickBatchServiceImpl implements PickBatchService {

    private final PickBatchMapper mapper;
    @Autowired
    private PickBatchItemService pickBatchItemService;
    @Autowired
    private PickTaskService pickTaskService;
    @Autowired
    private SoMasterService soMasterService;
    @Autowired
    private PickTaskExecuteService pickTaskExecuteService;
    @Autowired
    private PickBatchDetailService pickBatchDetailService;
    @Autowired
    private PickBatchGroupService pickBatchGroupService;

    private final ItemInfoService itemInfoService;

    @Override
    public void updateStatusByGroup(String pickBatchGroupNo, Integer status) {
        mapper.updateStatusByGroup(pickBatchGroupNo, status);
    }

    @Override
    public PickBatch save(PickBatch pickBatch) {
        pickBatch.setId(null);
        pickBatch.setCreateTime(Np.i(pickBatch.getCreateTime()).o(new Date()));
        pickBatch.setStatus(Np.i(pickBatch.getStatus()).o(0));
        if (StrUtil.isBlank(pickBatch.getPickBatchNo())
                || StrUtil.isBlank(pickBatch.getPickBatchGroupNo())) {
            throw new ServiceException(CommonCode.SYSTEM_ERROR, "PickBatch缺少必要的字段BatchNo、GroupNo");
        }
        mapper.insert(pickBatch);
        return pickBatch;
    }

    @Override
    public void updateStatusByNo(String pickBatchNo, Integer status) {
        PickBatch pickBatch = this.getByNo(pickBatchNo);
        if (pickBatch == null) {
            return;
        }
        pickBatch.setStatus(status);
        pickBatch.setPickTime(new Date());
        mapper.updateByPrimaryKeySelective(pickBatch);
    }

    @Override
    public PickBatch getByNo(String pickBatchNo) {
        return mapper.getByNo(pickBatchNo);
    }

    @Override
    public PickBatch getById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Synchronized(lockName = LockName.PICK_AFFIRM_BATCH, value = "#param.pickBatchNo")
    @Transactional
    @Override
    public void pickBatch(PickBatchParam param){
        // 拣货任务执行记录PickTaskExecute
        PickTaskExecute execute = new PickTaskExecute();
        execute.setHasSuccess(false);
        execute.setExecuteTime(new Date());
        execute.setReviewQuantity(param.getReviewQuantity());
        execute.setMemo(Np.i(param.getMemo()).ifBlank(null).o());
        execute.setExecuteUserId(param.getUserId());
        execute.setExecuteUserName(param.getUserName());
        execute.setQrCode(Np.i(param.getQrCode()).ifBlank(null).o());
        execute.setToCellCode(Np.i(param.getToCellCode()).ifBlank(null).o());
        execute.setFromCellCode(param.getCellCode());
        execute.setBoxCode(param.getBoxCode());
        execute.setItemCode(param.getItemCode());
        PickBatchItem pickBatchItem = pickBatchItemService.getByQrCode(param.getQrCode());
        List<PickTask> pickTaskList;
        try {
            if (pickBatchItem == null){
                throw new ServiceException(CommonCode.SERVICE_ERROR, "无效的二维码");
            }
            if (!param.getPickBatchNo().equals(pickBatchItem.getPickBatchNo())){
                throw new ServiceException(CommonCode.SERVICE_ERROR, "波次和二维码不匹配");
            }
            if (!param.getCellCode().equals(pickBatchItem.getCellCode())){
                throw new ServiceException(CommonCode.SERVICE_ERROR, "货位和二维码不匹配");
            }
            if (pickBatchItem.getStatus() == 1 && !param.getWholesale()) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "此二维码已经扫描过");
            }
            SoMaster so = soMasterService.findByBillNo(param.getSoBillNo());
            execute.setWareId(Np.i(so).x(x -> x.getWareId()).o());
            execute.setWareName(Np.i(so).x(x -> x.getWareName()).o());
            if (Np.i(so).x(x -> x.getState()).notIn(3, 4, 5)) {// 3-部分分配 4-分配完成 5-部分拣货
                throw new ServiceException(CommonCode.SERVICE_ERROR, "该订单状态无需拣货，请核对订单状态与商品数量");
            }
            pickTaskList = pickTaskService.findBySoMasterId(so.getSoMasterId()).stream()
                    .filter(x -> x.getItemCode().equals(param.getItemCode()))
                    .filter(x -> x.getPickQuantity() > x.getReviewQuantity())
                    .collect(Collectors.toList());// 总拣货任务
            if (pickTaskList.isEmpty()) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "此SKU拣出的数量已足够，无需再拣货");
            }
            if (param.getWholesale()) {
                Double reviewQuantity = param.getReviewQuantity();// 本次想要拣出数量（剩余分配）
                for (PickTask pickTask : pickTaskList) {
                    Double taskQty = pickTask.getPickQuantity() - pickTask.getReviewQuantity();// 本任务需要拣出数量
                    reviewQuantity -= taskQty;
                }
                if (reviewQuantity > 0) {
                    throw new ServiceException(CommonCode.SERVICE_ERROR, "此次拣出的数量过多，请核对数量");
                }
            }
        } catch (Exception e){
            pickTaskExecuteService.save(execute);
            throw e;
        }
        PickParam pickParam = new PickParam();
        BeanUtils.copyProperties(param, pickParam);
        pickParam.setPickTaskId(pickTaskList.get(0).getPickTaskId());
        pickParam.setReviewQuantity(param.getReviewQuantity());
        if (param.getWholesale()){
            //修改pick_batch_item表状态为完成
            List<PickBatchItem> batchItemList = pickBatchItemService.getByPickBatchNo(pickBatchItem.getPickBatchNo()).stream()
                    .filter(x -> x.getStatus().equals(0))
                    .filter(x -> x.getItemCode().equals(param.getItemCode()))
                    .collect(Collectors.toList());
            //需要修改状态的item
            List<PickBatchItem> subBatchItemList =  batchItemList.subList(0, param.getReviewQuantity().intValue());
            for (PickBatchItem sub : subBatchItemList){
                sub.setStatus(1);
                sub.setPickUserId(param.getUserId());
                sub.setPickTime(DateUtils.now());
            }
            pickBatchItemService.batchUpdateStatusById(subBatchItemList);
        }else {
            PickBatchItem batchItem = new PickBatchItem();
            batchItem.setId(pickBatchItem.getId());
            batchItem.setStatus(1);
            batchItem.setPickUserId(pickParam.getUserId());
            batchItem.setWeight(param.getWeight());
            batchItem.setPound(param.getPound());
            batchItem.setPickTime(DateUtils.now());
            //更新item
            pickBatchItemService.updateById(batchItem);
        }
        List<PickBatchItem> groupItemList = pickBatchItemService.getByPickBatchGroupNo(pickBatchItem.getPickBatchGroupNo());
        List<Long> itemIdList = new ArrayList<>();
        for (PickBatchItem item : groupItemList){
            itemIdList.add(item.getId());
        }
        pickBatchItemService.refreshPickBatchInfoByItems(itemIdList);
        if (param.getWholesale()) {
            pickTaskService.pick(0x5, pickParam);
        } else {
            pickTaskService.pick(0xD, pickParam);
        }
    }

    @Synchronized(lockName = LockName.PICK_AFFIRM_BATCH, value = "#param.pickBatchNo")
    @Transactional
    @Override
    public void pickBatchWholesale(int type, PickBatchParam param){
        // 拣货任务执行记录PickTaskExecute
        PickTaskExecute execute = new PickTaskExecute();
        execute.setHasSuccess(false);
        execute.setExecuteTime(new Date());
        execute.setReviewQuantity(param.getReviewQuantity());
        execute.setMemo(Np.i(param.getMemo()).ifBlank(null).o());
        execute.setExecuteUserId(param.getUserId());
        execute.setExecuteUserName(param.getUserName());
        execute.setQrCode(Np.i(param.getQrCode()).ifBlank(null).o());
        execute.setToCellCode(Np.i(param.getToCellCode()).ifBlank(null).o());
        execute.setFromCellCode(param.getCellCode());
        execute.setBoxCode(param.getBoxCode());
        execute.setItemCode(param.getItemCode());
        PickBatchItem pickBatchItem = pickBatchItemService.getByQrCode(param.getQrCode());
        List<PickTask> pickTaskList;
        try {
            if (pickBatchItem == null){
                throw new ServiceException(CommonCode.SERVICE_ERROR, "无效的二维码");
            }
            if (!param.getPickBatchNo().equals(pickBatchItem.getPickBatchNo())){
                throw new ServiceException(CommonCode.SERVICE_ERROR, "波次和二维码不匹配");
            }
            if (!param.getCellCode().equals(pickBatchItem.getCellCode())){
                throw new ServiceException(CommonCode.SERVICE_ERROR, "货位和二维码不匹配");
            }
//            if (pickBatchItem.getStatus() == 1){
//                throw new ServiceException(CommonCode.SERVICE_ERROR, "此二维码已经扫描过");
//            }
            SoMaster so = soMasterService.findByBillNo(param.getSoBillNo());
            execute.setWareId(Np.i(so).x(x -> x.getWareId()).o());
            execute.setWareName(Np.i(so).x(x -> x.getWareName()).o());
            if (Np.i(so).x(x -> x.getState()).notIn(3, 4, 5)) {// 3-部分分配 4-分配完成 5-部分拣货
                throw new ServiceException(CommonCode.SERVICE_ERROR, "该订单状态无需拣货，请核对订单状态与商品数量");
            }
            pickTaskList = pickTaskService.findBySoMasterId(so.getSoMasterId()).stream()
                    .filter(x -> x.getItemCode().equals(param.getItemCode()))
                    .filter(x -> x.getPickQuantity() > x.getReviewQuantity())
                    .collect(Collectors.toList());// 总拣货任务
            if (pickTaskList.isEmpty()) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "此SKU拣出的数量已足够，无需再拣货");
            }
            Double reviewQuantity = param.getReviewQuantity();// 本次想要拣出数量（剩余分配）
            for (PickTask pickTask : pickTaskList) {
                Double taskQty = pickTask.getPickQuantity() - pickTask.getReviewQuantity();// 本任务需要拣出数量
                reviewQuantity -= taskQty;
            }
            if (reviewQuantity > 0) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "此次拣出的数量过多，请核对数量");
            }
        } catch (Exception e){
            pickTaskExecuteService.save(execute);
            throw e;
        }
        PickParam pickParam = new PickParam();
        BeanUtils.copyProperties(param, pickParam);
        pickParam.setPickTaskId(pickTaskList.get(0).getPickTaskId());
        pickParam.setReviewQuantity(param.getReviewQuantity());
        //修改pick_batch_item表状态为完成
        List<PickBatchItem> batchItemList = pickBatchItemService.getByPickBatchNo(pickBatchItem.getPickBatchNo()).stream().filter(x -> x.getStatus().equals(0)).collect(Collectors.toList());
        //需要修改状态的item
        List<PickBatchItem> subBatchItemList =  batchItemList.subList(0, param.getReviewQuantity().intValue());
        for (PickBatchItem sub : subBatchItemList){
            sub.setStatus(1);
            sub.setPickUserId(param.getUserId());
        }
        pickBatchItemService.batchUpdateStatusById(subBatchItemList);
        pickBatchItemService.refreshPickBatchInfoByItems(subBatchItemList.stream().map(x -> x.getId()).collect(Collectors.toSet()));
        pickTaskService.pick(type, pickParam);
    }

    @Override
    public MyBatchKanbanRespVO getBatchKanbanByDate(Date startDate,Date endDate, Integer userId) {
        MyBatchKanbanRespVO result = new MyBatchKanbanRespVO();
        PickBatchSearchParam searceParam = new PickBatchSearchParam();
        searceParam.setCreateTimeFrom(startDate);
        searceParam.setCreateTimeTo(endDate);
        List<PickBatch> pickBatch = this.getBatchList(searceParam);
        if (pickBatch == null || pickBatch.isEmpty()) {
            result.setTodaySum(0);
            result.setTodayUnfinished(0);
            result.setMyTodaySum(0);
            result.setMyTodayFinished(0);
            result.setMyTodayPickedQty(0D);
            return result;
        }
        Integer toDaySum = pickBatch.size();
        Integer todayFinishTask = (int) pickBatch.stream().filter(batch -> batch.getStatus() == 2).count(); // 今日已完成
        Integer myToDaySum = (int) pickBatch.stream().filter(batch -> userId.equals(batch.getPickUserId())).count(); // 本人今日波次
        Integer myToDayFinished = (int) pickBatch.stream().filter(batch -> userId.equals(batch.getPickUserId())).filter(batch -> batch.getStatus() == 2).count(); // 本人今日波次
        Double myTodayPickedQty = pickBatchItemService.getPickTotalByUserAndDate(startDate, endDate, userId);
        result.setTodaySum(toDaySum);
        result.setTodayUnfinished(toDaySum - todayFinishTask);
        result.setMyTodaySum(myToDaySum);
        result.setMyTodayFinished(myToDayFinished);
        result.setMyTodayPickedQty(myTodayPickedQty);
        return result;

    }

    @Override
    public List<PickBatch> getBatchList(PickBatchSearchParam param) {
        return mapper.findList(param);
    }

    @Override
    public List<PickBatchDto> getBatchDtoList(PickBatchSearchParam param) {
        List<PickBatch> list = this.getBatchList(param);
        List<PickBatchDto> result = new ArrayList<>(list.size());
        for (PickBatch pickBatch : list) {
            result.add(this.entityToDto(pickBatch));
        }
        return result;
    }

    @Override
    public PickBatchDto tagBatch(String pickBatchNo,Integer userId) {
        PickBatch pickBatch = mapper.getByNo(pickBatchNo);
        if (pickBatch==null){
            throw new ServiceException(CommonCode.PARAMETER_ERROR,"您选择的批次号有误");
        }
        if (pickBatch.getStatus() == 2) {
            throw new ServiceException(CommonCode.SERVICE_ERROR,"所选批次已经完成");
        }
        pickBatch.setPickUserId(userId);
        pickBatch.setTaskTime(new Date());
        mapper.updateByPrimaryKey(pickBatch);
        return this.entityToDto(pickBatch);
    }

    @Override
    public PickBatchDto entityToDto(PickBatch pickBatch) {
        PickBatchDto dto = new PickBatchDto();
        BeanUtils.copyBeanProp(dto, pickBatch);
        //商品总数
        Double skuQty = 0.0;
        //已拣货数量
        Double pickedQty = 0.0;
        //干货数量
        Double scbSkuQty = 0.0;
        //冷货数量
        Double coldSkuQty = 0.0;
        // TODO 没有异常数量这个字段 待定
        ArrayList<String> sku = new ArrayList<>() ;
        ArrayList<String> cell = new ArrayList<>() ;
        List<PickBatchItem> itemList = pickBatchItemService.getByPickBatchNo(pickBatch.getPickBatchNo());
        for (PickBatchItem item : itemList) {
            skuQty = skuQty + 1.0;
            if (item.getStatus() == 1 ){
                pickedQty = pickedQty +1.0;
            }
            if (!sku.contains(item.getItemCode())){
                sku.add(item.getItemCode());
            }
            if (!cell.contains(item.getCellCode())){
                cell.add(item.getCellCode());
            }
            String storageConditions = itemInfoService.getStorageConditions(item.getItemCode());
            if (StorageConditions.SCB.equals(storageConditions)) {
                scbSkuQty = scbSkuQty + 1.0;
            } else if (StorageConditions.COLD.equals(storageConditions)) {
                coldSkuQty = coldSkuQty + 1.0;
            }
        }
        //未拣货数量
        Double unPickedQty = skuQty - pickedQty;
        PickBatchGroup group = pickBatchGroupService.getByNo(pickBatch.getPickBatchGroupNo());
        //司机名
        if (group!= null ){
            dto.setDriverName(group.getDriverName());
        }
        dto.setSkuQty(skuQty);
        dto.setPickedQty(pickedQty);
        dto.setUnPickedQty(unPickedQty);
        dto.setSkuNum(sku.size());
        dto.setCellNum(cell.size());
        dto.setScbSkuQty(scbSkuQty);
        dto.setColdSkuQty(coldSkuQty);
        return dto;
    }

    @Override
    public List<PickBatch> getByGroupNo(String pickBatchGroupNo) {
        PickBatchSearchParam param = new PickBatchSearchParam();
        param.setPickBatchGroupNo(pickBatchGroupNo);
        return this.getBatchList(param);
    }

    @Override
    public List<PickBatch> getByNos(Collection<String> pickBatchNos) {
        if (pickBatchNos == null || pickBatchNos.isEmpty()) {
            return new ArrayList<>();
        }
        return mapper.getByNos(pickBatchNos);
    }

    @Override
    public void putById(PickBatch pickBatch) {
        if (pickBatch == null || pickBatch.getId() == null) {
            throw new ServiceException(CommonCode.SYSTEM_ERROR, "修改波次Detail时缺少ID");
        }
        mapper.updateByPrimaryKey(pickBatch);
    }

}
