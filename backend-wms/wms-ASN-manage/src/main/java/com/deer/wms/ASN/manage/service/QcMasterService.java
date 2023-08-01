package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.qc.QcData;
import com.deer.wms.ASN.manage.model.qc.QcMaster;
import com.deer.wms.ASN.manage.model.qc.QcMasterCriteria;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2020/01/15.
 */
public interface QcMasterService extends Service<QcMaster, Integer> {

    void createQc(QcData qcData, CurrentUser currentUser);

    void deleteByBillNo(String billNo);

    List<QcMaster> findList(QcMasterCriteria criteria);

    void createFile(String billNo, CurrentUser currentUser);

}
