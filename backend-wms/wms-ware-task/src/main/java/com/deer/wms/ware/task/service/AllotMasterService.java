package com.deer.wms.ware.task.service;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.ware.task.model.Allot.AllotData;
import com.deer.wms.ware.task.model.Allot.AllotMaster;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.Allot.AllotMasterCriteria;

import java.util.List;

/**
 * Created by ll on 2020/03/04.
 */
public interface AllotMasterService extends Service<AllotMaster, Integer> {

    List<AllotMaster> findList(AllotMasterCriteria criteria);

    void createAllotMaster(AllotData allotData, CurrentUser currentUser);

    void deleteByBillNo(String billNo);
    void allot(String billNo);

}
