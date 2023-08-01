package com.deer.wms.ware.task.service;

import com.deer.wms.ware.task.model.pickTask.PickBatchExcepitonResultVo;
import com.deer.wms.ware.task.model.pickTask.PickBatchException;
import com.deer.wms.ware.task.model.pickTask.PickBatchExceptionParam;

import java.util.List;

/**
 * @author luolin
 **/
public interface PickBatchExceptionService {

    /**
     * 波次拣货上报异常
     * @author luolin
     * @param pickBatchException
     * @return
     **/
    void reportBatchException(PickBatchException pickBatchException);

    /**
     * 查询波次拣货上报异常的货位和sku
     * @author luolin
     * @return java.util.List<com.deer.wms.ware.task.model.pickTask.PickBatchException>
     **/
    List<PickBatchException> getPickBatchExceptionList();

    /**
     * 异常处理列表查询
     * @author luolin
     * @param searchParam
     * @return java.util.List<com.deer.wms.ware.task.model.pickTask.PickBatchExcepitonResultVo>
     **/
    List<PickBatchExcepitonResultVo> getExceptionList(PickBatchExceptionParam searchParam);

    /**
     * 根据条件查询所有波次拣货异常信息
     * @author luolin
     * @param searchParam
     * @return java.util.List<com.deer.wms.ware.task.model.pickTask.PickBatchException>
     **/
    List<PickBatchException> getAll(PickBatchExceptionParam searchParam);

    /**
     * 根据货位和sku修改处理状态
     * @author luolin
     * @param pickBatchException
     * @return
     **/
    void handleExcepiton(PickBatchException pickBatchException);
}
