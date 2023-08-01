package com.deer.wms.base.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.base.system.dao.PlatformMapper;
import com.deer.wms.base.system.model.Platform.Platform;
import com.deer.wms.base.system.model.Platform.PlatformCriteria;
import com.deer.wms.base.system.model.Platform.PlatformDto;
import com.deer.wms.base.system.service.PlatformService;
import com.deer.wms.project.root.constant.CacheKey;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.util.CacheTool;

/**
 * Created by guo on 2019/11/19.
 */
@Service
@Transactional
public class PlatformServiceImpl extends AbstractService<Platform, Integer> implements PlatformService {

    @Autowired
    private PlatformMapper platformMapper;

    @Autowired
    private CacheTool cache;

    @Override
    public List<PlatformDto> findList(PlatformCriteria criteria) {
        return platformMapper.findList(criteria);
    }

    @Override
    public List<Platform> findByWareId(Integer wareId) {
        return platformMapper.findByWareId(wareId);
    }

    @Override
    public int update(Platform platform) {
        int update = super.update(platform);
        cache.del(CacheKey.PLATFORM, platform.getWareId().toString());
        return update;
    }

}
