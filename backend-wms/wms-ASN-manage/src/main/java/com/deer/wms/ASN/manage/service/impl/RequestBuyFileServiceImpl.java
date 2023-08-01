package com.deer.wms.ASN.manage.service.impl;

import com.deer.wms.ASN.manage.dao.RequestBuyFileMapper;
import com.deer.wms.ASN.manage.model.file.RequestBuyFile;
import com.deer.wms.ASN.manage.model.file.RequestBuyFileCriteria;
import com.deer.wms.ASN.manage.service.RequestBuyFileService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2020/01/05.
 */
@Service
@Transactional
public class RequestBuyFileServiceImpl extends AbstractService<RequestBuyFile, Integer> implements RequestBuyFileService {

    @Autowired
    private RequestBuyFileMapper requestBuyFileMapper;

    @Override
    public List<RequestBuyFile> findList(RequestBuyFileCriteria criteria) {
        return requestBuyFileMapper.findList(criteria);
    }
}
