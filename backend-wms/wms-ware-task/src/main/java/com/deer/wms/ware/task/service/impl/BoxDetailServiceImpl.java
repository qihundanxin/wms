package com.deer.wms.ware.task.service.impl;

import com.deer.wms.ware.task.dao.BoxDetailMapper;
import com.deer.wms.ware.task.model.Box.BoxDetail;
import com.deer.wms.ware.task.model.Box.BoxDetailCriteria;
import com.deer.wms.ware.task.service.BoxDetailService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ll on 2020/02/15.
 */
@Service
@Transactional
public class BoxDetailServiceImpl extends AbstractService<BoxDetail, Integer> implements BoxDetailService {

    @Autowired
    private BoxDetailMapper boxDetailMapper;

    @Override
    public List<BoxDetail> findList(BoxDetailCriteria criteria) {
        return boxDetailMapper.findList(criteria);
    }
}
