package com.deer.wms.ASN.manage.service.impl;

import com.deer.wms.ASN.manage.dao.AsnMasterFileMapper;
import com.deer.wms.ASN.manage.model.file.AsnMasterFile;
import com.deer.wms.ASN.manage.model.file.AsnMasterFileCriteria;
import com.deer.wms.ASN.manage.model.file.AsnMasterFileDto;
import com.deer.wms.ASN.manage.service.AsnMasterFileService;

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
public class AsnMasterFileServiceImpl extends AbstractService<AsnMasterFile, Integer> implements AsnMasterFileService {

    @Autowired
    private AsnMasterFileMapper asnMasterFileMapper;

    @Override
    public List<AsnMasterFileDto> findList(AsnMasterFileCriteria criteria) {
        return asnMasterFileMapper.findList(criteria);
    }
}
