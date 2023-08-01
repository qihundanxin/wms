package com.deer.wms.base.system.service;

import com.deer.wms.base.system.model.Platform.Platform;
import com.deer.wms.base.system.model.Platform.PlatformCriteria;
import com.deer.wms.base.system.model.Platform.PlatformDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2019/11/19.
 */
public interface PlatformService extends Service<Platform, Integer> {

    List<PlatformDto> findList(PlatformCriteria criteria);

    List<Platform> findByWareId(Integer wareId);

}
