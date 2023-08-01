package com.deer.wms.inventory.service;

import com.deer.wms.inventory.model.stockTake.StockTakeTask;
import com.deer.wms.inventory.model.stockTake.StockTakeTaskCriteria;
import com.deer.wms.inventory.model.stockTake.StockTakeTaskDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by 郭靖勋 on 2020/02/06.
 */
public interface StockTakeTaskService extends Service<StockTakeTask, Integer> {

    List<StockTakeTaskDto> findList(StockTakeTaskCriteria criteria);
    List<StockTakeTask> findByPdId(Integer pdId);
    List<StockTakeTaskDto> findPDAList(StockTakeTaskCriteria criteria);

}
