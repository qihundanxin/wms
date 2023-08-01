package com.deer.wms.system.manage.service;

import com.deer.wms.system.manage.model.carrier.CarrierDetail;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.system.manage.model.carrier.CarrierDetailCriteria;

import java.util.List;

/**
 * Created by guo on 2020/01/06.
 */
public interface CarrierDetailService extends Service<CarrierDetail, Integer> {
    List<CarrierDetail> findList(CarrierDetailCriteria criteria);

}
