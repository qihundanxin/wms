package com.deer.wms.inventory.dao;

import com.deer.wms.inventory.model.combinePallet.CombinePalletTaskDetail;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CombinePalletTaskDetailMapper extends Mapper<CombinePalletTaskDetail> {
    List<CombinePalletTaskDetail> detailsWithBoxCode(@Param("boxCode") String boxCode);
}