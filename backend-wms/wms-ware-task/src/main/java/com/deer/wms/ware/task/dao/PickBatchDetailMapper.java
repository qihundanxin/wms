package com.deer.wms.ware.task.dao;

import java.util.List;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.pickTask.PickBatchDetail;

import io.lettuce.core.dynamic.annotation.Param;

/**
 * @author lixuehe
 * @date 2023/6/1
 */
public interface PickBatchDetailMapper extends Mapper<PickBatchDetail> {

    void updateStatusByGroup(@Param("pickBatchGroupNo") String pickBatchGroupNo, @Param("status") Integer status);

    /**
     * 根据波次号，查询有效的
     * 
     * @param batchNo 波次号
     * @return 查询结果
     */
    List<PickBatchDetail> findByBatchNo(String pickBatchNo);

    List<PickBatchDetail> findByBatchGroupNo(String pickBatchGroupNo);
}
