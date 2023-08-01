package com.deer.wms.inventory.dao;

import com.deer.wms.inventory.model.InventoryAdjustDetail;
import com.deer.wms.inventory.model.InventoryAdjustDetailCriteria;
import com.deer.wms.inventory.model.InventoryAdjustDetailDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InventoryAdjustDetailMapper extends Mapper<InventoryAdjustDetail> {
    List<InventoryAdjustDetailDto> findList(InventoryAdjustDetailCriteria inventoryAdjustDetailCriteria);

    void deleteByBillNo(@Param("billNo") String billNo);
}