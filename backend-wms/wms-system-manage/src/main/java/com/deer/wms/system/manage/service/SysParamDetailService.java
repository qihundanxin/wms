package com.deer.wms.system.manage.service;

import com.deer.wms.system.manage.model.SysParamDetail;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.system.manage.model.SysParamDetailCriteria;

import java.util.List;

/**
 * Created by guo on 2019/07/12.
 */
public interface SysParamDetailService extends Service<SysParamDetail, Integer> {
    List<SysParamDetail> findList(SysParamDetailCriteria criteria);

    void deleteBySysParamId(Integer SysParamId);

}
