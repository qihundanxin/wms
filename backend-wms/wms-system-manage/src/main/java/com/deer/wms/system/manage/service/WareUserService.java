package com.deer.wms.system.manage.service;

import com.deer.wms.system.manage.model.ware.WareUser;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.system.manage.model.ware.WareUserCriteria;
import com.deer.wms.system.manage.model.ware.WareUserDto;

import java.util.List;

/**
 * Created by guo on 2019/11/05.
 */
public interface WareUserService extends Service<WareUser, Integer> {
    List<WareUserDto> findList(WareUserCriteria criteria);

    List<WareUserDto> findByWareId(Integer wareId);

    void deleteByWareId(Integer wareId);

    void addWareUser(Integer userId, Integer wareId, Integer createUserId);

    void deleteByUserId(Integer userId);
}
