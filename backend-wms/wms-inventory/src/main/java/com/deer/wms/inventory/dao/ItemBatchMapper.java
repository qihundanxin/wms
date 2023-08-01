package com.deer.wms.inventory.dao;

import com.deer.wms.inventory.model.Inventory.ItemBatch;
import com.deer.wms.inventory.model.Inventory.ItemBatchCriteria;
import com.deer.wms.inventory.model.Inventory.ItemBatchDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemBatchMapper extends Mapper<ItemBatch> {

    List<ItemBatchDto> findList(ItemBatchCriteria criteria);

    List<ItemBatchDto> findByOrganizationIdAndItemCode(@Param("organizationId") Integer organizationId, @Param("itemCode") String itemCode);
}