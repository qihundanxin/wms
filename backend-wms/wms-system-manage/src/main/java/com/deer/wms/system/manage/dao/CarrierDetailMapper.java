package com.deer.wms.system.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.carrier.CarrierDetail;
import com.deer.wms.system.manage.model.carrier.CarrierDetailCriteria;

import java.util.List;

public interface CarrierDetailMapper extends Mapper<CarrierDetail> {
    List<CarrierDetail> findList(CarrierDetailCriteria criteria);
}