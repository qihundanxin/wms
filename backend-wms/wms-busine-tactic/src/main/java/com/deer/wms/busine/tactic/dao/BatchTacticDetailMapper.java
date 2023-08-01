package com.deer.wms.busine.tactic.dao;

import com.deer.wms.busine.tactic.model.Batch.BatchTacticDetail;
import com.deer.wms.busine.tactic.model.Batch.BatchTacticDetailCriteria;
import com.deer.wms.busine.tactic.model.Batch.BatchTacticDetailDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BatchTacticDetailMapper extends Mapper<BatchTacticDetail> {

    List<BatchTacticDetailDto> findList(BatchTacticDetailCriteria criteria);

    List<BatchTacticDetailDto> findByState(@Param("state") Integer state);

    void deleteByCode(@Param("batchTacticCode") String batchTacticCode);

    List<BatchTacticDetail> findByBatchTacticCode(@Param("batchTacticCode") String batchTacticCode);
}