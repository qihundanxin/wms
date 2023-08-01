package com.deer.wms.ware.task.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.Allot.AllotMaster;
import com.deer.wms.ware.task.model.Allot.AllotMasterCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AllotMasterMapper extends Mapper<AllotMaster> {

    List<AllotMaster> findList(AllotMasterCriteria criteria);

    void deleteByBillNo(@Param("billNo") String billNo);
    void allot(@Param("billNo") String billNo);

}