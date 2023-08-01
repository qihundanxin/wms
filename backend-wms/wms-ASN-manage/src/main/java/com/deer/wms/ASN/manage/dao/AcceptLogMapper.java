package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.AcceptLog;
import com.deer.wms.ASN.manage.model.in.AcceptLogCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.Date;
import java.util.List;

public interface AcceptLogMapper extends Mapper<AcceptLog> {

    Date getLogMaxDate();

    List<AcceptLog> findList(AcceptLogCriteria acceptLogCriteria);

    void insertUnique(AcceptLog acceptLog);

}