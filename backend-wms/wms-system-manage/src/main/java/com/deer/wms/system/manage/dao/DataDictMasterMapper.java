package com.deer.wms.system.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.DataDictMaster;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DataDictMasterMapper extends Mapper<DataDictMaster> {

    List<DataDictMaster> findByCode(@Param("dataDictCode") String dataDictCode);

    List<DataDictMaster> findByName(@Param("dataDictName") String dataDictName);
}