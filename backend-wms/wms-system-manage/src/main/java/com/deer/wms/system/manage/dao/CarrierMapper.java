package com.deer.wms.system.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.carrier.Carrier;
import com.deer.wms.system.manage.model.carrier.CarrierCriteria;
import com.deer.wms.system.manage.model.carrier.CarrierDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CarrierMapper extends Mapper<Carrier> {

    List<CarrierDto> findList(CarrierCriteria criteria);

    List<Carrier> findByCode(@Param("carrierCode") String carrierCode);

    List<Carrier> findByName(@Param("carrierCode") String carrierName);
}