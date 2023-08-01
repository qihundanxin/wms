package com.deer.wms.inventory.service.impl;

import com.deer.wms.inventory.dao.ReasonMapper;
import com.deer.wms.inventory.model.Reason;
import com.deer.wms.inventory.model.ReasonCriteria;
import com.deer.wms.inventory.service.ReasonService;

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
public class ReasonServiceImpl extends AbstractService<Reason, Integer> implements ReasonService {

    @Autowired
    private ReasonMapper reasonMapper;

    @Override
    public List<Reason> findList(ReasonCriteria criteria) {
        return reasonMapper.findList(criteria);
    }

}
