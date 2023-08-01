package com.deer.wms.base.system.service;

import com.deer.wms.base.system.model.ItemCombined.ItemCombined;
import com.deer.wms.base.system.model.ItemCombined.ItemCombinedCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by ll on 2020/02/14.
 */
public interface ItemCombinedService extends Service<ItemCombined, Integer> {

    List<ItemCombined> findList(ItemCombinedCriteria criteria);

    ItemCombined findByCombinedId(Integer id);
}
