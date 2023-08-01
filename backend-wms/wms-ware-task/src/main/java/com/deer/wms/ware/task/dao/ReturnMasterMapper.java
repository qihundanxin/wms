package com.deer.wms.ware.task.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.Return.ReturnMaster;
import org.apache.ibatis.annotations.Param;

public interface ReturnMasterMapper extends Mapper<ReturnMaster> {

    void deleteByBillNo(@Param("billNo") String billNo);
}