package com.deer.wms.base.system.service.impl;

import com.deer.wms.base.system.dao.SeedingCellMapper;
import com.deer.wms.base.system.model.SeedingCell;
import com.deer.wms.base.system.model.SeedingCellDto;
import com.deer.wms.base.system.service.SeedingCellService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by  on 2021/03/06.
 */
@Service
@Transactional
public class SeedingCellServiceImpl extends AbstractService<SeedingCell, Integer> implements SeedingCellService {

    @Autowired
    private SeedingCellMapper seedingCellMapper;

    @Override
    public void deleteByWallCode(String wallCode, Integer wareId) {
        seedingCellMapper.deleteByWallCode(wallCode, wareId);
    }

    @Override
    public List<SeedingCellDto> findByWallCode(String wallCode, Integer wareId) {
        return seedingCellMapper.findByWallCode(wallCode, wareId);
    }
}
