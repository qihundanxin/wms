package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.file.AsnMasterFile;
import com.deer.wms.ASN.manage.model.file.AsnMasterFileCriteria;
import com.deer.wms.ASN.manage.model.file.AsnMasterFileDto;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface AsnMasterFileMapper extends Mapper<AsnMasterFile> {

    List<AsnMasterFileDto> findList(AsnMasterFileCriteria criteria);
}