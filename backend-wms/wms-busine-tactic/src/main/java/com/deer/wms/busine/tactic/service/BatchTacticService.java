package com.deer.wms.busine.tactic.service;

import com.deer.wms.busine.tactic.model.Batch.BatchTactic;
import com.deer.wms.busine.tactic.model.Batch.BatchTacticCriteria;
import com.deer.wms.busine.tactic.model.Batch.BatchTacticDto;
import com.deer.wms.busine.tactic.model.Batch.InsertData;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.service.Service;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by guo on 2019/11/29.
 */
public interface BatchTacticService extends Service<BatchTactic, Integer> {
    List<BatchTactic> findList(BatchTacticCriteria criteria);

    List<BatchTacticDto> findByState(Integer batchTacticId, Integer state);

    List<BatchTacticDto> findByOrganizationOrItemInfo(Integer organizationId, String itemCode);

    List<BatchTacticDto> findByOrganizationOrItemInfo2(Integer organizationId, String itemCode);

    void createBatchTactic(InsertData insertData, CurrentUser currentUser);

    void updateBatchTactic(InsertData insertData, CurrentUser currentUser);

    BatchTacticDto findByBatchTacticCode(String batchTacticCode);

}
