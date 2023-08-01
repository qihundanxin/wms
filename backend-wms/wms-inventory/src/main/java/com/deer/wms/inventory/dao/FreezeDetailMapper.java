package com.deer.wms.inventory.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deer.wms.inventory.model.freeze.FreezeDetail;
import com.deer.wms.inventory.model.freeze.FreezeDetailCriteria;
import com.deer.wms.inventory.model.freeze.FreezeDetailDto;
import com.deer.wms.project.root.core.mapper.Mapper;

public interface FreezeDetailMapper extends Mapper<FreezeDetail> {

    List<FreezeDetailDto> findList(FreezeDetailCriteria criteria);

    void deleteByBillNo(@Param("billNo") String billNo);

    List<FreezeDetail> findFreezingByInventory(Integer inventoryId);

    List<FreezeDetailDto> findListAndMaster(FreezeDetailCriteria criteria);
}