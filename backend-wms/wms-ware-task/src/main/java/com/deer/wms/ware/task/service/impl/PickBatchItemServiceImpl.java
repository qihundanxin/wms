package com.deer.wms.ware.task.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deer.wms.base.system.model.Item.ItemInfo;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.ware.task.dao.PickBatchItemMapper;
import com.deer.wms.ware.task.model.pickTask.PickBatch;
import com.deer.wms.ware.task.model.pickTask.PickBatchDetail;
import com.deer.wms.ware.task.model.pickTask.PickBatchGroup;
import com.deer.wms.ware.task.model.pickTask.PickBatchItem;
import com.deer.wms.ware.task.model.pickTask.PickBatchItemDto;
import com.deer.wms.ware.task.service.PickBatchDetailService;
import com.deer.wms.ware.task.service.PickBatchGroupService;
import com.deer.wms.ware.task.service.PickBatchItemService;
import com.deer.wms.ware.task.service.PickBatchService;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PickBatchItemServiceImpl implements PickBatchItemService {

    private final PickBatchItemMapper dao;

    private final ItemInfoService itemInfoService;

    @Autowired
    private PickBatchGroupService pickBatchGroupService;

    @Autowired
    private PickBatchService pickBatchService;

    @Autowired
    private PickBatchDetailService pickBatchDetailService;

    @Override
    public void updateStatusByGroup(String pickBatchGroupNo, Integer status) {
        dao.updateStatusByGroup(pickBatchGroupNo, status);
    }

    @Override
    public List<PickBatchItem> getByPickBatchGroupNo(String pickBatchGroupNo) {
        return dao.getByPickBatchGroupNo(pickBatchGroupNo);
    }

    @Override
    public List<PickBatchItem> getByPickBatchNo(String pickBatchNo) {
        return dao.getByPickBatchNo(pickBatchNo);
    }

    @Override
    public void updateById(PickBatchItem pickBatchItem) {
        pickBatchItem.setPickBatchGroupNo(null);
        pickBatchItem.setPickBatchNo(null);
        pickBatchItem.setPickBatchDetailId(null);
        pickBatchItem.setCreateTime(null);
        pickBatchItem.setCreateUserId(null);
        pickBatchItem.setCreateUserName(null);
        dao.updateByPrimaryKeySelective(pickBatchItem);
    }

    @Override
    public PickBatchItem add(PickBatchItem pickBatchItem) {
        if (StrUtil.isBlank(pickBatchItem.getQrCode())) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "波次详情，缺少一物一码");
        }
        PickBatchItem exist = this.getByQrCode(pickBatchItem.getQrCode());
        if (exist != null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "重复的SKU码" + pickBatchItem.getQrCode() + "，已存在于波次：" + exist.getPickBatchNo());
        }
        if (StrUtil.isBlank(pickBatchItem.getPickBatchGroupNo())
                || StrUtil.isBlank(pickBatchItem.getPickBatchNo())
                || pickBatchItem.getPickBatchDetailId() == null
                || StrUtil.isBlank(pickBatchItem.getItemCode())) {
            // 货位为推荐货位不是必填，缺货可能会导致无货位，异常会导致更换货位
            throw new ServiceException(CommonCode.SYSTEM_ERROR, "缺少必要的信息：GroupNo、BatchNo、DetailId");
        }
        pickBatchItem.setCreateTime(Np.i(pickBatchItem.getCreateTime()).o(new Date()));
        pickBatchItem.setStatus(Np.i(pickBatchItem.getStatus()).o(0));
        pickBatchItem.setId(null);
        dao.insert(pickBatchItem);
        return pickBatchItem;
    }

    @Override
    public PickBatchItem getByQrCode(String qrCode) {
        return dao.getByQrCode(qrCode);
    }

    @Override
    public List<PickBatchItem> getByDetailId(Long detailId) {
        return dao.getByDetailId(detailId);
    }

    @Override
    public Integer summaryBatchStatus(Collection<PickBatchItem> items) {
        Long status0 = items.stream().filter(x -> x.getStatus().equals(0)).count();
        Long status1 = items.stream().filter(x -> x.getStatus().equals(1)).count();
        if (status0 == 0 && status1 > 0) {
            return 2;
        } else if (status0 > 0 && status1 > 0) {
            return 1;
        } else if (status0 > 0 && status1 == 0) {
            return 0;
        } else {
            return -1;
        }
    }


    @Override
    public Double getPickTotalByUserAndDate(Date startDate, Date endDate, Integer userId) {

        return Np.i(dao.getPickTotalByUserAndDate(startDate,endDate,userId)).o(0.0);
    }

    @Override
    public void batchUpdateStatusById(List<PickBatchItem> pickBatchItem){
        dao.batchUpdateStatusById(pickBatchItem);
    }

    @Override
    public PickBatchItemDto entityToDto(PickBatchItem item) {
        PickBatchItemDto dto = new PickBatchItemDto();
        BeanUtils.copyBeanProp(dto, item);
        ItemInfo itemInfo = itemInfoService.findByItemCode(item.getItemCode());
        dto.setImgUrl(Np.i(itemInfo.getImgUrl()).o(null));
        dto.setWeighting(itemInfo.getWeighting());
        dto.setUnpacking(itemInfo.getUnpacking());
        return dto;
    }

    @Override
    public List<PickBatchItem> getExceptionItem() {
        List<PickBatchItem> list = dao.getExceptionItem();
        return list;
    }

    @Override
    public void insertList(List<PickBatchItem> pickBatchItemList) {
        dao.insertList(pickBatchItemList);
    }

    @Override
    public void refreshPickBatchInfoByItems(Collection<Long> itemIds) {
        List<PickBatchItem> itemList = this.getByIds(itemIds);// 触发改变的Items

        Set<String> pickBatchGroupNos = itemList.stream().map(x -> x.getPickBatchGroupNo()).collect(Collectors.toSet());// 波次组IDs
        Set<String> pickBatchNos = itemList.stream().map(x -> x.getPickBatchNo()).collect(Collectors.toSet());// 波次IDs
        Set<Long> pickBatchDetailIds = itemList.stream().map(x -> x.getPickBatchDetailId()).collect(Collectors.toSet());// 波次DetailIDs

        List<PickBatchItem> allItemList = this.getByPickBatchGroupNos(pickBatchGroupNos);// 参与运算的全部Item

        List<PickBatchGroup> pickBatchGroups = pickBatchGroupService.getByNos(pickBatchGroupNos);// 需要更新的波次组
        for (PickBatchGroup pickBatchGroup : pickBatchGroups) {
            String snapshot = pickBatchGroup.toString();
            Set<PickBatchItem> thisItems = allItemList.stream().filter(x -> x.getPickBatchGroupNo().equals(pickBatchGroup.getPickBatchGroupNo())).collect(Collectors.toSet());
            boolean hasB1 = false;
            boolean has0 = false;
            boolean has1 = false;
            boolean has2 = false;
            double totalQty = 0D;
            for(PickBatchItem item : thisItems) {
                if(item.getStatus() == -1) {
                    hasB1 = true;
                }else if(item.getStatus() == 0) {
                    has0 = true;
                }else if(item.getStatus() == 1) {
                    has1 = true;
                }else if(item.getStatus() == 2) {
                    has2 = true;
                }
                totalQty = totalQty + 1D;
            }
            pickBatchGroup.setTotalQty(totalQty);
            if (hasB1 && !has0 && !has1 && !has2) {
                pickBatchGroup.setStatus(-1);
            } else if (!has1 && (has0 || has2)) {
                pickBatchGroup.setStatus(0);
            } else if (has1 && (has0 || has2)) {
                pickBatchGroup.setStatus(1);
            } else if (has1 && !has0 && !has2) {
                pickBatchGroup.setStatus(2);
            }
            if (!snapshot.equals(pickBatchGroup.toString())) {
                pickBatchGroupService.putById(pickBatchGroup);
            }
        }

        List<PickBatch> pickBatchs = pickBatchService.getByNos(pickBatchNos);// 需要更新的波次
        for (PickBatch pickBatch : pickBatchs) {
            String snapshot = pickBatch.toString();
            Set<PickBatchItem> thisItems = allItemList.stream().filter(x -> x.getPickBatchNo().equals(pickBatch.getPickBatchNo())).collect(Collectors.toSet());
            boolean hasB1 = false;
            boolean has0 = false;
            boolean has1 = false;
            boolean has2 = false;
            Date pickTime = pickBatch.getPickTime();
            for (PickBatchItem item : thisItems) {
                if (item.getStatus() == -1) {
                    hasB1 = true;
                } else if (item.getStatus() == 0) {
                    has0 = true;
                } else if (item.getStatus() == 1) {
                    has1 = true;
                    pickTime = Np.i(pickTime).els(old -> old.compareTo(item.getPickTime()) > 0, item.getPickTime()).o(item.getPickTime());
                } else if (item.getStatus() == 2) {
                    has2 = true;
                }
            }
            if (hasB1 && !has0 && !has1 && !has2) {
                pickBatch.setStatus(-1);
            } else if (!has1 && (has0 || has2)) {
                pickBatch.setStatus(0);
            } else if (has1 && (has0 || has2)) {
                pickBatch.setStatus(1);
            } else if (has1 && !has0 && !has2) {
                pickBatch.setStatus(2);
            }
            pickBatch.setPickTime(pickTime);
            if (!snapshot.equals(pickBatch.toString())) {
                pickBatchService.putById(pickBatch);
            }
        }

        List<PickBatchDetail> pickBatchDetails = pickBatchDetailService.getByIds(pickBatchDetailIds);// 需要更新的Detail
        for (PickBatchDetail pickBatchDetail : pickBatchDetails) {
            String snapshot = pickBatchDetail.toString();
            Set<PickBatchItem> thisItems = allItemList.stream().filter(x -> x.getPickBatchDetailId().equals(pickBatchDetail.getId())).collect(Collectors.toSet());
            boolean hasB1 = false;
            boolean has0 = false;
            boolean has1 = false;
            boolean has2 = false;
            double itemQty = 0D;
            double reviewQty = 0D;
            double exceptionQty = 0D;
            for (PickBatchItem item : thisItems) {
                if (item.getStatus() == -1) {
                    hasB1 = true;
                } else if (item.getStatus() == 0) {
                    has0 = true;
                    itemQty = itemQty + 1;
                } else if (item.getStatus() == 1) {
                    has1 = true;
                    itemQty = itemQty + 1;
                    reviewQty = reviewQty + 1;
                } else if (item.getStatus() == 2) {
                    has2 = true;
                    itemQty = itemQty + 1;
                    exceptionQty = exceptionQty + 1;
                }
            }
            pickBatchDetail.setReviewQty(reviewQty);
            pickBatchDetail.setExceptionQty(exceptionQty);
            if (hasB1 && !has0 && !has1 && !has2) {
                pickBatchDetail.setStatus(-1);
            } else if (!has1 && (has0 || has2)) {
                pickBatchDetail.setStatus(0);
            } else if (has1 && (has0 || has2)) {
                pickBatchDetail.setStatus(1);
            } else if (has1 && !has0 && !has2) {
                pickBatchDetail.setStatus(2);
            }
            if (!snapshot.equals(pickBatchDetail.toString())) {
                pickBatchDetailService.putById(pickBatchDetail);
            }
        }

    }

    @Override
    public List<PickBatchItem> getByIds(Collection<Long> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) {
            return new ArrayList<>();
        }
        String ids = itemIds.stream().distinct()
                .filter(x -> x != null && StrUtil.isNotBlank(x.toString()))
                .map(x -> x.toString())
                .collect(Collectors.joining(","));
        if (StrUtil.isBlank(ids)) {
            return new ArrayList<>();
        }
        return dao.selectByIds(ids).stream().filter(x -> x.getStatus() >= 0).collect(Collectors.toList());
    }

    @Override
    public List<PickBatchItem> getByPickBatchGroupNos(Collection<String> pickBatchGroupNos) {
        if (pickBatchGroupNos == null || pickBatchGroupNos.isEmpty()) {
            return new ArrayList<>();
        }
        return dao.getByPickBatchGroupNos(pickBatchGroupNos);
    }

    @Override
    public List<PickBatchItem> getAllByQr(String qrCode) {
        if (qrCode == null) {
            return null;
        }
        PickBatchItem pickBatchItem = new PickBatchItem();
        pickBatchItem.setQrCode(qrCode);
        return dao.select(pickBatchItem);
    }
}
