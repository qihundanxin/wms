package com.deer.wms.system.manage.service;

import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.util.RedisUtil;
import com.deer.wms.system.manage.model.SysParam;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.system.manage.model.SysParamCriteria;
import com.deer.wms.system.manage.model.SysParamDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by guo on 2019/07/12.
 */
public interface SysParamService extends Service<SysParam, Integer> {

    Result createSysParam(SysParam sysParam);

    List<SysParamDto> findList(SysParamCriteria criteria);

    List<SysParamDto> reSetRedis();

    List<SysParamDto> findByCode(String paramCode);

    List<SysParamDto> findByName(String paramName);

    List<SysParam> findByMemo(String memo);

}
