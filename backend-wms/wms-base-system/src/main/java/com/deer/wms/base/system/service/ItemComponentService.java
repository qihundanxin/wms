package com.deer.wms.base.system.service;

import com.deer.wms.base.system.model.ItemCombined.ItemComponent;
import com.deer.wms.base.system.model.ItemCombined.ItemComponentCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by ll on 2020/02/14.
 */
public interface ItemComponentService extends Service<ItemComponent, Integer> {

    List<ItemComponent> findList(ItemComponentCriteria criteria);

    void deleteByCombinedCode(String code);
}
