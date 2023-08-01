package com.deer.wms.base.system.service.impl;

import com.deer.wms.base.system.dao.SeedingWallMapper;
import com.deer.wms.base.system.model.SeedingWall.SeedingWall;
import com.deer.wms.base.system.model.SeedingWall.SeedingWallCriteria;
import com.deer.wms.base.system.service.SeedingWallService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ll on 2020/02/17.
 */
@Service
@Transactional
public class SeedingWallServiceImpl extends AbstractService<SeedingWall, Integer> implements SeedingWallService {

    @Autowired
    private SeedingWallMapper seedingWallMapper;

    @Override
    public List<SeedingWall> findList(SeedingWallCriteria criteria) {
        return seedingWallMapper.findList(criteria);
    }

    @Override
    public SeedingWall findByCode(String code, Integer wareId) {
        return seedingWallMapper.findByCode(code, wareId);
    }
}
