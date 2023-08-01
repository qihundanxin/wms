package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.AcceptLog;
import com.deer.wms.ASN.manage.model.in.AcceptLogCriteria;
import com.deer.wms.project.root.core.service.Service;
import com.github.pagehelper.PageInfo;

import java.util.Date;

public interface AcceptLogService extends Service<AcceptLog, Integer> {

    Date getMaxLogDate();

    PageInfo findList(AcceptLogCriteria acceptLogCriteria);

    void insertUnique(AcceptLog acceptLog);
}
