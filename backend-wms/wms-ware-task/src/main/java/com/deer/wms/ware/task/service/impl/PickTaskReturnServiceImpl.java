package com.deer.wms.ware.task.service.impl;

import com.deer.wms.ware.task.dao.PickTaskReturnMapper;
import com.deer.wms.ware.task.model.PickTaskReturn;
import com.deer.wms.ware.task.model.PickTaskReturnCriteria;
import com.deer.wms.ware.task.model.PickTaskReturnDto;
import com.deer.wms.ware.task.service.PickTaskReturnService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by  on 2021/03/20.
 */
@Service
@Transactional
public class PickTaskReturnServiceImpl extends AbstractService<PickTaskReturn, Integer> implements PickTaskReturnService {

    @Autowired
    private PickTaskReturnMapper pickTaskReturnMapper;

    @Override
    public List<PickTaskReturn> findByWaveMasterId(Integer waveMasterId) {
        return pickTaskReturnMapper.findByWaveMasterId(waveMasterId);
    }

    @Override
    public List<PickTaskReturn> findList(PickTaskReturnCriteria criteria) {
        return pickTaskReturnMapper.findList(criteria);
    }

    @Override
    public List<PickTaskReturnDto> findByBoxCode(String boxCode, Integer wareId) {
        return pickTaskReturnMapper.findByBoxCode(boxCode, wareId);
    }
}
