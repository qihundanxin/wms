package com.deer.wms.ware.task.service;

import com.deer.wms.ware.task.model.Load.LoadDetail;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.Load.LoadDetailCriteria;
import com.deer.wms.ware.task.model.Load.LoadDetailDto;

import java.util.List;

/**
 * Created by ll on 2020/02/17.
 */
public interface LoadDetailService extends Service<LoadDetail, Integer> {

    List<LoadDetailDto> findList(LoadDetailCriteria criteria);
}
