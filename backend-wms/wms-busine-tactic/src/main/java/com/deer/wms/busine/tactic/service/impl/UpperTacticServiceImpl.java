package com.deer.wms.busine.tactic.service.impl;

import com.deer.wms.busine.tactic.dao.UpperTacticMapper;
import com.deer.wms.busine.tactic.model.Upper.UpperTactic;
import com.deer.wms.busine.tactic.model.Upper.UpperTacticCriteria;
import com.deer.wms.busine.tactic.model.Upper.UpperTacticDto;
import com.deer.wms.busine.tactic.service.UpperTacticService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2019/11/28.
 */
@Service
@Transactional
public class UpperTacticServiceImpl extends AbstractService<UpperTactic, Integer> implements UpperTacticService {

    @Autowired
    private UpperTacticMapper upperTacticMapper;

    @Override
    public List<UpperTacticDto> findList(UpperTacticCriteria criteria) {
        return upperTacticMapper.findList(criteria);
    }

    @Override
    public List<UpperTactic> findByWareId(Integer wareId) {
        return upperTacticMapper.findByWareId(wareId);
    }

    @Override
    public List<UpperTactic> findByUpperTacticCode(String upperTacticCode) {
        return upperTacticMapper.findByUpperTacticCode(upperTacticCode);
    }

    @Override
    public List<UpperTactic> findByOrganizationAndWare(Integer organizationId, Integer wareId) {
        return upperTacticMapper.findByOrganizationAndWare(organizationId, wareId);
    }

}
