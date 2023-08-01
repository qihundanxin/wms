package com.deer.wms.ware.task.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.Return.ReturnDetail;
import com.deer.wms.ware.task.model.Return.ReturnDetailCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReturnDetailMapper extends Mapper<ReturnDetail> {

    List<ReturnDetail> findByBillNo(@Param("billNo") String billNo);

    List<ReturnDetail> findList(ReturnDetailCriteria criteria);
}