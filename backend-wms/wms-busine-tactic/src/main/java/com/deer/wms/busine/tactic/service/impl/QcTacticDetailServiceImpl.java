package com.deer.wms.busine.tactic.service.impl;

import com.deer.wms.busine.tactic.dao.QcTacticDetailMapper;
import com.deer.wms.busine.tactic.model.QC.QcTacticDetail;
import com.deer.wms.busine.tactic.model.QC.QcTacticDetailCriteria;
import com.deer.wms.busine.tactic.service.QcTacticDetailService;

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
public class QcTacticDetailServiceImpl extends AbstractService<QcTacticDetail, Integer> implements QcTacticDetailService {

    @Autowired
    private QcTacticDetailMapper qcTacticDetailMapper;

    @Override
    public List<QcTacticDetail> findList(QcTacticDetailCriteria criteria) {
        return qcTacticDetailMapper.findList(criteria);
    }

    @Override
    public List<QcTacticDetail> findByQcTacticCode(String qcTacticCode) {
        return qcTacticDetailMapper.findByQcTacticCode(qcTacticCode);
    }
}
