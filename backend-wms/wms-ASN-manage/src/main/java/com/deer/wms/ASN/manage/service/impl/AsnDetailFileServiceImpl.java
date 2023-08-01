package com.deer.wms.ASN.manage.service.impl;

import com.deer.wms.ASN.manage.dao.AsnDetailFileMapper;
import com.deer.wms.ASN.manage.model.file.AsnDetailFile;
import com.deer.wms.ASN.manage.model.file.AsnDetailFileCriteria;
import com.deer.wms.ASN.manage.model.file.AsnDetailFileDto;
import com.deer.wms.ASN.manage.service.AsnDetailFileService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2020/01/07.
 */
@Service
@Transactional
public class AsnDetailFileServiceImpl extends AbstractService<AsnDetailFile, Integer> implements AsnDetailFileService {

    @Autowired
    private AsnDetailFileMapper asnDetailFileMapper;

    @Override
    public List<AsnDetailFileDto> findList(AsnDetailFileCriteria criteria) {
        return asnDetailFileMapper.findList(criteria);
    }
}
