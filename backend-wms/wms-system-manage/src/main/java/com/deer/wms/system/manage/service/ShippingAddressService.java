package com.deer.wms.system.manage.service;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.system.manage.model.address.ShippingAddress;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.system.manage.model.address.ShippingAddressCreate;
import com.deer.wms.system.manage.model.address.ShippingAddressCriteria;
import com.deer.wms.system.manage.model.address.ShippingAddressDto;

import java.util.List;

/**
 * Created by guo on 2019/11/13.
 */
public interface ShippingAddressService extends Service<ShippingAddress, Integer> {
    List<ShippingAddressDto> findList(ShippingAddressCriteria criteria);

    List<ShippingAddressDto> findByOrganizationIdAndWareId(Integer organizationId, Integer WareId);

    List<ShippingAddressDto> findDefaultByOrganizationIdAndWareId(Integer organizationId, Integer WareId);

    void createShippingAddress(ShippingAddressCreate create, CurrentUser currentUser);

    ShippingAddress findByThree(Integer organizationId, Integer wareId, Integer state);

}
