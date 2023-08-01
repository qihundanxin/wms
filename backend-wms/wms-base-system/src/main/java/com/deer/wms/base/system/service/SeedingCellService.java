package com.deer.wms.base.system.service;

import com.deer.wms.base.system.model.SeedingCell;
import com.deer.wms.base.system.model.SeedingCellDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by  on 2021/03/06.
 */
public interface SeedingCellService extends Service<SeedingCell, Integer> {

    void deleteByWallCode(String wallCode, Integer wareId);

    List<SeedingCellDto> findByWallCode(String wallCode, Integer wareId);

}
