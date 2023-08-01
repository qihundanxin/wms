package com.deer.wms.ASN.manage.service.impl;

import com.deer.wms.ASN.manage.dao.QcDetailFileMapper;
import com.deer.wms.ASN.manage.model.file.QcDetailFile;
import com.deer.wms.ASN.manage.model.file.QcDetailFileCriteria;
import com.deer.wms.ASN.manage.service.QcDetailFileService;

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
public class QcDetailFileServiceImpl extends AbstractService<QcDetailFile, Integer> implements QcDetailFileService {

    @Autowired
    private QcDetailFileMapper qcDetailFileMapper;

    @Override
    public List<QcDetailFile> findList(QcDetailFileCriteria criteria) {
        return qcDetailFileMapper.findList(criteria);
    }
}
