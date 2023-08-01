package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.AcceptEx;
import com.deer.wms.ASN.manage.model.AcceptExCriteria;
import com.deer.wms.ASN.manage.model.AcceptExDto;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface AcceptExMapper extends Mapper<AcceptEx> {

    List<AcceptExDto> findList(AcceptExCriteria criteria);

    List<AcceptEx> findCanTongBu();
}