package com.deer.wms.ware.task.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.SO.SoMasterFile;
import com.deer.wms.ware.task.model.SO.SoMasterFileCriteria;

import java.util.List;

public interface SoMasterFileMapper extends Mapper<SoMasterFile> {

    List<SoMasterFile> findList(SoMasterFileCriteria criteria);
}