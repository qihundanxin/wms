package com.deer.wms.ware.task.service;

import com.deer.wms.ware.task.model.SO.SoDetailFile;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.SO.SoDetailFileCriteria;

import java.util.List;

/**
 * Created by ll on 2020/02/03.
 */
public interface SoDetailFileService extends Service<SoDetailFile, Integer> {

    List<SoDetailFile> findList(SoDetailFileCriteria criteria);

}
