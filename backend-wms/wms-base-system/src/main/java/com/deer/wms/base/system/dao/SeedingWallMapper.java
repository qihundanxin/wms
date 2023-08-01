package com.deer.wms.base.system.dao;

import com.deer.wms.base.system.model.SeedingWall.SeedingWall;
import com.deer.wms.base.system.model.SeedingWall.SeedingWallCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SeedingWallMapper extends Mapper<SeedingWall> {
    List<SeedingWall> findList(SeedingWallCriteria criteria);

    SeedingWall findByCode(@Param("code") String code, @Param("wareId") Integer wareId);
}