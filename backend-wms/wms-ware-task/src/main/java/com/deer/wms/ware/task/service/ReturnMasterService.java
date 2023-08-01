package com.deer.wms.ware.task.service;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.ware.task.model.Return.ReturnData;
import com.deer.wms.ware.task.model.Return.ReturnMaster;
import com.deer.wms.project.root.core.service.Service;

/**
 * Created by ll on 2020/03/13.
 */
public interface ReturnMasterService extends Service<ReturnMaster, Integer> {

    void create(ReturnData returnData, CurrentUser currentUser);

    void deleteByBillNo(String billNo);

}