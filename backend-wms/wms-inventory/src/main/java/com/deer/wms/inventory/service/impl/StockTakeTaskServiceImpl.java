package com.deer.wms.inventory.service.impl;

import com.deer.wms.inventory.dao.StockTakeTaskMapper;
import com.deer.wms.inventory.model.stockTake.StockTakeTask;
import com.deer.wms.inventory.model.stockTake.StockTakeTaskCriteria;
import com.deer.wms.inventory.model.stockTake.StockTakeTaskDto;
import com.deer.wms.inventory.service.StockTakeTaskService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 郭靖勋 on 2020/02/06.
 */
@Service
@Transactional
public class StockTakeTaskServiceImpl extends AbstractService<StockTakeTask, Integer> implements StockTakeTaskService {

    @Autowired
    private StockTakeTaskMapper stockTakeTaskMapper;

    @Override
    public List<StockTakeTaskDto> findList(StockTakeTaskCriteria criteria) {
        return stockTakeTaskMapper.findList(criteria);
    }

    @Override
    public List<StockTakeTask> findByPdId(Integer pdId) {
          return stockTakeTaskMapper.findByPdId(pdId);
    }

    @Override
    public List<StockTakeTaskDto> findPDAList(StockTakeTaskCriteria criteria) {
        return stockTakeTaskMapper.findPDAList(criteria);
    }
}
