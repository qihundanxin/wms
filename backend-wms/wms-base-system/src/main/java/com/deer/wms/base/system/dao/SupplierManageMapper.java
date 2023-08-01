package com.deer.wms.base.system.dao;

import com.deer.wms.base.system.model.Supplier.SupplierManage;
import com.deer.wms.base.system.model.Supplier.SupplierManageCriteria;
import com.deer.wms.base.system.model.Supplier.SupplierManageDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SupplierManageMapper extends Mapper<SupplierManage> {
    List<SupplierManageDto> findList(SupplierManageCriteria criteria);
    List<SupplierManageDto> hasAsn(SupplierManageCriteria criteria);

    List<SupplierManageDto> findByCode(@Param("supplierCode") String supplierCode);

    List<SupplierManageDto> findByName(@Param("supplierName") String supplierName);

    void deleteBySupplierCode(@Param("supplierCode") String supplierCode);
}