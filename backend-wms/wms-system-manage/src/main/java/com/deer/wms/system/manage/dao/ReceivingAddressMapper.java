package com.deer.wms.system.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.address.ReceivingAddress;
import com.deer.wms.system.manage.model.address.ReceivingAddressCriteria;
import com.deer.wms.system.manage.model.address.ReceivingAddressDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReceivingAddressMapper extends Mapper<ReceivingAddress> {

    List<ReceivingAddressDto> findList(ReceivingAddressCriteria criteria);

    List<ReceivingAddressDto> findByCustomerId(@Param("customerId") Integer customerId);
}