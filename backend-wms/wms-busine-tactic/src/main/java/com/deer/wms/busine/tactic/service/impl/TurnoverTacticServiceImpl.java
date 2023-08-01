package com.deer.wms.busine.tactic.service.impl;

import com.deer.wms.busine.tactic.dao.TurnoverTacticMapper;
import com.deer.wms.busine.tactic.model.TurnoverTactic;
import com.deer.wms.busine.tactic.model.TurnoverTacticCriteria;
import com.deer.wms.busine.tactic.model.TurnoverTacticDetail;
import com.deer.wms.busine.tactic.service.TurnoverTacticDetailService;
import com.deer.wms.busine.tactic.service.TurnoverTacticService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by  on 2020/03/15.
 */
@Service
@Transactional
public class TurnoverTacticServiceImpl extends AbstractService<TurnoverTactic, Integer> implements TurnoverTacticService {

    @Autowired
    private TurnoverTacticMapper turnoverTacticMapper;
    @Autowired
    private TurnoverTacticDetailService turnoverTacticDetailService;

    @Override
    public List<TurnoverTactic> findList(TurnoverTacticCriteria criteria) {
        return turnoverTacticMapper.findList(criteria);
    }

    @Override
    public void deleteTurnoverDetailAndMaster(TurnoverTacticCriteria criteria) {
        turnoverTacticDetailService.deleteByTurnoverCode(criteria.getTurnoverCode());
        deleteById(criteria.getTurnoverMasterId());
    }
}
