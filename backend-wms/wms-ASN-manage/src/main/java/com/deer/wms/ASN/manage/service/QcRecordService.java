package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.qc.QcRecord;
import com.deer.wms.ASN.manage.model.qc.QcRecordCriteria;
import com.deer.wms.ASN.manage.model.qc.QcRecordDto;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2020/01/15.
 */
public interface QcRecordService extends Service<QcRecord, Integer> {

    List<QcRecordDto> findList(QcRecordCriteria criteria);

    void createFile(String detailNo, CurrentUser currentUser);

    void deleteByDetailNo(String detailNo);

}
