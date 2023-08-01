package com.deer.wms.ware.task.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.ware.task.dao.PickBatchDetailMapper;
import com.deer.wms.ware.task.model.pickTask.PickBatchDetail;
import com.deer.wms.ware.task.service.PickBatchDetailService;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;

/**
 * @author lixuehe
 * @date 2023/6/1
 */
@Service
@Transactional
@AllArgsConstructor
public class PickBatchDetailServiceImpl implements PickBatchDetailService {


    private PickBatchDetailMapper mapper;


    @Override
    public void updateStatusByGroup(String pickBatchGroupNo, Integer status) {
        mapper.updateStatusByGroup(pickBatchGroupNo, status);
    }

    @Override
    public PickBatchDetail save(PickBatchDetail detail) {
        detail.setId(null);
        detail.setCreateTime(Np.i(detail.getCreateTime()).o(new Date()));
        if (StrUtil.isBlank(detail.getPickBatchGroupNo())
                || StrUtil.isBlank(detail.getPickBatchNo())
                || StrUtil.isBlank(detail.getItemCode())
                || Np.i(detail.getItemQty()).o(0D) <= 0
        ) {
            throw new ServiceException(CommonCode.SYSTEM_ERROR, "PickBatchDetail缺少必要的值：GroupNo、BatchNo、ItemCode、Qty");
        }
        mapper.insert(detail);
        return detail;
    }

    @Override
    public void updateStatusById(Long id, Integer status) {
        PickBatchDetail detail = new PickBatchDetail();
        detail.setId(id);
        detail.setStatus(status);
        mapper.updateByPrimaryKeySelective(detail);
    }

    @Override
    public List<PickBatchDetail> findByPickBatchNo(String pickBatchNo) {
        return mapper.findByBatchNo(pickBatchNo);
    }

    @Override
    public void updatePickBatchDetail(PickBatchDetail pickBatchDetail){
        PickBatchDetail detail = mapper.selectByPrimaryKey(pickBatchDetail.getId());
        if (detail == null){
            return;
        }
        detail.setStatus(pickBatchDetail.getStatus());
        detail.setReviewQty(detail.getReviewQty() + pickBatchDetail.getReviewQty());
        mapper.updateByPrimaryKey(detail);
    }

    @Override
    public List<PickBatchDetail> findByPickBatchGroupNo(String pickBatchGroupNo) {
        return mapper.findByBatchGroupNo(pickBatchGroupNo);
    }

    @Override
    public List<PickBatchDetail> getByIds(Collection<Long> pickBatchDetailIds) {
        if (pickBatchDetailIds == null || pickBatchDetailIds.isEmpty()) {
            return new ArrayList<>();
        }
        String ids = pickBatchDetailIds.stream().distinct()
                .filter(x -> x != null && StrUtil.isNotBlank(x.toString()))
                .map(x -> x.toString())
                .collect(Collectors.joining(","));
        if (StrUtil.isBlank(ids)) {
            return new ArrayList<>();
        }
        return mapper.selectByIds(ids).stream().filter(x -> x.getStatus() >= 0).collect(Collectors.toList());
    }

    @Override
    public void putById(PickBatchDetail pickBatchDetail) {
        if (pickBatchDetail == null || pickBatchDetail.getId() == null) {
            throw new ServiceException(CommonCode.SYSTEM_ERROR, "修改波次Detail时缺少ID");
        }
        mapper.updateByPrimaryKey(pickBatchDetail);
    }
}
