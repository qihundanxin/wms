package com.deer.wms.inventory.dao;

import com.deer.wms.inventory.model.move.InventoryMove;
import com.deer.wms.inventory.model.move.InventoryMoveCriteria;
import com.deer.wms.inventory.model.move.InventoryMoveDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InventoryMoveMapper extends Mapper<InventoryMove> {
    List<InventoryMoveDto> findList(InventoryMoveCriteria criteria);

    void deleteByBillNo(@Param("billNo") String billNo);
}