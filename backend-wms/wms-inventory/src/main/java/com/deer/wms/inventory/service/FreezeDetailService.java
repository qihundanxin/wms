package com.deer.wms.inventory.service;

import java.util.List;

import com.deer.wms.inventory.model.freeze.FreezeDetail;
import com.deer.wms.inventory.model.freeze.FreezeDetailCriteria;
import com.deer.wms.inventory.model.freeze.FreezeDetailDto;
import com.deer.wms.project.root.core.service.Service;

/**
 * Created by guo on 2019/12/25.
 */
public interface FreezeDetailService extends Service<FreezeDetail, Integer> {

    List<FreezeDetailDto> findList(FreezeDetailCriteria criteria);

    void deleteByBillNo(String billNo);

    List<FreezeDetail> findFreezingByInventory(Integer inventoryId);

    List<FreezeDetailDto> findListAndMaster(FreezeDetailCriteria criteria);

}
