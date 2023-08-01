package com.deer.wms.ware.task.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.Return.SoLogDto;
import com.deer.wms.ware.task.model.SO.SoLogCriteria;
import com.deer.wms.ware.task.model.SO.SoMasterDto;
import com.deer.wms.ware.task.model.SoLog;

import java.util.Date;
import java.util.List;

public interface SoLogMapper extends Mapper<SoLog> {

    Date getMaxLogDate();

    List<SoLogDto> findList(SoLogCriteria criteria);

    void insertUnique(SoLog soLog);

}