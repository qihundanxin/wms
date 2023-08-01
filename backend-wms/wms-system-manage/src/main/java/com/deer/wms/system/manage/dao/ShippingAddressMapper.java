package com.deer.wms.system.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.address.ShippingAddress;
import com.deer.wms.system.manage.model.address.ShippingAddressCriteria;
import com.deer.wms.system.manage.model.address.ShippingAddressDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingAddressMapper extends Mapper<ShippingAddress> {
    List<ShippingAddressDto> findList(ShippingAddressCriteria criteria);

    List<ShippingAddressDto> findByOrganizationIdAndWareId(@Param("organizationId") Integer organizationId, @Param("wareId") Integer wareId);

    List<ShippingAddressDto> findDefaultByOrganizationIdAndWareId(@Param("organizationId") Integer organizationId, @Param("wareId") Integer wareId);

    ShippingAddress findByThree(@Param("organizationId") Integer organizationId, @Param("wareId") Integer wareId, @Param("state") Integer state);
}