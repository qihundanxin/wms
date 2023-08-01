package com.deer.wms.system.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.project.root.constant.CacheKey;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.util.CacheTool;
import com.deer.wms.system.manage.dao.CarrierMapper;
import com.deer.wms.system.manage.model.carrier.Carrier;
import com.deer.wms.system.manage.model.carrier.CarrierCriteria;
import com.deer.wms.system.manage.model.carrier.CarrierDto;
import com.deer.wms.system.manage.service.CarrierService;

/**
 * Created by guo on 2019/11/13.
 */
@Service
@Transactional
public class CarrierServiceImpl extends AbstractService<Carrier, Integer> implements CarrierService {

    @Autowired
    private CarrierMapper carrierMapper;

    @Autowired
    private CacheTool cache;

    @Override
    public List<CarrierDto> findList(CarrierCriteria criteria) {
        return carrierMapper.findList(criteria);
    }

    public List<Carrier> findByCode(String carrierCode) {
        return carrierMapper.findByCode(carrierCode);
    }

    public List<Carrier> findByName(String carrierCode) {
        return carrierMapper.findByName(carrierCode);
    }

    @Override
    public void save(Carrier carrier) {
        super.save(carrier);
        cache.del(CacheKey.ALL_CARRIER);
    }

    @Override
    public void deleteById(Integer carrierId) {
        super.deleteById(carrierId);
        cache.del(CacheKey.ALL_CARRIER);
    }

    @Override
    public int update(Carrier carrier) {
        int update = super.update(carrier);
        cache.del(CacheKey.ALL_CARRIER);
        return update;
    }
}
