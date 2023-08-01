package com.deer.wms.busine.tactic.dao;

import com.deer.wms.busine.tactic.model.Batch.BatchTacticDetailDetail;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

public interface BatchTacticDetailDetailMapper extends Mapper<BatchTacticDetailDetail> {

    void deleteByCodeAndId(@Param("detailCode") String detailCode, @Param("batchTacticDetailId") Integer batchTacticDetailId);

}