package com.deer.wms.busine.tactic.service;

import com.deer.wms.busine.tactic.model.Batch.BatchTacticDetailDetail;
import com.deer.wms.project.root.core.service.Service;

/**
 * Created by guo on 2019/11/29.
 */
public interface BatchTacticDetailDetailService extends Service<BatchTacticDetailDetail, Integer> {

    void deleteByCodeAndId(String detailCode, Integer batchTacticDetailId);

}
