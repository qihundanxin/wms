package com.deer.wms.inventory.service;

import com.deer.wms.inventory.model.Inventory.ItemBatch;
import com.deer.wms.inventory.model.Inventory.ItemBatchCriteria;
import com.deer.wms.inventory.model.Inventory.ItemBatchDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2019/12/02.
 */
public interface ItemBatchService extends Service<ItemBatch, Integer> {

    List<ItemBatchDto> findList(ItemBatchCriteria criteria);

    List<ItemBatchDto> findByOrganizationIdAndItemCode(Integer organizationId, String itemCode);

    ItemBatch createItemBatch(ItemBatch itemBatch);

}
