package com.deer.wms.inventory.dao;

import com.deer.wms.inventory.model.stockTake.StockTakeMaster;
import com.deer.wms.inventory.model.stockTake.StockTakeMasterCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;

import java.util.List;

public interface StockTakeMasterMapper extends Mapper<StockTakeMaster> {
    List<StockTakeMaster> findList(StockTakeMasterCriteria stockTakeMasterCriteria);

}