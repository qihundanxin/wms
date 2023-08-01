package com.deer.wms.busine.tactic.service.impl;

import com.deer.wms.busine.tactic.dao.BatchTacticDetailDetailMapper;
import com.deer.wms.busine.tactic.model.Batch.BatchTacticDetailDetail;
import com.deer.wms.busine.tactic.service.BatchTacticDetailDetailService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by guo on 2019/11/29.
 */
@Service
@Transactional
public class BatchTacticDetailDetailServiceImpl extends AbstractService<BatchTacticDetailDetail, Integer> implements BatchTacticDetailDetailService {

    @Autowired
    private BatchTacticDetailDetailMapper batchTacticDetailDetailMapper;

    @Override
    public void deleteByCodeAndId(String detailCode, Integer batchTacticDetailId) {
        batchTacticDetailDetailMapper.deleteByCodeAndId(detailCode, batchTacticDetailId);
    }

}
