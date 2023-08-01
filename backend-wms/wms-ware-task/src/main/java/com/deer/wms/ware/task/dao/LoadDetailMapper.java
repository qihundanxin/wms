package com.deer.wms.ware.task.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.Load.LoadDetail;
import com.deer.wms.ware.task.model.Load.LoadDetailCriteria;
import com.deer.wms.ware.task.model.Load.LoadDetailDto;

import java.util.List;

public interface LoadDetailMapper extends Mapper<LoadDetail> {
    List<LoadDetailDto> findList(LoadDetailCriteria criteria);
}