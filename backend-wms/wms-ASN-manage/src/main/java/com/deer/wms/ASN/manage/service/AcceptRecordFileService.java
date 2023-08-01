package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.in.AcceptRecordFile;
import com.deer.wms.ASN.manage.model.in.AcceptRecordFileCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2020/01/09.
 */
public interface AcceptRecordFileService extends Service<AcceptRecordFile, Integer> {

    List<AcceptRecordFile> findList(AcceptRecordFileCriteria criteria);

}
