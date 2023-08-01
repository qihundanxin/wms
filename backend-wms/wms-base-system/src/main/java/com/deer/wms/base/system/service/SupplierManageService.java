package com.deer.wms.base.system.service;

import com.deer.wms.base.system.model.Supplier.SupplierManage;
import com.deer.wms.base.system.model.Supplier.SupplierManageCriteria;
import com.deer.wms.base.system.model.Supplier.SupplierManageDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2019/11/21.
 */
public interface SupplierManageService extends Service<SupplierManage, Integer> {
    List<SupplierManageDto> findList(SupplierManageCriteria criteria);
    List<SupplierManageDto> hasAsn(SupplierManageCriteria criteria);

    List<SupplierManageDto> findByCode(String supplierCode);

    List<SupplierManageDto> findByName(String supplierName);

    void deleteBySupplierCode(String supplierCode);

}
