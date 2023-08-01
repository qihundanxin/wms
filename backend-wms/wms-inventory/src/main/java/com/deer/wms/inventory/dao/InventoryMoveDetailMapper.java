package com.deer.wms.inventory.dao;

import com.deer.wms.inventory.model.move.InventoryMoveDetail;
import com.deer.wms.inventory.model.move.InventoryMoveDetailCriteria;
import com.deer.wms.inventory.model.move.InventoryMoveDetailDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InventoryMoveDetailMapper extends Mapper<InventoryMoveDetail> {

    List<InventoryMoveDetailDto> findList(InventoryMoveDetailCriteria criteria);

    void deleteByBillNo(@Param("billNo") String billNo);
}