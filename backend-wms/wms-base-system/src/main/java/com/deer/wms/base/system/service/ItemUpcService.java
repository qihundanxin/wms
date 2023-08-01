package com.deer.wms.base.system.service;

import com.deer.wms.base.system.model.ItemUpc;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by  on 2021/07/08.
 */
public interface ItemUpcService extends Service<ItemUpc, Integer> {

    void delete(Integer itemUpcId);
    List<ItemUpc> findByItemCode(String itemCode);

}
