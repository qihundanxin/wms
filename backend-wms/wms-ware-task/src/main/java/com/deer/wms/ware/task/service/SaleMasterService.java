package com.deer.wms.ware.task.service;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.ware.task.model.Sale.SaleData;
import com.deer.wms.ware.task.model.Sale.SaleMaster;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.Sale.SaleMasterCriteria;

import java.util.List;

/**
 * Created by ll on 2020/03/13.
 */
public interface SaleMasterService extends Service<SaleMaster, Integer> {

    void createSale(SaleData saleData, CurrentUser currentUser);

    void createSo(SaleData saleData, CurrentUser currentUser);

    void deleteByBillNo(String billNo);

    List<SaleMaster> findList(SaleMasterCriteria criteria);

}
