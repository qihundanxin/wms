package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.file.RequestBuyDetailFile;
import com.deer.wms.ASN.manage.model.file.RequestBuyDetailFileCriteria;
import com.deer.wms.ASN.manage.model.file.RequestBuyDetailFileDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2020/01/05.
 */
public interface RequestBuyDetailFileService extends Service<RequestBuyDetailFile, Integer> {

    void run(RequestBuyDetailFile requestBuyDetailFile);

    List<RequestBuyDetailFileDto> findList(RequestBuyDetailFileCriteria criteria);

}
