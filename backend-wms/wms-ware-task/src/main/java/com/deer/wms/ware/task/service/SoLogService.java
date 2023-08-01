package com.deer.wms.ware.task.service;

import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.SO.SoLogCriteria;
import com.deer.wms.ware.task.model.SO.SoMasterDto;
import com.deer.wms.ware.task.model.SoLog;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;

public interface SoLogService extends Service<SoLog, Integer> {

    Date getMaxLogDate();

    PageInfo findList(SoLogCriteria criteria);

    void insertUnique(SoLog soLog);
}
