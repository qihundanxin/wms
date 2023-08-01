package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.SjcUpperTask;
import com.deer.wms.ASN.manage.model.SjcUpperTaskCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SjcUpperTaskMapper extends Mapper<SjcUpperTask> {

    List<SjcUpperTask> findBySjcShellCode(SjcUpperTaskCriteria criteria);

    List<SjcUpperTask> findList(SjcUpperTaskCriteria criteria);

    String findSameItemCell(@Param("itemCode") String itemCode, @Param("wareId") Integer wareId);

    SjcUpperTask find0ByCellCode(@Param("cellCode") String cellCode, @Param("wareId") Integer wareId);

    SjcUpperTask find0ByToCellCode(@Param("cellCode") String cellCode, @Param("wareId") Integer wareId);
}