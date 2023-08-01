package com.deer.wms.base.system.dao;

import com.deer.wms.base.system.model.Pack.PackDetail;
import com.deer.wms.base.system.model.Pack.PackDetail2;
import com.deer.wms.base.system.model.Pack.PackDetailCriteria;
import com.deer.wms.base.system.model.Pack.PackDetailDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PackDetailMapper extends Mapper<PackDetail> {

    List<PackDetailDto> findList(PackDetailCriteria criteria);
    List<PackDetailDto> findByPackCode(String packCode);

    List<PackDetailDto> findByPackId(@Param("id") Integer id);

    void deleteByCode(@Param("packCode") String packCode);

    PackDetail findLevel(@Param("packDetailId") Integer packDetailId);

    List<PackDetail> findByCode(@Param("packCode") String packCode);

    List<PackDetail2> findByCode2(@Param("packCode") String packCode);
}