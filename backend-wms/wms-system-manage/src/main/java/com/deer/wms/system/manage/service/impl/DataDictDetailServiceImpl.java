package com.deer.wms.system.manage.service.impl;

import com.deer.wms.project.root.util.RedisUtil;
import com.deer.wms.system.manage.dao.DataDictDetailMapper;
import com.deer.wms.system.manage.model.DataDictDetail;
import com.deer.wms.system.manage.model.DataDictDetailCriteria;
import com.deer.wms.system.manage.service.DataDictDetailService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by  on 2019/11/25.
 */
@Service
@Transactional
public class DataDictDetailServiceImpl extends AbstractService<DataDictDetail, Integer> implements DataDictDetailService {

    @Autowired
    private DataDictDetailMapper dataDictDetailMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<DataDictDetail> findList(DataDictDetailCriteria criteria) {
        return dataDictDetailMapper.findList(criteria);
    }

    @Override
    public List<DataDictDetail> findBydDataDictCode(String dataDictCode) {
        return dataDictDetailMapper.findBydDataDictCode(dataDictCode);
    }
}
