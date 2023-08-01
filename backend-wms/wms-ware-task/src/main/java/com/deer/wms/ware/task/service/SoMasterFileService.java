package com.deer.wms.ware.task.service;

import com.deer.wms.ware.task.model.SO.SoMasterFile;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.SO.SoMasterFileCriteria;

import java.util.List;

/**
 * Created by ll on 2020/02/03.
 */
public interface SoMasterFileService extends Service<SoMasterFile, Integer> {

    List<SoMasterFile> findList(SoMasterFileCriteria criteria);

}
