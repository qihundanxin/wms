package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.in.AcceptInsert;
import com.deer.wms.ASN.manage.model.in.AcceptRecord;
import com.deer.wms.ASN.manage.model.in.AcceptRecordCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by 郭靖勋 on 2019/12/18.
 */
public interface AcceptRecordService extends Service<AcceptRecord, Integer> {

    List<AcceptRecord> findList(AcceptRecordCriteria criteria);

    Integer accept(AcceptInsert acceptInsert);

    List<AcceptRecord> findDayList(AcceptRecordCriteria criteria);

    List<AcceptRecord> findCanTongbu();

    List<AcceptRecord> findUnUpList();

}
