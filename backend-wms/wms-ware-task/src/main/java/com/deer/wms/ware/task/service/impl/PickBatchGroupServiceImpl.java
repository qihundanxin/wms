package com.deer.wms.ware.task.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.model.QrEntity;
import com.deer.wms.project.root.model.QrEntity.QrStringType;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.ware.task.dao.PickBatchGroupMapper;
import com.deer.wms.ware.task.model.pickTask.PickBatch;
import com.deer.wms.ware.task.model.pickTask.PickBatchDetail;
import com.deer.wms.ware.task.model.pickTask.PickBatchGroup;
import com.deer.wms.ware.task.model.pickTask.PickBatchItem;
import com.deer.wms.ware.task.model.pickTask.PickBatchListResponse;
import com.deer.wms.ware.task.model.pickTask.PickBatchResponse;
import com.deer.wms.ware.task.model.pickTask.PickTaskDto;
import com.deer.wms.ware.task.model.pickTask.PickTaskExecute;
import com.deer.wms.ware.task.model.print.SkuCellCodePosition;
import com.deer.wms.ware.task.model.print.SoSkuInfo;
import com.deer.wms.ware.task.service.PickBatchDetailService;
import com.deer.wms.ware.task.service.PickBatchGroupService;
import com.deer.wms.ware.task.service.PickBatchItemService;
import com.deer.wms.ware.task.service.PickBatchService;
import com.deer.wms.ware.task.service.PickTaskExecuteService;
import com.deer.wms.ware.task.service.PickTaskService;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

/**
 * @author lixuehe
 * @date 2023/6/1
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PickBatchGroupServiceImpl implements PickBatchGroupService {

    private final PickBatchGroupMapper mapper;
    private final PickTaskService pickTaskService;

    @Autowired
    private PickBatchService pickBatchService;
    @Autowired
    private PickBatchDetailService pickBatchDetailService;
    @Autowired
    private PickBatchItemService pickBatchItemService;
    @Autowired
    private PickTaskExecuteService pickTaskExecuteService;


    @Override
    public void saveGroupBatchAndDetail(PickBatchResponse result, String driverName, CurrentUser user) {
        Date now = new Date();
        // 0. 取消原有波次
        String pickBatchGroupNo = result.getPickBatchGroupNo();
        PickBatchGroup criteria = new PickBatchGroup();
        criteria.setPickBatchGroupNo(pickBatchGroupNo);
        List<PickBatchGroup> groupDtos = this.findList(criteria);
        if (groupDtos.size() > 0) {
            this.updateStatus(pickBatchGroupNo, -1);
            pickBatchService.updateStatusByGroup(pickBatchGroupNo, -1);
            pickBatchDetailService.updateStatusByGroup(pickBatchGroupNo, -1);
            pickBatchItemService.updateStatusByGroup(pickBatchGroupNo, -1);
        }

        PickBatchGroup newGroup = new PickBatchGroup();// 波次组
        List<PickBatch> newBatchList = new ArrayList<>();// 波次
        Map<String, PickBatchDetail> detailMap = new LinkedHashMap<>();// 波次Detail（UUID -> Detail）
        Map<String, List<PickBatchItem>> detailToItem = new HashMap<>();// UUID -> ItemList
        List<PickBatchItem> newItemList = new ArrayList<>();// 波次标签
        Map<String, Map<String, Integer>> soToSkuToIndex = new HashMap<>();// 用于计算这个SKU是订单中的第几个（一物一码的关键：序号）
        // 1. 构建新的波次组
        newGroup.setCreateTime(now);
        newGroup.setCreateUserId(user.getUserId());
        newGroup.setCreateUserName(user.getUserName());
        newGroup.setStatus(0);
        newGroup.setTotalQty(result.getTotalQty());
        newGroup.setPickBatchGroupNo(pickBatchGroupNo);
        newGroup.setDriverName(driverName);
        String pqOrderLst = result.getPqOrderLst().stream().collect(Collectors.joining(","));
        newGroup.setPqOrderLst(pqOrderLst);
        newGroup.setTotalOrderCount(result.getTotalOrderCount());

        List<PickBatchListResponse> pickBatchList = result.getPickBatchList();
        for (PickBatchListResponse response : pickBatchList) {
            PickBatch batch = new PickBatch();
            batch.setPickBatchGroupNo(pickBatchGroupNo);
            batch.setPickBatchNo(response.getPickBatchId());
            batch.setStatus(0);
            batch.setCreateTime(now);
            batch.setCreateUserId(user.getUserId());
            batch.setCreateUserName(user.getUserName());
            batch.setWholesale(response.getWholesale());
            batch.setContainerType(response.getContainer_type());
            newBatchList.add(batch);// 2. 构建新的波次

            List<SoSkuInfo> soSkuInfoList = response.getItemList();
            Map<SkuCellCodePosition, String> keyToDetail = new HashMap<>();// 用于汇总Detail（UUID），判断Item是不是同Detail
            for (SoSkuInfo soSkuInfo : soSkuInfoList) {
                SkuCellCodePosition skuCellCodePosition = new SkuCellCodePosition(soSkuInfo.getItemCode(), soSkuInfo.getCellCode(), soSkuInfo.getPosition());
                String detailUuid;
                if (!keyToDetail.containsKey(skuCellCodePosition)) {
                    PickBatchDetail newDetail = new PickBatchDetail();
                    newDetail.setCellCode(soSkuInfo.getCellCode());
                    newDetail.setItemName(soSkuInfo.getItemName());
                    newDetail.setItemQty(1D);
                    newDetail.setReviewQty(0D);
                    newDetail.setExceptionQty(0D);
                    newDetail.setPickBatchGroupNo(pickBatchGroupNo);
                    newDetail.setPickBatchNo(response.getPickBatchId());
                    newDetail.setStatus(0);
                    newDetail.setItemCode(soSkuInfo.getItemCode());
                    newDetail.setItemName(soSkuInfo.getItemName());
                    newDetail.setCellCode(soSkuInfo.getCellCode());
                    newDetail.setPosition(soSkuInfo.getPosition());
                    newDetail.setCreateTime(now);
                    newDetail.setCreateUserId(user.getUserId());
                    newDetail.setCreateUserName(user.getUserName());
                    detailUuid = IdUtil.fastSimpleUUID();
                    detailMap.put(detailUuid, newDetail);
                    keyToDetail.put(skuCellCodePosition, detailUuid);// 汇总Detail
                    detailToItem.put(detailUuid, new ArrayList<>());// 3. 构建新Detail
                } else {
                    detailUuid = keyToDetail.get(skuCellCodePosition);
                    PickBatchDetail newDetail = detailMap.get(detailUuid);
                    newDetail.setItemQty(newDetail.getItemQty() + 1D);// 3. 构建新Detail
                }

                PickBatchItem newItem = new PickBatchItem();
                newItem.setCellCode(soSkuInfo.getCellCode());
                newItem.setCreateTime(now);
                newItem.setCreateUserId(user.getUserId());
                newItem.setCreateUserName(user.getUserName());
                newItem.setItemCode(soSkuInfo.getItemCode());
                newItem.setItemName(soSkuInfo.getItemName());
                newItem.setPickBatchGroupNo(pickBatchGroupNo);
                newItem.setPickBatchNo(response.getPickBatchId());
                newItem.setPosition(soSkuInfo.getPosition());
                newItem.setSoBillNo(soSkuInfo.getBillNo());
                newItem.setDeliverIndex(soSkuInfo.getOrderIndex());
                newItem.setStatus(0);
                {
                    // 序号计算
                    Map<String, Integer> soSku2Index = Np.i(soToSkuToIndex.get(soSkuInfo.getBillNo())).o(new HashMap<>());
                    Integer soSkuIndex = Np.i(soSku2Index.get(soSkuInfo.getItemCode())).o(0);
                    soSkuIndex++;
                    soSku2Index.put(soSkuInfo.getItemCode(), soSkuIndex);
                    soToSkuToIndex.put(soSkuInfo.getBillNo(), soSku2Index);
                    // 二维码生成
                    QrEntity rqEntity = new QrEntity();
                    rqEntity.setSo(soSkuInfo.getBillNo());
                    rqEntity.setSku(soSkuInfo.getItemCode());
                    rqEntity.setSkui(soSkuIndex + "");
                    DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
                    rqEntity.setSkuqty(decimalFormat.format(soSkuInfo.getOrderQuantity()));
                    newItem.setQrCode(rqEntity.entityToString(QrStringType.SIMPLE_STRING));
                }
                newItem.setPickBatchDetailId(null);// 此处缺少DetailId，需要Detail插入完成后才知道。插入后设置ID
                newItemList.add(newItem);// 4. 构建新Item
                detailToItem.get(detailUuid).add(newItem);// 缓存detail->item的关系，用于生成Item中的DetailId
            }
        }
        
        // 重新计算Item初始状态
        Map<String, Double> allPickQtyMap = new HashMap<>();// [so+sku+cellcode]->ReviewQty
        Set<String> soBillNos = newItemList.stream().map(x->x.getSoBillNo()).collect(Collectors.toSet());
        for (String soBillNo : soBillNos) {
            List<PickTaskDto> tasks = pickTaskService.findBySo(soBillNo);
            for (PickTaskDto task : tasks) {
                String key = soBillNo + task.getItemCode() + task.getCellCode();
                Double reviewQty = Np.i(task).x(x -> x.getReviewQuantity()).o(0D);
                allPickQtyMap.put(key, Np.i(allPickQtyMap.get(key)).o(0D) + reviewQty);
            }
        }
        for (PickBatchItem pickBatchItem : newItemList) {// 有二维码的，标记对应二维码为已完成
            if (StrUtil.isNotBlank(pickBatchItem.getQrCode())) {
                List<PickTaskExecute> executedList = pickTaskExecuteService.getByQrCode(pickBatchItem.getQrCode());
                List<PickTaskExecute> pickTaskExecuteSuccesseds = executedList.stream().filter(x -> x.getHasSuccess()).collect(Collectors.toList());
                if (pickTaskExecuteSuccesseds.size() > 0) {
                    pickBatchItem.setStatus(1);// 此Item执行过
                    pickBatchItem.setPickUserId(pickTaskExecuteSuccesseds.get(0).getExecuteUserId());
                    pickBatchItem.setPickTime(pickTaskExecuteSuccesseds.get(0).getExecuteTime());
                    List<PickBatchItem> oldItemList = pickBatchItemService.getAllByQr(pickBatchItem.getQrCode());
                    PickBatchItem oldItem = oldItemList.stream().filter(x -> x.getPickTime() != null && x.getStatus() == -1)
                            .max((x, y) -> (int) (x.getPickTime().getTime() - y.getPickTime().getTime())).orElse(null);
                    pickBatchItem.setWeight(Np.i(oldItem).x(x -> x.getWeight()).o());
                    pickBatchItem.setPound(Np.i(oldItem).x(x -> x.getPound()).o());
                    String key = pickBatchItem.getSoBillNo() + pickBatchItem.getItemCode() + pickBatchItem.getCellCode();
                    allPickQtyMap.put(key, allPickQtyMap.get(key) - 1D);
                }
            }
        }
        for (PickBatchItem pickBatchItem : newItemList) {// 二维码没匹配到，但数量扣减了（批发单或使用了非波次拣货）
            String key = pickBatchItem.getSoBillNo() + pickBatchItem.getItemCode() + pickBatchItem.getCellCode();
            if (Np.i(allPickQtyMap.get(key)).o(0D) >= 1D && Np.i(pickBatchItem.getStatus()).eq(0)) {
                pickBatchItem.setStatus(1);
                pickBatchItem.setPickUserId(206);// 通过其他方式拣货扣除了，不再追查拣货人，标记为系统默认（实际拣货人记录在Execute）
                pickBatchItem.setPickTime(new Date());
                allPickQtyMap.put(key, allPickQtyMap.get(key) - 1D);
            }
        }

        // 保存
        mapper.insert(newGroup);
        for (PickBatch newPickBatch : newBatchList) {
            pickBatchService.save(newPickBatch);
        }
        for (Map.Entry<String, List<PickBatchItem>> e : detailToItem.entrySet()) {
            PickBatchDetail p = pickBatchDetailService.save(detailMap.get(e.getKey()));
            e.getValue().forEach(x -> x.setPickBatchDetailId(p.getId()));// 主键回填，生成Item中的DetailId
        }
        for (PickBatchItem newItem : newItemList) {
            pickBatchItemService.add(newItem);
        }

        // 刷新上级状态
        pickBatchItemService.refreshPickBatchInfoByItems(newItemList.stream().map(x -> x.getId()).collect(Collectors.toList()));
    }

    public List<PickBatchGroup> findList(PickBatchGroup criteria) {
        return mapper.findList(criteria);
    }

    public void updateStatus(String pickBatchGroupNo, Integer status) {
        mapper.updateStatus(pickBatchGroupNo, status);
    }

    @Override
    public PickBatchGroup getByNo(String pickBatchGroupNo) {
        return mapper.getByNo(pickBatchGroupNo);
    }

    @Override
    public List<PickBatchGroup> getByNos(Collection<String> pickBatchGroupNos) {
        if (pickBatchGroupNos == null || pickBatchGroupNos.isEmpty()) {
            return new ArrayList<>();
        }
        return mapper.getByNos(pickBatchGroupNos);
    }

    @Override
    public void putById(PickBatchGroup pickBatchGroup) {
        if (pickBatchGroup == null || pickBatchGroup.getId() == null) {
            throw new ServiceException(CommonCode.SYSTEM_ERROR, "修改波次Group时缺少ID");
        }
        mapper.updateByPrimaryKey(pickBatchGroup);
    }
}
