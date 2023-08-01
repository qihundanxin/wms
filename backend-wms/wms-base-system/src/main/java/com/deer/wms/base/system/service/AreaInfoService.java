package com.deer.wms.base.system.service;

import com.deer.wms.base.system.model.Area.AreaInfo;
import com.deer.wms.base.system.model.Area.AreaInfoCriteria;
import com.deer.wms.base.system.model.Area.AreaInfoDto;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.system.manage.model.ValidatorAreaInfoModel;

import java.util.List;

/**
 * Created by guo on 2019/11/21.
 */
public interface AreaInfoService extends Service<AreaInfo, Integer> {

    List<AreaInfoDto> findList(AreaInfoCriteria criteria);

    List<AreaInfoDto> findByCode(String areaCode);

    List<AreaInfoDto> findByName(String areaName);

    int validatorChongfu(ValidatorAreaInfoModel model);

    /**
     * @Author luolin
     * @Description 保存货区
     * @Date 11:08 2023/6/25
     * @param areaInfo
     * @return
     **/
    void saveAreaInfo(AreaInfo areaInfo);

    /**
     * @Author luolin
     * @Description 修改货区
     * @Date 11:32 2023/6/25
     * @param areaInfo
     * @return
     **/
    void updateAreaInfo(AreaInfo areaInfo);

    /**
     * @Author luolin
     * @Description 删除货区
     * @Date 13:37 2023/6/25
     * @param areaId 货区id
     * @return
     **/
    void deleteByAreaId(Integer areaId);
}
