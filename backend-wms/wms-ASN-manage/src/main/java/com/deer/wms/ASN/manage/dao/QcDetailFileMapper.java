package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.file.QcDetailFile;
import com.deer.wms.ASN.manage.model.file.QcDetailFileCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface QcDetailFileMapper extends Mapper<QcDetailFile> {
    List<QcDetailFile> findList(QcDetailFileCriteria criteria);
}