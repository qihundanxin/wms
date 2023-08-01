package com.deer.wms.base.system.service;

import com.deer.wms.base.system.model.Item.InventorySafety;
import com.deer.wms.base.system.model.Item.InventorySafetyCriteria;
import com.deer.wms.base.system.model.Item.InventorySafetyDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by ll on 2020/03/03.
 */
public interface InventorySafetyService extends Service<InventorySafety, Integer> {

    List<InventorySafety> findList(InventorySafetyCriteria criteria);

    List<InventorySafetyDto> bh();

}
