package com.deer.wms.base.system.dao;

import com.deer.wms.base.system.model.PrinterInfo;
import com.deer.wms.base.system.model.PrinterInfoCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PrinterInfoMapper extends Mapper<PrinterInfo> {

    List<PrinterInfo> findList(PrinterInfoCriteria criteria);
     PrinterInfo findPdaDefault(Integer wareId);

     void changePda(@Param("id") Integer id, @Param("pdaDefault") String pdaDefault, @Param("wareId") Integer wareId, @Param("pdaDefault2") String pdaDefault2);
}