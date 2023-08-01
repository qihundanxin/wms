package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.qc.QcRecordFile;
import com.deer.wms.ASN.manage.model.qc.QcRecordFileCriteria;
import com.deer.wms.ASN.manage.model.qc.QcRecordFileDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2020/01/19.
 */
public interface QcRecordFileService extends Service<QcRecordFile, Integer> {

    List<QcRecordFileDto> findList(QcRecordFileCriteria criteria);

}
