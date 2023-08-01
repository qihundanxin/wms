package com.deer.wms.system.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.DataDictDetail;
import com.deer.wms.system.manage.model.DataDictDetailCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DataDictDetailMapper extends Mapper<DataDictDetail> {
    List<DataDictDetail> findList(DataDictDetailCriteria criteria);

    List<DataDictDetail> findBydDataDictCode(@Param("dataDictCode") String dataDictCode);
}