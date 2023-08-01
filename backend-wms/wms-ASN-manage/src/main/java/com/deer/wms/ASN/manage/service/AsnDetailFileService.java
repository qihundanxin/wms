package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.file.AsnDetailFile;
import com.deer.wms.ASN.manage.model.file.AsnDetailFileCriteria;
import com.deer.wms.ASN.manage.model.file.AsnDetailFileDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2020/01/07.
 */
public interface AsnDetailFileService extends Service<AsnDetailFile, Integer> {

    List<AsnDetailFileDto> findList(AsnDetailFileCriteria criteria);

}
