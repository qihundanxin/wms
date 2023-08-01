package com.deer.wms.ware.task.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.PickTaskReturn;
import com.deer.wms.ware.task.model.PickTaskReturnCriteria;
import com.deer.wms.ware.task.model.PickTaskReturnDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PickTaskReturnMapper extends Mapper<PickTaskReturn> {
    List<PickTaskReturn> findByWaveMasterId(@Param("waveMasterId") Integer waveMasterId);

    List<PickTaskReturnDto> findByBoxCode(@Param("boxCode") String boxCode, @Param("wareId") Integer wareId);

    List<PickTaskReturn> findList(PickTaskReturnCriteria criteria);

    PickTaskReturn findById(@Param("id") Integer id);

    int update(PickTaskReturn pickTaskReturn);
}
