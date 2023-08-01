package com.deer.wms.system.manage.service;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.system.manage.model.address.ReceivingAddress;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.system.manage.model.address.ReceivingAddressCreate;
import com.deer.wms.system.manage.model.address.ReceivingAddressCriteria;
import com.deer.wms.system.manage.model.address.ReceivingAddressDto;

import java.util.List;

/**
 * Created by guo on 2019/11/13.
 */
public interface ReceivingAddressService extends Service<ReceivingAddress, Integer> {

    List<ReceivingAddressDto> findList(ReceivingAddressCriteria criteria);

    List<ReceivingAddressDto> findByCustomerId(Integer customerId);

    void createReceivingAddress(ReceivingAddressCreate create, CurrentUser currentUser);
}
