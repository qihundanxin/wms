package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.file.QcMasterFile;
import com.deer.wms.ASN.manage.model.file.QcMasterFileCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface QcMasterFileMapper extends Mapper<QcMasterFile> {

    List<QcMasterFile> findList(QcMasterFileCriteria criteria);
}