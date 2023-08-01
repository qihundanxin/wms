package com.deer.wms.system.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.customer.Customer;
import com.deer.wms.system.manage.model.customer.CustomerCriteria;
import com.deer.wms.system.manage.model.customer.CustomerDto;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerMapper extends Mapper<Customer> {

    List<CustomerDto> findList(CustomerCriteria criteria);

    List<CustomerDto> findByCustomerCode(@Param("customerCode") String customerCode);

    List<CustomerDto> findByCustomerName(@Param("customerName") String customerName);
}