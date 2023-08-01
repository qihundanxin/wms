package com.deer.wms.system.manage.service;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.system.manage.model.customer.Customer;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.system.manage.model.customer.CustomerCriteria;
import com.deer.wms.system.manage.model.customer.CustomerDto;

import java.util.List;

/**
 * Created by guo on 2019/11/13.
 */
public interface CustomerService extends Service<Customer, Integer> {

    List<CustomerDto> findList(CustomerCriteria criteria);

    List<CustomerDto> findByCustomerCode(String customerCode);

    List<CustomerDto> findByCustomerName(String customerName);

    public Result createCustomer(CustomerDto customer, CurrentUser currentUser);

}
