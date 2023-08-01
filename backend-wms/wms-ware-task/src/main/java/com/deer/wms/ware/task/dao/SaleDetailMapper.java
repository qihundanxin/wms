package com.deer.wms.ware.task.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.Sale.SaleDetail;
import com.deer.wms.ware.task.model.Sale.SaleDetailCriteria;

import java.util.List;

public interface SaleDetailMapper extends Mapper<SaleDetail> {
    List<SaleDetail> findList(SaleDetailCriteria criteria);
}