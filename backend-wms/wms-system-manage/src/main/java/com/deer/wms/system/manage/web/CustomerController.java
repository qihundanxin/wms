package com.deer.wms.system.manage.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.constant.CacheKey;
import com.deer.wms.project.root.constant.CacheTime;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.CacheTool;
import com.deer.wms.system.manage.model.customer.Customer;
import com.deer.wms.system.manage.model.customer.CustomerCriteria;
import com.deer.wms.system.manage.model.customer.CustomerDto;
import com.deer.wms.system.manage.service.CustomerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 客户api信息
 * 
 * Created by guo on 2019/11/13.
 */
@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CacheTool cache;

    /**
     * 新增客户收件地址
     */
    @PostMapping("/insert")
    @Authority("customers_insert")
    public Result add(@RequestBody CustomerDto customer, @User CurrentUser currentUser) {
        customerService.createCustomer(customer, currentUser);
        cache.del(CacheKey.ALL_CUSTOMER);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("customers_delete")
    public Result delete(Integer id) {
        customerService.deleteById(id);
        cache.del(CacheKey.ALL_CUSTOMER);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("customers_update")
    public Result update(@RequestBody Customer customer) {
        List<CustomerDto> list = customerService.findByCustomerCode(customer.getCustomerCode());
        if (list.size() == 0) {
            customerService.update(customer);
            cache.del(CacheKey.ALL_CUSTOMER);
        } else if (list.size() == 1) {
            if (list.get(0).getCustomerId().equals(customer.getCustomerId())) {
                List<CustomerDto> list1 = customerService.findByCustomerName(customer.getCustomerName());
                if (list1.size() == 0) {
                    customerService.update(customer);
                    cache.del(CacheKey.ALL_CUSTOMER);
                } else if (list1.size() == 1 && list1.get(0).getCustomerId().equals(customer.getCustomerId())) {
                    customerService.update(customer);
                    cache.del(CacheKey.ALL_CUSTOMER);
                } else {
                    throw new ServiceException(CommonCode.CUSTOMER_EXISTS, "此客户已存在");
                }

            } else if (!list.get(0).getCustomerId().equals(customer.getCustomerId())) {

                throw new ServiceException(CommonCode.CUSTOMER_CODE_EXISTS, "此客户编码已存在");
            }
        }

        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("customers_detail")
    public Result detail(Integer id) {
        Customer customer = customerService.findById(id);
        return ResultGenerator.genSuccessResult(customer);
    }

    /**
     * 客户信息
     */
    @GetMapping("/list")
    @Authority("customers_list")
    public Result list(CustomerCriteria criteria, @User CurrentUser currentUser) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<CustomerDto> list = customerService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 客户信息
     */
    @GetMapping("/allList")
    @Authority("customers_allList")
    public Result allList(CustomerCriteria criteria, @User CurrentUser currentUser) {
        List<CustomerDto> list = cache.getObject(CacheKey.ALL_CUSTOMER, null);
        if (list == null || list.size() == 0) {
            list = customerService.findList(criteria);
            cache.setObject(CacheKey.ALL_CUSTOMER, null, list, CacheTime.ALL_CUSTOMER);
        }

        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
