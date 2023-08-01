package com.deer.wms.base.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.base.system.dao.SupplierManageMapper;
import com.deer.wms.base.system.model.Supplier.SupplierManage;
import com.deer.wms.base.system.model.Supplier.SupplierManageCriteria;
import com.deer.wms.base.system.model.Supplier.SupplierManageDto;
import com.deer.wms.base.system.service.SupplierManageService;
import com.deer.wms.project.root.constant.CacheKey;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.util.CacheTool;

/**
 * Created by guo on 2019/11/21.
 */
@Service
@Transactional
public class SupplierManageServiceImpl extends AbstractService<SupplierManage, Integer> implements SupplierManageService {

    @Autowired
    private SupplierManageMapper supplierManageMapper;

    @Autowired
    private CacheTool cache;

    @Override
    public List<SupplierManageDto> findByCode(String supplierCode) {
        return supplierManageMapper.findByCode(supplierCode);
    }

    @Override
    public List<SupplierManageDto> findByName(String supplierName) {
        return supplierManageMapper.findByName(supplierName);
    }

    @Override
    public List<SupplierManageDto> findList(SupplierManageCriteria criteria) {
        return supplierManageMapper.findList(criteria);
    }

    @Override
    public List<SupplierManageDto> hasAsn(SupplierManageCriteria criteria) {
        return supplierManageMapper.hasAsn(criteria);
    }

    @Override
    public void deleteBySupplierCode(String supplierCode) {
        supplierManageMapper.deleteBySupplierCode(supplierCode);
    }

    @Override
    public void save(SupplierManage supplierManage) {
        super.save(supplierManage);
        cache.del(CacheKey.ALL_SUPPLIER_MANAGE);
    }

    @Override
    public void deleteById(Integer supplierManageId) {
        super.deleteById(supplierManageId);
        cache.del(CacheKey.ALL_SUPPLIER_MANAGE);
    }

    @Override
    public int update(SupplierManage supplierManage) {
        int update = super.update(supplierManage);
        cache.del(CacheKey.ALL_SUPPLIER_MANAGE);
        return update;
    }
}
