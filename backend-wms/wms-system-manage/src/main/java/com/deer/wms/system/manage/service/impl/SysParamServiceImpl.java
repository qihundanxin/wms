package com.deer.wms.system.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.project.root.constant.CacheKey;
import com.deer.wms.project.root.constant.CacheTime;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.CacheTool;
import com.deer.wms.system.manage.dao.SysParamMapper;
import com.deer.wms.system.manage.model.SysParam;
import com.deer.wms.system.manage.model.SysParamCriteria;
import com.deer.wms.system.manage.model.SysParamDetail;
import com.deer.wms.system.manage.model.SysParamDetailCriteria;
import com.deer.wms.system.manage.model.SysParamDto;
import com.deer.wms.system.manage.service.SysParamDetailService;
import com.deer.wms.system.manage.service.SysParamService;

/**
 * Created by guo on 2019/07/12.
 */
@Service
@Transactional
public class SysParamServiceImpl extends AbstractService<SysParam, Integer> implements SysParamService {

    @Autowired
    private SysParamMapper sysParamMapper;

    @Autowired
    private SysParamDetailService sysParamDetailService;

    @Autowired
    private SysParamService sysParamService;

    @Autowired
    private CacheTool cache;

    @Override
    public List<SysParamDto> findList(SysParamCriteria criteria) {
        return sysParamMapper.findList(criteria);
    }

    public List<SysParamDto> reSetRedis() {
        SysParamCriteria criteria = new SysParamCriteria();
        List<SysParamDto> list = findList(criteria);

        for (SysParamDto ysParamDto : list) {
            SysParamDetailCriteria sysParamDetailCriteria = new SysParamDetailCriteria();
            sysParamDetailCriteria.setSysParamId(ysParamDto.getSysParamId());
            List<SysParamDetail> detailList = sysParamDetailService.findList(sysParamDetailCriteria);
            ysParamDto.setDetails(detailList);
        }
        cache.setObject(CacheKey.ALL_SYS_PARAM_DTO, null, list, CacheTime.ALL_SYS_PARAM_DTO);
        return list;
    }

    @Override
    public Result createSysParam(SysParam sysParam) {
        List<SysParamDto> list = sysParamService.findByCode(sysParam.getParamCode());
        List<SysParamDto> list1 = sysParamService.findByName(sysParam.getParamName());

        if (list.size() == 0 && list1.size() == 0) {
            save(sysParam);
            return ResultGenerator.genSuccessResult();
        } else if (list.size() == 0 && list1.size() != 0) {
            throw new ServiceException(CommonCode.SYSPARAM_EXISTS, "此系统参数已存在");
        } else {
            throw new ServiceException(CommonCode.SYSPARAM_CODE_EXISTS, "此系统参数编码已存在");

        }
    }

    @Override
    public List<SysParamDto> findByCode(String paramCode) {
        return sysParamMapper.findByCode(paramCode);
    }

    @Override
    public List<SysParamDto> findByName(String paramName) {
        return sysParamMapper.findByName(paramName);
    }

    @Override
    public List<SysParam> findByMemo(String memo) {
        return sysParamMapper.findByMemo(memo);
    }

}
