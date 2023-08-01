package com.deer.wms.base.system.dao;

import com.deer.wms.base.system.model.Area.AreaInfo;
import com.deer.wms.base.system.model.Area.AreaInfoCriteria;
import com.deer.wms.base.system.model.Area.AreaInfoDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.ValidatorAreaInfoModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AreaInfoMapper extends Mapper<AreaInfo> {

    List<AreaInfoDto> findList(AreaInfoCriteria criteria);

    List<AreaInfoDto> findByCode(@Param("areaCode") String areaCode);

    List<AreaInfoDto> findByName(@Param("areaName") String areaName);

    int validatorChongfu(ValidatorAreaInfoModel model);

    List<AreaInfoDto> findByWareIdAndCode(@Param("wareId") Integer wareId, @Param("areaCode") String areaCode);
}
