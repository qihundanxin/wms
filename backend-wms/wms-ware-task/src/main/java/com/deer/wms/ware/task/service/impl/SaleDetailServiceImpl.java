package com.deer.wms.ware.task.service.impl;

import com.deer.wms.ware.task.dao.SaleDetailMapper;
import com.deer.wms.ware.task.model.Sale.SaleDetail;
import com.deer.wms.ware.task.model.Sale.SaleDetailCriteria;
import com.deer.wms.ware.task.service.SaleDetailService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ll on 2020/03/13.
 */
@Service
@Transactional
public class SaleDetailServiceImpl extends AbstractService<SaleDetail, Integer> implements SaleDetailService {

    @Autowired
    private SaleDetailMapper saleDetailMapper;

    @Override
    public List<SaleDetail> findList(SaleDetailCriteria criteria) {
        return saleDetailMapper.findList(criteria);
    }
}
