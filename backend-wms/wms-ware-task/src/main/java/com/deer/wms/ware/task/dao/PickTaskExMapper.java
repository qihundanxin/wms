package com.deer.wms.ware.task.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.PickTaskEx;
import com.deer.wms.ware.task.model.PickTaskExCriteria;
import com.deer.wms.ware.task.model.PickTaskReturnCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PickTaskExMapper extends Mapper<PickTaskEx> {

    PickTaskEx findById(@Param("id") Integer id);

    int update(PickTaskEx pickTaskEx);

    List<PickTaskEx> findList(PickTaskExCriteria criteria);
}