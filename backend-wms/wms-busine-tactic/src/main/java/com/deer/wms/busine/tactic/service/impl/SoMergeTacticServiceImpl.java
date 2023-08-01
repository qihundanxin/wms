package com.deer.wms.busine.tactic.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.busine.tactic.dao.SoMergeTacticMapper;
import com.deer.wms.busine.tactic.model.SoMergeTactic;
import com.deer.wms.busine.tactic.model.SoMergeTacticCriteria;
import com.deer.wms.busine.tactic.service.SoMergeTacticService;
import com.deer.wms.project.root.core.service.AbstractService;

/**
 * Created by ll on 2020/02/04.
 */
@Service
@Transactional
public class SoMergeTacticServiceImpl extends AbstractService<SoMergeTactic, Integer> implements SoMergeTacticService {

    @Autowired
    private SoMergeTacticMapper soMergeTacticMapper;

    @Override
    public List<SoMergeTactic> findList(SoMergeTacticCriteria criteria) {
        return soMergeTacticMapper.findList(criteria);
    }
}
