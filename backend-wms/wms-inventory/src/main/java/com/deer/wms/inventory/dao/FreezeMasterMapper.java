package com.deer.wms.inventory.dao;

import com.deer.wms.inventory.model.freeze.FreezeMaster;
import com.deer.wms.inventory.model.freeze.FreezeMasterCriteria;
import com.deer.wms.inventory.model.freeze.FreezeMasterDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FreezeMasterMapper extends Mapper<FreezeMaster> {

    List<FreezeMasterDto> findList(FreezeMasterCriteria criteria);

    void deleteByBillNo(@Param("billNo") String billNo);
}