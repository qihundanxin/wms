package com.deer.wms.base.system.dao;

import com.deer.wms.base.system.model.Pack.Pack;
import com.deer.wms.base.system.model.Pack.PackCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PackMapper extends Mapper<Pack> {

    List<Pack> findList(PackCriteria criteria);

    List<Pack> findByPackData(@Param("packData") String packData);
}