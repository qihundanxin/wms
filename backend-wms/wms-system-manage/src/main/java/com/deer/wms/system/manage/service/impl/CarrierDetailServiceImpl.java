package com.deer.wms.system.manage.service.impl;

import com.deer.wms.system.manage.dao.CarrierDetailMapper;
import com.deer.wms.system.manage.model.carrier.CarrierDetail;
import com.deer.wms.system.manage.model.carrier.CarrierDetailCriteria;
import com.deer.wms.system.manage.service.CarrierDetailService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2020/01/06.
 */
@Service
@Transactional
public class CarrierDetailServiceImpl extends AbstractService<CarrierDetail, Integer> implements CarrierDetailService {

    @Autowired
    private CarrierDetailMapper carrierDetailMapper;

    @Override
    public List<CarrierDetail> findList(CarrierDetailCriteria criteria) {
        return carrierDetailMapper.findList(criteria);
    }
}
