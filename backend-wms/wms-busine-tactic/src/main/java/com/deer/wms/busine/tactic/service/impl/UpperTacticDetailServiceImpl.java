package com.deer.wms.busine.tactic.service.impl;

import com.deer.wms.busine.tactic.dao.UpperTacticDetailMapper;
import com.deer.wms.busine.tactic.model.Upper.UpperTacticDetail;
import com.deer.wms.busine.tactic.model.Upper.UpperTacticDetailCriteria;
import com.deer.wms.busine.tactic.model.Upper.UpperTacticDetailDto;
import com.deer.wms.busine.tactic.service.UpperTacticDetailService;

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
public class UpperTacticDetailServiceImpl extends AbstractService<UpperTacticDetail, Integer> implements UpperTacticDetailService {

    @Autowired
    private UpperTacticDetailMapper upperTacticDetailMapper;

    @Override
    public List<UpperTacticDetailDto> findList(UpperTacticDetailCriteria criteria) {
        return upperTacticDetailMapper.findList(criteria);
    }

    @Override
    public List<UpperTacticDetail> findByAreaCode(String areaCode) {
        return upperTacticDetailMapper.findByAreaCode(areaCode);
    }

    @Override
    public List<UpperTacticDetailDto> findByUpperTacticCode(String upperTacticCode) {
        return upperTacticDetailMapper.findByUpperTacticCode(upperTacticCode);
    }

}
