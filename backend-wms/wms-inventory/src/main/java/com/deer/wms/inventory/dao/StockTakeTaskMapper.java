package com.deer.wms.inventory.dao;

import com.deer.wms.inventory.model.stockTake.StockTakeTask;
import com.deer.wms.inventory.model.stockTake.StockTakeTaskCriteria;
import com.deer.wms.inventory.model.stockTake.StockTakeTaskDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StockTakeTaskMapper extends Mapper<StockTakeTask> {
    List<StockTakeTaskDto> findList(StockTakeTaskCriteria criteria);
    List<StockTakeTaskDto> findPDAList(StockTakeTaskCriteria criteria);

    List<StockTakeTask> findByPdId(@Param("pdId") Integer pdId);
}
