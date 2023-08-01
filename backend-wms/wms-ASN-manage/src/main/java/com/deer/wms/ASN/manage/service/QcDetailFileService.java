package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.file.QcDetailFile;
import com.deer.wms.ASN.manage.model.file.QcDetailFileCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2020/01/19.
 */
public interface QcDetailFileService extends Service<QcDetailFile, Integer> {

    List<QcDetailFile> findList(QcDetailFileCriteria criteria);

}
