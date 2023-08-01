package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.file.QcMasterFile;
import com.deer.wms.ASN.manage.model.file.QcMasterFileCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2020/01/19.
 */
public interface QcMasterFileService extends Service<QcMasterFile, Integer> {

    List<QcMasterFile> findList(QcMasterFileCriteria criteria);
}
