package com.deer.wms.busine.tactic.service.impl;

import com.deer.wms.busine.tactic.dao.TurnoverTacticDetailMapper;
import com.deer.wms.busine.tactic.model.TurnoverTacticDetail;
import com.deer.wms.busine.tactic.model.TurnoverTacticDetailCriteria;
import com.deer.wms.busine.tactic.service.TurnoverTacticDetailService;

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
public class TurnoverTacticDetailServiceImpl extends AbstractService<TurnoverTacticDetail, Integer> implements TurnoverTacticDetailService {

    @Autowired
    private TurnoverTacticDetailMapper turnoverTacticDetailMapper;

    @Override
    public List<TurnoverTacticDetail> findList(TurnoverTacticDetailCriteria criteria) {
        return turnoverTacticDetailMapper.findList(criteria);
    }

    @Override
    public void deleteByTurnoverCode(String turnoverCode) {
        turnoverTacticDetailMapper.deleteByTurnoverCode(turnoverCode);
    }
}
