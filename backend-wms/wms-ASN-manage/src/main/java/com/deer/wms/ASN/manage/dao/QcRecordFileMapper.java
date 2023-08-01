package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.qc.QcRecordFile;
import com.deer.wms.ASN.manage.model.qc.QcRecordFileCriteria;
import com.deer.wms.ASN.manage.model.qc.QcRecordFileDto;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface QcRecordFileMapper extends Mapper<QcRecordFile> {

    List<QcRecordFileDto> findList(QcRecordFileCriteria criteria);
}