package com.deer.wms.ware.task.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.Load.LoadMaster;
import com.deer.wms.ware.task.model.Load.LoadMasterCriteria;

import java.util.List;

public interface LoadMasterMapper extends Mapper<LoadMaster> {
    List<LoadMaster> findList(LoadMasterCriteria criteria);
}