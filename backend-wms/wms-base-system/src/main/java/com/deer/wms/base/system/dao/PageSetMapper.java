package com.deer.wms.base.system.dao;

import com.deer.wms.base.system.model.PageSet.PageSet;
import com.deer.wms.base.system.model.PageSet.PageSetCriteria;
import com.deer.wms.base.system.model.PageSet.PageSetDto;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface PageSetMapper extends Mapper<PageSet> {
    List<PageSetDto> findList(PageSetCriteria criteria);
}