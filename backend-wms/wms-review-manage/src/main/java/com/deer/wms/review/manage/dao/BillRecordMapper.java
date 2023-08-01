package com.deer.wms.review.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.model.BillRecordCriteria;

import java.util.List;

public interface BillRecordMapper extends Mapper<BillRecord> {

    List<BillRecord> findList(BillRecordCriteria criteria);

    BillRecord findByBillNoAndType(BillRecordCriteria criteria);
}