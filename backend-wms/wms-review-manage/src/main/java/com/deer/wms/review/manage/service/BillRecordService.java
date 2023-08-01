package com.deer.wms.review.manage.service;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.review.manage.model.BillRecordCriteria;

import java.util.List;

/**
 * Created by ll on 2020/03/05.
 */
public interface BillRecordService extends Service<BillRecord, Integer> {

    List<BillRecord> findList(BillRecordCriteria criteria);

    void updateBillRecord(BillRecord billRecord, CurrentUser currentUser);

    void createBillRecord(BillRecord billRecord, CurrentUser currentUser);

    BillRecord findByBillNoAndType(String billNo, Integer recordType);

}
