package com.deer.wms.ASN.manage.service.impl;

import com.deer.wms.ASN.manage.dao.RequestBuyDetailFileMapper;
import com.deer.wms.ASN.manage.model.file.RequestBuyDetailFile;
import com.deer.wms.ASN.manage.model.file.RequestBuyDetailFileCriteria;
import com.deer.wms.ASN.manage.model.file.RequestBuyDetailFileDto;
import com.deer.wms.ASN.manage.service.RequestBuyDetailFileService;

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
public class RequestBuyDetailFileServiceImpl extends AbstractService<RequestBuyDetailFile, Integer> implements RequestBuyDetailFileService {

    @Autowired
    private RequestBuyDetailFileMapper requestBuyDetailFileMapper;

    @Override
    public void run(RequestBuyDetailFile requestBuyDetailFile) {
        super.save(requestBuyDetailFile);

    }

    @Override
    public List<RequestBuyDetailFileDto> findList(RequestBuyDetailFileCriteria criteria) {
        return requestBuyDetailFileMapper.findList(criteria);
    }
}
