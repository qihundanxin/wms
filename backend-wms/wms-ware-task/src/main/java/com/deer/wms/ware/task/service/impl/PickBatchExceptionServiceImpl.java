package com.deer.wms.ware.task.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.deer.wms.ware.task.model.pickTask.PickBatchExcepitonResultVo;
import com.deer.wms.ware.task.model.pickTask.PickBatchExceptionParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.ware.task.dao.PickBatchExceptionMapper;
import com.deer.wms.ware.task.model.pickTask.PickBatchException;
import com.deer.wms.ware.task.model.pickTask.PickBatchItem;
import com.deer.wms.ware.task.service.PickBatchExceptionService;
import com.deer.wms.ware.task.service.PickBatchItemService;

import lombok.RequiredArgsConstructor;

/**
 * @author luolin
 **/
@Service
@Transactional
@RequiredArgsConstructor
public class PickBatchExceptionServiceImpl implements PickBatchExceptionService {

    private final PickBatchExceptionMapper dao;

    private final PickBatchItemService pickBatchItemService;

    @Override
    public void reportBatchException(PickBatchException pickBatchException) {
        //保存上报异常信息
        dao.insertSelective(pickBatchException);
        //修改pickBatchItem表状态
        List<PickBatchItem> pickBatchItemList = pickBatchItemService.getByPickBatchNo(pickBatchException.getPickBatchNo()).stream()
                .filter(x -> x.getCellCode().equals(pickBatchException.getCellCode()))
                .filter(x -> x.getItemCode().equals(pickBatchException.getItemCode()))
                .filter(x -> x.getStatus() == 0)
                .collect(Collectors.toList());
        Double qty = pickBatchException.getExceptionQty(); //上报异常数量
        if (qty > pickBatchItemList.size()){
            throw new ServiceException(CommonCode.SERVICE_ERROR, "上报数量不能大于此货位需要拣货数量");
        }
        int updataIndex = pickBatchItemList.size()-qty.intValue() < 0 ? 0 : pickBatchItemList.size()-qty.intValue();
        List<PickBatchItem> updateItemList = pickBatchItemList.subList(updataIndex, pickBatchItemList.size());
        for (PickBatchItem item : updateItemList){
            item.setStatus(2);
        }
        pickBatchItemService.batchUpdateStatusById(updateItemList);
        pickBatchItemService.refreshPickBatchInfoByItems(updateItemList.stream().map(x -> x.getId()).collect(Collectors.toSet()));// FIXME 2023-07-27 罗林review一下
    }

    @Override
    public List<PickBatchException> getPickBatchExceptionList() {
        List<PickBatchException> list = dao.getPickBatchExceptionList();
        return list;
    }

    @Override
    public List<PickBatchExcepitonResultVo> getExceptionList(PickBatchExceptionParam searchParam) {
        return dao.getExceptionList(searchParam);
    }

    @Override
    public List<PickBatchException> getAll(PickBatchExceptionParam searchParam) {
        List<PickBatchException> list = dao.getAll(searchParam);
        return list;
    }

    @Override
    public void handleExcepiton(PickBatchException pickBatchException) {
        dao.handleExcepiton(pickBatchException);
    }
}
