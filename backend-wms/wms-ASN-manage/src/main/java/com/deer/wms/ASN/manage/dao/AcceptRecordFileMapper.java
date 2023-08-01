package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.in.AcceptRecordFile;
import com.deer.wms.ASN.manage.model.in.AcceptRecordFileCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface AcceptRecordFileMapper extends Mapper<AcceptRecordFile> {
    List<AcceptRecordFile> findList(AcceptRecordFileCriteria criteria);
}