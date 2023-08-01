package com.deer.wms.ware.task.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.SO.SoDetailFile;
import com.deer.wms.ware.task.model.SO.SoDetailFileCriteria;

import java.util.List;

public interface SoDetailFileMapper extends Mapper<SoDetailFile> {

    List<SoDetailFile> findList(SoDetailFileCriteria criteria);
}