package com.deer.wms.inventory.dao;

import com.deer.wms.inventory.model.combinePallet.CombinePalletTask;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CombinePalletTaskMapper extends Mapper<CombinePalletTask> {
    List<CombinePalletTask> taskWithBoxCode(@Param("boxCode") String boxCode);
}