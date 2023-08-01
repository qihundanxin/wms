package com.deer.wms.ware.task.dao;

import java.util.Collection;
import java.util.List;

import com.deer.wms.ware.task.model.pickTask.PickBatchGroup;

import io.lettuce.core.dynamic.annotation.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author lixuehe
 * @date 2023/6/1
 */
public interface PickBatchGroupMapper extends Mapper<PickBatchGroup> {

    List<PickBatchGroup> findList(PickBatchGroup criteria);

    void updateStatus(@Param("pickBatchGroupNo") String pickBatchGroupNo, @Param("status") Integer status);

    PickBatchGroup getByNo(String pickBatchGroupNo);

    List<PickBatchGroup> getByNos(@Param("pickBatchGroupNos") Collection<String> pickBatchGroupNos);

}
