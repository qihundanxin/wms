package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.file.RequestBuyFile;
import com.deer.wms.ASN.manage.model.file.RequestBuyFileCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2020/01/05.
 */
public interface RequestBuyFileService extends Service<RequestBuyFile, Integer> {

    List<RequestBuyFile> findList(RequestBuyFileCriteria criteria);

}
