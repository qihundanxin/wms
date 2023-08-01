package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.file.RequestBuyFile;
import com.deer.wms.ASN.manage.model.file.RequestBuyFileCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface RequestBuyFileMapper extends Mapper<RequestBuyFile> {
    List<RequestBuyFile> findList(RequestBuyFileCriteria criteria);
}