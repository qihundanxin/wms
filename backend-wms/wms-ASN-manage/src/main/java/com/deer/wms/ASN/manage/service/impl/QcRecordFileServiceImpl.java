package com.deer.wms.ASN.manage.service.impl;

import com.deer.wms.ASN.manage.dao.QcRecordFileMapper;
import com.deer.wms.ASN.manage.model.qc.QcRecordFile;
import com.deer.wms.ASN.manage.model.qc.QcRecordFileCriteria;
import com.deer.wms.ASN.manage.model.qc.QcRecordFileDto;
import com.deer.wms.ASN.manage.service.QcRecordFileService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2020/01/19.
 */
@Service
@Transactional
public class QcRecordFileServiceImpl extends AbstractService<QcRecordFile, Integer> implements QcRecordFileService {

    @Autowired
    private QcRecordFileMapper qcRecordFileMapper;

    @Override
    public List<QcRecordFileDto> findList(QcRecordFileCriteria criteria) {
        return qcRecordFileMapper.findList(criteria);
    }
}
