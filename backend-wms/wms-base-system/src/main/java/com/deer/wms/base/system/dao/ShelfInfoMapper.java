package com.deer.wms.base.system.dao;

import com.deer.wms.base.system.model.Shelf.ShelfInfo;
import com.deer.wms.base.system.model.Shelf.ShelfInfoCriteria;
import com.deer.wms.base.system.model.Shelf.ShelfInfoDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import com.deer.wms.system.manage.model.ValidatorShelfInfoModel;

import java.util.List;

public interface ShelfInfoMapper extends Mapper<ShelfInfo> {

    List<ShelfInfoDto> findList(ShelfInfoCriteria criteria);
    ShelfInfoDto findByCodeAndWareId(@Param("wareId") Integer wareId, @Param("shelfCode") String shelfCode);

    List<ShelfInfoDto> findByCode(String shelfCode);

    List<ShelfInfo> findByAreaId(Integer areaId);

    int validatorChongfu(ValidatorShelfInfoModel model);
}