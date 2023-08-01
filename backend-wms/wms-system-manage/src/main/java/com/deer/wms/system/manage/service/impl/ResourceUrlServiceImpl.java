package com.deer.wms.system.manage.service.impl;

import com.deer.wms.system.manage.dao.ResourceUrlMapper;
import com.deer.wms.system.manage.model.resource.ResourceUrl;
import com.deer.wms.system.manage.service.ResourceUrlService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by guo on 2018/8/11.
 */
@Service
@Transactional
public class ResourceUrlServiceImpl extends AbstractService<ResourceUrl, Integer> implements ResourceUrlService {

    @Autowired
    private ResourceUrlMapper resourceUrlMapper;

}
