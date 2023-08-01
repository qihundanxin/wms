package com.deer.wms.system.manage.service;

import com.deer.wms.system.manage.model.carrier.Carrier;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.system.manage.model.carrier.CarrierCriteria;
import com.deer.wms.system.manage.model.carrier.CarrierDto;

import java.util.List;

/**
 * Created by guo on 2019/11/13.
 */
public interface CarrierService extends Service<Carrier, Integer> {

    List<CarrierDto> findList(CarrierCriteria criteria);

    List<Carrier> findByCode(String carrierCode);

    List<Carrier> findByName(String carrierName);

}
