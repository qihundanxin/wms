package com.deer.wms.system.manage.service.impl;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.system.manage.dao.ShippingAddressMapper;
import com.deer.wms.system.manage.model.address.ShippingAddress;
import com.deer.wms.system.manage.model.address.ShippingAddressCreate;
import com.deer.wms.system.manage.model.address.ShippingAddressCriteria;
import com.deer.wms.system.manage.model.address.ShippingAddressDto;
import com.deer.wms.system.manage.service.ShippingAddressService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2019/11/13.
 */
@Service
@Transactional
public class ShippingAddressServiceImpl extends AbstractService<ShippingAddress, Integer> implements ShippingAddressService {

    @Autowired
    private ShippingAddressMapper shippingAddressMapper;

    @Autowired
    private ShippingAddressService shippingAddressService;

    @Override
    public List<ShippingAddressDto> findList(ShippingAddressCriteria criteria) {
        return shippingAddressMapper.findList(criteria);
    }

    @Override
    public List<ShippingAddressDto> findByOrganizationIdAndWareId(Integer organizationId, Integer WareId) {
        return shippingAddressMapper.findByOrganizationIdAndWareId(organizationId, WareId);
    }

    @Override
    public List<ShippingAddressDto> findDefaultByOrganizationIdAndWareId(Integer organizationId, Integer WareId) {
        return shippingAddressMapper.findDefaultByOrganizationIdAndWareId(organizationId, WareId);
    }

    @Override
    public void createShippingAddress(ShippingAddressCreate create, CurrentUser currentUser) {
        ShippingAddress shippingAddress = new ShippingAddress();
        BeanUtils.copyProperties(create, shippingAddress);

        if (create.getState() == 1) {
            Integer organizationId = create.getOrganizationId();
            Integer wareId = create.getWareId();
            List<ShippingAddressDto> list = shippingAddressService.findByOrganizationIdAndWareId(organizationId, wareId);
            for (ShippingAddress shippingAddress1 : list) {
                shippingAddress1.setState(0);
                super.update(shippingAddress1);
            }
        }
        super.save(shippingAddress);
    }

    @Override
    public ShippingAddress findByThree(Integer organizationId, Integer wareId, Integer state) {
        return shippingAddressMapper.findByThree(organizationId, wareId, state);
    }
}
