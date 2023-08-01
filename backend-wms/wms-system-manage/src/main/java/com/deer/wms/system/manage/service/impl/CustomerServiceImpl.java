package com.deer.wms.system.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.system.manage.dao.CustomerMapper;
import com.deer.wms.system.manage.model.customer.Customer;
import com.deer.wms.system.manage.model.customer.CustomerCriteria;
import com.deer.wms.system.manage.model.customer.CustomerDto;
import com.deer.wms.system.manage.service.CustomerService;

/**
 * Created by guo on 2019/11/13.
 */
@Service
@Transactional
public class CustomerServiceImpl extends AbstractService<Customer, Integer> implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<CustomerDto> findList(CustomerCriteria criteria) {
        return customerMapper.findList(criteria);
    }

    @Override
    public List<CustomerDto> findByCustomerCode(String customerCode) {
        return customerMapper.findByCustomerCode(customerCode);
    }

    @Override
    public List<CustomerDto> findByCustomerName(String customerName) {
        return customerMapper.findByCustomerName(customerName);
    }

    @Override
    public Result createCustomer(CustomerDto customer, CurrentUser currentUser) {
        /*
         * 新建客户
         */
        List<CustomerDto> list = this.findByCustomerCode(customer.getCustomerCode());
        List<CustomerDto> list1 = this.findByCustomerName(customer.getCustomerName());
        if (list.size() == 0 && list1.size() == 0) {
            save(customer);
            return ResultGenerator.genSuccessResult();
        } else if (list.size() == 0 && list1.size() != 0) {
            throw new ServiceException(CommonCode.CUSTOMER_EXISTS, "此客户名称已存在");
        } else {
            throw new ServiceException(CommonCode.CUSTOMER_CODE_EXISTS, "此客户编码已存在");

        }
    }
}
