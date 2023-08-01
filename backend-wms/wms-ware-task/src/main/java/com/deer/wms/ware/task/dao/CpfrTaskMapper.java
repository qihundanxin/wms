package com.deer.wms.ware.task.dao;

import com.deer.wms.base.system.model.Item.InventorySafetyDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.CpfrTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CpfrTaskMapper extends Mapper<CpfrTask> {

    void cancel();

    CpfrTask findSame(
             @Param("wareId") Integer wareId
            ,@Param("organizationId") Integer organizationId
            ,@Param("itemCode") String itemCode
            ,@Param("fromInventoryId") Integer fromInventoryId
            ,@Param("toCellCode") String toCellCode);

    int update(CpfrTask cpfrTask);

}
