package com.deer.wms.base.system.dao;

import com.deer.wms.base.system.model.Platform.Platform;
import com.deer.wms.base.system.model.Platform.PlatformCriteria;
import com.deer.wms.base.system.model.Platform.PlatformDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlatformMapper extends Mapper<Platform> {

    List<PlatformDto> findList(PlatformCriteria criteria);

    List<Platform> findByWareId(@Param("wareId") Integer wareId);
}