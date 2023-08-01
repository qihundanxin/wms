package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.in.AcceptRecord;
import com.deer.wms.ASN.manage.model.in.AcceptRecordCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface AcceptRecordMapper extends Mapper<AcceptRecord> {

    List<AcceptRecord> findList(AcceptRecordCriteria criteria);

    List<AcceptRecord> findCanTongbu();

    List<AcceptRecord> findDayList(AcceptRecordCriteria criteria);

    List<AcceptRecord> findUnUpList();
}