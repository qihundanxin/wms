package com.deer.wms.ware.task.service.impl;

import com.deer.wms.ware.task.dao.AllotDetailMapper;
import com.deer.wms.ware.task.model.Allot.AllotDetail;
import com.deer.wms.ware.task.model.Allot.AllotDetailCriteria;
import com.deer.wms.ware.task.service.AllotDetailService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ll on 2020/03/04.
 */
@Service
@Transactional
public class AllotDetailServiceImpl extends AbstractService<AllotDetail, Integer> implements AllotDetailService {

    @Autowired
    private AllotDetailMapper allotDetailMapper;

    @Override
    public List<AllotDetail> findList(AllotDetailCriteria criteria) {
        return allotDetailMapper.findList(criteria);
    }
}
