package com.deer.wms.inventory.service;

import java.util.List;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.stockTake.StockTakeData;
import com.deer.wms.inventory.model.stockTake.StockTakeMaster;
import com.deer.wms.inventory.model.stockTake.StockTakeMasterCriteria;
import com.deer.wms.project.root.core.service.Service;

/**
 * Created by ll on 2020/03/09.
 */
public interface StockTakeMasterService extends Service<StockTakeMaster, Integer> {
    List<StockTakeMaster> findList(StockTakeMasterCriteria stockTakeMasterCriteria);

    void createStockTakeMaster(StockTakeData stockTakeData, CurrentUser currentUser);

    int saveStockTakeTasks(StockTakeMaster stockTakeMaster, String billNo, Integer createUserId, String createUserName, String createTime);

    /**
     * 根据id查询盘点任务是否全部完成并修改状态
     *
     * @param stockTakeMasterId
     */
    void updateStockTakeMasterState(Integer stockTakeMasterId);
}
