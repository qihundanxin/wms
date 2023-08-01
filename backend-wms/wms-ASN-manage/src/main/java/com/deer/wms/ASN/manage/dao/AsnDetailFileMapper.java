package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.file.AsnDetailFile;
import com.deer.wms.ASN.manage.model.file.AsnDetailFileCriteria;
import com.deer.wms.ASN.manage.model.file.AsnDetailFileDto;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface AsnDetailFileMapper extends Mapper<AsnDetailFile> {

    List<AsnDetailFileDto> findList(AsnDetailFileCriteria criteria);
}