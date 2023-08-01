package com.deer.wms.base.system.service;

import com.deer.wms.base.system.model.SeedingWall.SeedingWall;
import com.deer.wms.base.system.model.SeedingWall.SeedingWallCriteria;
import com.deer.wms.project.root.core.service.Service;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ll on 2020/02/17.
 */
public interface SeedingWallService extends Service<SeedingWall, Integer> {
    List<SeedingWall> findList(SeedingWallCriteria criteria);

    SeedingWall findByCode(@Param("code") String boxCode, @Param("wareId") Integer wareId);

}
