package com.deer.wms.base.system.dao;

import com.deer.wms.base.system.model.BoxInfo;
import com.deer.wms.base.system.model.BoxInfoCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BoxInfoMapper extends Mapper<BoxInfo> {
    List<BoxInfo> findList(BoxInfoCriteria criteria);

    BoxInfo findByBoxCode(@Param("boxCode") String boxCode,@Param("type") Integer type, @Param("wareId") Integer wareId);

    BoxInfo findByBoxCode2(@Param("boxCode") String boxCode, @Param("wareId") Integer wareId);
}
