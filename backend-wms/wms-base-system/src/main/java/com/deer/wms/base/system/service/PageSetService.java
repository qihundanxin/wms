package com.deer.wms.base.system.service;

import com.deer.wms.base.system.model.PageSet.PageSet;
import com.deer.wms.base.system.model.PageSet.PageSetCriteria;
import com.deer.wms.base.system.model.PageSet.PageSetDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2019/11/27.
 */
public interface PageSetService extends Service<PageSet, Integer> {

    List<PageSetDto> findList(PageSetCriteria criteria);

}
