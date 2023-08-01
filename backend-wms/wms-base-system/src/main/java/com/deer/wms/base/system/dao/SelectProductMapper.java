package com.deer.wms.base.system.dao;

import com.deer.wms.base.system.model.SelectProduct;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SelectProductMapper extends Mapper<SelectProduct> {
    void deleteAll();

    List<SelectProduct> selectBy(@Param("where") String where);
}