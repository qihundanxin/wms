package com.deer.wms.busine.tactic.service.impl;

import com.deer.wms.busine.tactic.dao.BatchTacticDetailMapper;
import com.deer.wms.busine.tactic.model.Batch.BatchTacticDetail;
import com.deer.wms.busine.tactic.model.Batch.BatchTacticDetailCriteria;
import com.deer.wms.busine.tactic.model.Batch.BatchTacticDetailDto;
import com.deer.wms.busine.tactic.service.BatchTacticDetailService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2019/11/29.
 */
@Service
@Transactional
public class BatchTacticDetailServiceImpl extends AbstractService<BatchTacticDetail, Integer> implements BatchTacticDetailService {

    @Autowired
    private BatchTacticDetailMapper batchTacticDetailMapper;

    @Override
    public List<BatchTacticDetailDto> findList(BatchTacticDetailCriteria criteria) {
        return batchTacticDetailMapper.findList(criteria);
    }

    @Override
    public List<BatchTacticDetailDto> findByState(Integer state) {
        return batchTacticDetailMapper.findByState(state);
    }

    @Override
    public void deleteByCode(String batchTacticCode) {
        batchTacticDetailMapper.deleteByCode(batchTacticCode);
    }

    @Override
    public List<BatchTacticDetail> findByBatchTacticCode(String batchTacticCode) {
        if (batchTacticCode == null) {
            batchTacticCode = "sys_batch_tactic";
        }
        return batchTacticDetailMapper.findByBatchTacticCode(batchTacticCode);
    }

}
