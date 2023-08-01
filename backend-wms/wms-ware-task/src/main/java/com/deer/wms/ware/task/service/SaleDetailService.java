package com.deer.wms.ware.task.service;

import com.deer.wms.ware.task.model.Sale.SaleDetail;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.Sale.SaleDetailCriteria;

import java.util.List;

/**
 * Created by ll on 2020/03/13.
 */
public interface SaleDetailService extends Service<SaleDetail, Integer> {
    List<SaleDetail> findList(SaleDetailCriteria criteria);

}
