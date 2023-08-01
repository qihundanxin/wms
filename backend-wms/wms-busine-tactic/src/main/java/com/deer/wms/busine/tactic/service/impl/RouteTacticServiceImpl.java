package com.deer.wms.busine.tactic.service.impl;

import com.deer.wms.busine.tactic.dao.RouteTacticMapper;
import com.deer.wms.busine.tactic.model.RouteTactic;
import com.deer.wms.busine.tactic.model.RouteTacticCriteria;
import com.deer.wms.busine.tactic.service.RouteTacticService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ll on 2020/02/13.
 */
@Service
@Transactional
public class RouteTacticServiceImpl extends AbstractService<RouteTactic, Integer> implements RouteTacticService {

    @Autowired
    private RouteTacticMapper routeTacticMapper;

    @Override
    public List<RouteTactic> findList(RouteTacticCriteria criteria) {
        return routeTacticMapper.findList(criteria);
    }
}
