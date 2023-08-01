package com.deer.wms.system.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.resource.Resource;
import com.deer.wms.system.manage.model.resource.ResourceCriteria;
import com.deer.wms.system.manage.model.resource.ResourceDto;

import java.util.List;

public interface ResourceMapper extends Mapper<Resource> {
    List<ResourceDto> findList(ResourceCriteria criteria);

    List<ResourceDto> findPageList();


    List<Resource> findListByParam(ResourceCriteria criteria);
}
