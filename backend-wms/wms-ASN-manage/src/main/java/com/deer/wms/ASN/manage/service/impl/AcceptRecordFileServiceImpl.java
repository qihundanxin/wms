package com.deer.wms.ASN.manage.service.impl;

import com.deer.wms.ASN.manage.dao.AcceptRecordFileMapper;
import com.deer.wms.ASN.manage.model.in.AcceptRecordFile;
import com.deer.wms.ASN.manage.model.in.AcceptRecordFileCriteria;
import com.deer.wms.ASN.manage.service.AcceptRecordFileService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2020/01/09.
 */
@Service
@Transactional
public class AcceptRecordFileServiceImpl extends AbstractService<AcceptRecordFile, Integer> implements AcceptRecordFileService {

    @Autowired
    private AcceptRecordFileMapper acceptRecordFileMapper;

    @Override
    public List<AcceptRecordFile> findList(AcceptRecordFileCriteria criteria) {
        return acceptRecordFileMapper.findList(criteria);
    }
}
