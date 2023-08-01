package com.deer.wms.base.system.dao;

import com.deer.wms.base.system.model.SeedingCell;
import com.deer.wms.base.system.model.SeedingCellDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SeedingCellMapper extends Mapper<SeedingCell> {

    void deleteByWallCode(@Param("wallCode") String wallCode, @Param("wareId") Integer wareId);

    List<SeedingCellDto> findByWallCode(@Param("wallCode") String wallCode, @Param("wareId") Integer wareId);
}