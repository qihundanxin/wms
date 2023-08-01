package com.deer.wms.busine.tactic.dao;

import com.deer.wms.busine.tactic.model.Batch.BatchTactic;
import com.deer.wms.busine.tactic.model.Batch.BatchTacticCriteria;
import com.deer.wms.busine.tactic.model.Batch.BatchTacticDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BatchTacticMapper extends Mapper<BatchTactic> {
    List<BatchTactic> findList(BatchTacticCriteria criteria);

    List<BatchTacticDto> findByState(@Param("batchTacticId") Integer batchTacticId, @Param("state") Integer state);

    List<BatchTacticDto> findByOrganizationOrItemInfo(@Param("organizationId") Integer organizationId, @Param("itemCode") String itemCode);

    List<BatchTacticDto> findByOrganizationOrItemInfo2(@Param("organizationId") Integer organizationId, @Param("itemCode") String itemCode);

    BatchTacticDto findByBatchTacticCode(@Param("batchTacticCode") String batchTacticCode);
}