package com.deer.wms.ware.task.service;

import com.deer.wms.ware.task.model.PickTaskReturn;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.PickTaskReturnCriteria;
import com.deer.wms.ware.task.model.PickTaskReturnDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by  on 2021/03/20.
 */
public interface PickTaskReturnService extends Service<PickTaskReturn, Integer> {
    List<PickTaskReturn> findByWaveMasterId(Integer waveMasterId);

    List<PickTaskReturn> findList(PickTaskReturnCriteria criteria);

    List<PickTaskReturnDto> findByBoxCode(String boxCode, Integer wareId);

}
