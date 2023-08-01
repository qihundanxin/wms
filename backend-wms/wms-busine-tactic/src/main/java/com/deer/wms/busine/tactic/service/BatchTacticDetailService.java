package com.deer.wms.busine.tactic.service;

import com.deer.wms.busine.tactic.model.Batch.BatchTacticDetail;
import com.deer.wms.busine.tactic.model.Batch.BatchTacticDetailCriteria;
import com.deer.wms.busine.tactic.model.Batch.BatchTacticDetailDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2019/11/29.
 */
public interface BatchTacticDetailService extends Service<BatchTacticDetail, Integer> {

    List<BatchTacticDetailDto> findList(BatchTacticDetailCriteria criteria);

    List<BatchTacticDetailDto> findByState(Integer state);

    void deleteByCode(String batchTacticCode);

    List<BatchTacticDetail> findByBatchTacticCode(String batchTacticCode);

}
