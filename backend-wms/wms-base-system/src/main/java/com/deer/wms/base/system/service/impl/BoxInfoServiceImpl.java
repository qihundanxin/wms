package com.deer.wms.base.system.service.impl;

import com.deer.wms.base.system.dao.BoxInfoMapper;
import com.deer.wms.base.system.model.BoxInfo;
import com.deer.wms.base.system.model.BoxInfoCriteria;
import com.deer.wms.base.system.service.BoxInfoService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by  on 2021/03/17.
 */
@Service
@Transactional
public class BoxInfoServiceImpl extends AbstractService<BoxInfo, Integer> implements BoxInfoService {

    @Autowired
    private BoxInfoMapper boxInfoMapper;

    @Override
    public List<BoxInfo> findList(BoxInfoCriteria criteria) {
        return boxInfoMapper.findList(criteria);
    }

    @Override
    public BoxInfo findByBoxCode(String boxCode,Integer type,  Integer wareId) {
        return boxInfoMapper.findByBoxCode(boxCode,type, wareId);
    }

    @Override
    public BoxInfo findByBoxCode2(String boxCode,Integer wareId) {
        return boxInfoMapper.findByBoxCode2(boxCode, wareId);
    }
}
