package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.file.AsnMasterFile;
import com.deer.wms.ASN.manage.model.file.AsnMasterFileCriteria;
import com.deer.wms.ASN.manage.model.file.AsnMasterFileDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2020/01/07.
 */
public interface AsnMasterFileService extends Service<AsnMasterFile, Integer> {

    List<AsnMasterFileDto> findList(AsnMasterFileCriteria criteria);

}
