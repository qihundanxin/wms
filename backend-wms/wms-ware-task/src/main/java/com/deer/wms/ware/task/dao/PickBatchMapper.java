package com.deer.wms.ware.task.dao;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.pickTask.PickBatch;
import com.deer.wms.ware.task.model.pickTask.PickBatchSearchParam;

/**
 * @author lixuehe
 * @date 2023/6/1
 */
public interface PickBatchMapper extends Mapper<PickBatch> {

    void updateStatusByGroup(@Param("pickBatchGroupNo") String pickBatchGroupNo, @Param("status") Integer status);

    /**
     * 只查询有效的
     * 
     * @param pickBatchNo
     * @return
     */
    PickBatch getByNo(String pickBatchNo);

    /**
     * 多个NO查询 只查询有效的
     * 
     * @param pickBatchNos
     * @return
     */
    List<PickBatch> getByNos(@Param("pickBatchNos") Collection<String> pickBatchNos);

    /**
     * List查询
     * 
     * @param param 条件
     * @return list
     */
    List<PickBatch> findList(PickBatchSearchParam param);

}
