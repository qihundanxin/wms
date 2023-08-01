package com.deer.wms.system.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.ware.WareUser;
import com.deer.wms.system.manage.model.ware.WareUserCriteria;
import com.deer.wms.system.manage.model.ware.WareUserDto;

import java.util.List;

public interface WareUserMapper extends Mapper<WareUser> {

    List<WareUserDto> findList(WareUserCriteria criteria);

    List<WareUserDto> findByWareId(Integer wareId);

    void deleteByUserId(Integer userId);

    void deleteByWareId(Integer wareId);
}