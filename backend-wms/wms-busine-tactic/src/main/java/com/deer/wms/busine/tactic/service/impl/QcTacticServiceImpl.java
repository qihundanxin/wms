package com.deer.wms.busine.tactic.service.impl;

import com.deer.wms.busine.tactic.dao.QcTacticMapper;
import com.deer.wms.busine.tactic.model.QC.QcTactic;
import com.deer.wms.busine.tactic.model.QC.QcTacticCriteria;
import com.deer.wms.busine.tactic.service.QcTacticService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2020/01/15.
 */
@Service
@Transactional
public class QcTacticServiceImpl extends AbstractService<QcTactic, Integer> implements QcTacticService {

    @Autowired
    private QcTacticMapper qcTacticMapper;

    @Override
    public List<QcTactic> findList(QcTacticCriteria criteria) {
        return qcTacticMapper.findList(criteria);
    }
}
