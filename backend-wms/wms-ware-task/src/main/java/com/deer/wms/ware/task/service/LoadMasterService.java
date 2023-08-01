package com.deer.wms.ware.task.service;

import com.deer.wms.ware.task.model.Load.LoadMaster;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.Load.LoadMasterCriteria;

import java.util.List;

/**
 * Created by ll on 2020/02/17.
 */
public interface LoadMasterService extends Service<LoadMaster, Integer> {

    List<LoadMaster> findList(LoadMasterCriteria criteria);
}
