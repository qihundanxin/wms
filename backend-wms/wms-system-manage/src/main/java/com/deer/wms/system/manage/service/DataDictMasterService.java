package com.deer.wms.system.manage.service;

import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.system.manage.model.DataDictMaster;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by  on 2019/11/25.
 */
public interface DataDictMasterService extends Service<DataDictMaster, Integer> {

    Result createDataDictMaster(DataDictMaster dataDictMaster);

    List<DataDictMaster> findByCode(String dataDictCode);

    List<DataDictMaster> findByName(String dataDictName);

}
