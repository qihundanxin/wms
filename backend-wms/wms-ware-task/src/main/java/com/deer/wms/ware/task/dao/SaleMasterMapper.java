package com.deer.wms.ware.task.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.Sale.SaleMaster;
import com.deer.wms.ware.task.model.Sale.SaleMasterCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SaleMasterMapper extends Mapper<SaleMaster> {

    List<SaleMaster> findList(SaleMasterCriteria criteria);

    void deleteByBillNo(@Param("billNo") String billNo);
}