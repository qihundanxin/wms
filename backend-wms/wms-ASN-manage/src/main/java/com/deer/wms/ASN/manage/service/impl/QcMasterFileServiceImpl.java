package com.deer.wms.ASN.manage.service.impl;

import com.deer.wms.ASN.manage.dao.QcMasterFileMapper;
import com.deer.wms.ASN.manage.model.file.QcMasterFile;
import com.deer.wms.ASN.manage.model.file.QcMasterFileCriteria;
import com.deer.wms.ASN.manage.service.QcMasterFileService;

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
public class QcMasterFileServiceImpl extends AbstractService<QcMasterFile, Integer> implements QcMasterFileService {

    @Autowired
    private QcMasterFileMapper qcMasterFileMapper;

    @Override
    public List<QcMasterFile> findList(QcMasterFileCriteria criteria) {
        return qcMasterFileMapper.findList(criteria);
    }
}
