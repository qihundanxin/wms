package com.deer.wms.busine.tactic.service.impl;

import com.deer.wms.busine.tactic.dao.WaveTacticDianshangMapper;
import com.deer.wms.busine.tactic.model.WaveTacticDianshang;
import com.deer.wms.busine.tactic.model.WaveTacticDianshangCriteria;
import com.deer.wms.busine.tactic.model.WaveTacticDianshangDto;
import com.deer.wms.busine.tactic.service.WaveTacticDianshangService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by  on 2020/11/07.
 */
@Service
@Transactional
public class WaveTacticDianshangServiceImpl extends AbstractService<WaveTacticDianshang, Integer> implements WaveTacticDianshangService {

    @Autowired
    private WaveTacticDianshangMapper waveTacticDianshangMapper;

    @Override
    public List<WaveTacticDianshangDto> findList(WaveTacticDianshangCriteria criteria) {
        return waveTacticDianshangMapper.findList(criteria);
    }
}
