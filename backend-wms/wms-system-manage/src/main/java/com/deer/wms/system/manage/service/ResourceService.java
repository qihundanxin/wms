package com.deer.wms.system.manage.service;

import com.deer.wms.system.manage.model.resource.Resource;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.system.manage.model.resource.ResourceCriteria;
import com.deer.wms.system.manage.model.resource.ResourceDto;

import java.util.List;

/**
 * Created by guo on 2018/8/11.
 */
public interface ResourceService extends Service<Resource, Integer> {

    List<ResourceDto> findList(ResourceCriteria criteria);

    List<ResourceDto> findPageList();

    boolean updateAllByParentId(Integer resourceId,ResourceDto resourceDto);

    List<Resource> findListByParam(ResourceCriteria criteria);

    boolean deleteAllById(Integer id);
}
