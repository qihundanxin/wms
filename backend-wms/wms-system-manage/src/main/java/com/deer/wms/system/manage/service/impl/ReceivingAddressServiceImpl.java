package com.deer.wms.system.manage.service.impl;

import com.deer.wms.system.manage.dao.ReceivingAddressMapper;
import com.deer.wms.system.manage.service.ReceivingAddressService;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.system.manage.model.address.ReceivingAddress;
import com.deer.wms.system.manage.model.address.ReceivingAddressCreate;
import com.deer.wms.system.manage.model.address.ReceivingAddressCriteria;
import com.deer.wms.system.manage.model.address.ReceivingAddressDto;

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
public class ReceivingAddressServiceImpl extends AbstractService<ReceivingAddress, Integer> implements ReceivingAddressService {

    @Autowired
    private ReceivingAddressMapper receivingAddressMapper;

    @Autowired
    private ReceivingAddressService receivingAddressService;

    @Override
    public List<ReceivingAddressDto> findList(ReceivingAddressCriteria criteria) {
        return receivingAddressMapper.findList(criteria);
    }

    @Override
    public List<ReceivingAddressDto> findByCustomerId(Integer customerId) {
        return receivingAddressMapper.findByCustomerId(customerId);
    }

    @Override
    public void createReceivingAddress(ReceivingAddressCreate create, CurrentUser currentUser) {
        ReceivingAddress receivingAddress = new ReceivingAddress();
        BeanUtils.copyProperties(create, receivingAddress);

        if (create.getState() == 1) {
            Integer customerId = create.getCustomerId();
            List<ReceivingAddressDto> list = receivingAddressService.findByCustomerId(customerId);
            for (ReceivingAddress receivingAddress1 : list) {
                receivingAddress1.setState(0);
                super.update(receivingAddress1);
            }
        }
        super.save(receivingAddress);
    }

}
