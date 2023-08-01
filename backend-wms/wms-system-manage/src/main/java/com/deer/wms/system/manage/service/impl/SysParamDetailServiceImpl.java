package com.deer.wms.system.manage.service.impl;

import com.deer.wms.system.manage.dao.SysParamDetailMapper;
import com.deer.wms.system.manage.model.SysParamDetail;
import com.deer.wms.system.manage.model.SysParamDetailCriteria;
import com.deer.wms.system.manage.service.SysParamDetailService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2019/07/12.
 */
@Service
@Transactional
public class SysParamDetailServiceImpl extends AbstractService<SysParamDetail, Integer> implements SysParamDetailService {

    @Autowired
    private SysParamDetailMapper sysParamDetailMapper;

    @Override
    public List<SysParamDetail> findList(SysParamDetailCriteria criteria) {
        return sysParamDetailMapper.findList(criteria);
    }

    @Override
    public void deleteBySysParamId(Integer SysParamId) {
        sysParamDetailMapper.deleteBySysParamId(SysParamId);
    }
}
