package com.deer.wms.ware.task.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.pickTask.PickBatchExcepitonResultVo;
import com.deer.wms.ware.task.model.pickTask.PickBatchException;
import com.deer.wms.ware.task.model.pickTask.PickBatchExceptionParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author dell
 **/
public interface PickBatchExceptionMapper extends Mapper<PickBatchException> {

    /**
     * 查询未处理异常的货位和sku及异常数
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
     * 修改异常处理状态
     * @author luolin
     * @param pickBatchException
     * @return
     **/
    void handleExcepiton(PickBatchException pickBatchException);
}
