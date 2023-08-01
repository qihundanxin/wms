package com.deer.wms.busine.tactic.service.impl;

import com.deer.wms.busine.tactic.dao.PickTacticMapper;
import com.deer.wms.busine.tactic.model.pickTactic.PickTactic;
import com.deer.wms.busine.tactic.model.pickTactic.PickTacticCriteria;
import com.deer.wms.busine.tactic.service.PickTacticService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ll on 2020/02/02.
 */
@Service
@Transactional
public class PickTacticServiceImpl extends AbstractService<PickTactic, Integer> implements PickTacticService {

    @Autowired
    private PickTacticMapper pickTacticMapper;

    @Override
    public List<PickTactic> findList(PickTacticCriteria criteria) {
        return pickTacticMapper.findList(criteria);
    }

    @Override
    public List<PickTactic> verify(PickTacticCriteria criteria) {
        return pickTacticMapper.verify(criteria);
    }
}
