package com.deer.wms.base.system.dao;

import com.deer.wms.base.system.model.Pack.PackMaster;
import com.deer.wms.base.system.model.Pack.PackMasterCriteria;
import com.deer.wms.base.system.model.Pack.PackMasterDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PackMasterMapper extends Mapper<PackMaster> {

    List<PackMasterDto> findList(PackMasterCriteria criteria);

    List<PackMasterDto> findByPackCode(@Param("packCode") String packCode);

    List<PackMasterDto> findByPackName(@Param("packName") String packName);

    void deleteByCode(@Param("packCode") String packCode);

}