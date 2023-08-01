package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.file.RequestBuyDetailFile;
import com.deer.wms.ASN.manage.model.file.RequestBuyDetailFileCriteria;
import com.deer.wms.ASN.manage.model.file.RequestBuyDetailFileDto;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface RequestBuyDetailFileMapper extends Mapper<RequestBuyDetailFile> {
    List<RequestBuyDetailFileDto> findList(RequestBuyDetailFileCriteria criteria);
}