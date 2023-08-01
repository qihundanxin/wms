package com.deer.wms.system.manage.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.constant.CacheKey;
import com.deer.wms.project.root.constant.CacheTime;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.util.CacheTool;
import com.deer.wms.system.manage.model.SysParam;
import com.deer.wms.system.manage.model.SysParamAllValue;
import com.deer.wms.system.manage.model.SysParamCriteria;
import com.deer.wms.system.manage.model.SysParamDetail;
import com.deer.wms.system.manage.model.SysParamDto;
import com.deer.wms.system.manage.model.SysParamInsert;
import com.deer.wms.system.manage.service.SysParamDetailService;
import com.deer.wms.system.manage.service.SysParamService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 系统参数信息api接口
 * 
 * Created by guo on 2019/07/12.
 */
@RestController
@RequestMapping("/sys/params")
public class SysParamController {

    @Autowired
    private SysParamService sysParamService;

    @Autowired
    private SysParamDetailService sysParamDetailService;

    @Autowired
    private CacheTool cache;

    @GetMapping("/getByCode")
    @Authority("sys_params_getByCode")
    public Result getByCode(String code) {
        SysParam sysParam = sysParamService.findBy("paramCode", code);
        return ResultGenerator.genSuccessResult(sysParam.getValue());
    }

    @PostMapping("/save")
    @Authority("sys_params_save")
    public Result add(@RequestBody SysParam sysParam) {
        cache.del(CacheKey.ALL_SYS_PARAM);
        sysParamService.createSysParam(sysParam);
        removeRedisA();
        removeRedisB();
        removeRedisC();
        sysParamService.reSetRedis();
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("sys_params_delete")
    public Result delete(Integer id) {
        cache.del(CacheKey.ALL_SYS_PARAM);
        sysParamService.deleteById(id);
        sysParamService.reSetRedis();
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("sys_params_update")
    public Result update(@RequestBody SysParamInsert sysParamInsert) {
        cache.del(CacheKey.ALL_SYS_PARAM);
        SysParam sysParam = sysParamInsert;
        sysParamService.update(sysParam);
        sysParamDetailService.deleteBySysParamId(sysParam.getSysParamId());
        List<SysParamDetail> sysParamDetails = sysParamInsert.getDetails();
        if (sysParamDetails != null) {
            for (SysParamDetail detail : sysParamDetails) {
                sysParamDetailService.save(detail);
            }

        }
        removeRedisA();
        removeRedisB();
        removeRedisC();
        sysParamService.reSetRedis();
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/updateAllValue")
    @Authority("sys_params_updateAllValue")
    public Result updateAllValue(@RequestBody SysParamAllValue sysParamAllValue) {
        cache.del(CacheKey.ALL_SYS_PARAM);
        List<SysParam> list = sysParamAllValue.getSysParams();
        if (list != null) {
            for (SysParam sysParam : list) {
                sysParamService.update(sysParam);
            }
        }
        sysParamService.reSetRedis();
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping
    @Authority("sys_params_list")
    public Result list(SysParamCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SysParamDto> list = sysParamService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    // @Async
    private void removeRedisA() {
        cache.del(CacheKey.ALL_SYS_PARAM);
        List<SysParam> list = sysParamService.findAll();
        String[] memos = list.stream().map(x -> x.getMemo()).toArray(String[]::new);
        cache.del(CacheKey.SYS_PARAM, memos);
    }

    // @Async
    private void removeRedisB() {
        cache.del(CacheKey.ALL_SYS_PARAM);
        SysParamCriteria criteria = new SysParamCriteria();
        List<SysParamDto> list = sysParamService.findList(criteria);
        String[] paramCodes = list.stream().map(x -> x.getParamCode()).toArray(String[]::new);
        cache.del(CacheKey.SYS_PARAM, paramCodes);
    }

    // @Async
    private void removeRedisC() {
        cache.del(CacheKey.ALL_SYS_PARAM);
        SysParamCriteria criteria = new SysParamCriteria();
        List<SysParamDto> list = sysParamService.findList(criteria);
        String[] moduleCodes = list.stream().map(x -> x.getModuleCode()).toArray(String[]::new);
        cache.del(CacheKey.SYS_PARAM, moduleCodes);
    }

    @GetMapping("/findByMemo")
    @Authority("sys_params_findByMemo")
    public Result list(String memo) {
        List<SysParam> list = cache.getObject(CacheKey.SYS_PARAM, memo);
        if (list == null || list.size() == 0) {
            list = sysParamService.findByMemo(memo);
            cache.setObject(CacheKey.SYS_PARAM, memo, list, CacheTime.SYS_PARAM);
        }

        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据模块查询
     * @param moduleCode
     * @return
     */
    @GetMapping("/findByModuleCode")
    @Authority("sys_params_findByModuleCode")
    public Result findByModuleCode(String moduleCode) {

        List<SysParamDto> list = cache.getObject(CacheKey.SYS_PARAM, moduleCode);
        if (list == null || list.size() == 0) {
            SysParamCriteria criteria = new SysParamCriteria();
            criteria.setModuleCode(moduleCode);
            list = sysParamService.findList(criteria);
            cache.setObject(CacheKey.SYS_PARAM, moduleCode, list, CacheTime.SYS_PARAM);
        }

        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据参数编码查询
     * @param paramCode
     * @return
     */
    @GetMapping("/findByParamCode")
    @Authority("sys_params_findByParamCode")
    public Result findByParamCode(String paramCode) {

        List<SysParamDto> list = cache.getObject(CacheKey.SYS_PARAM, paramCode);
        if (list == null || list.size() == 0) {
            SysParamCriteria criteria = new SysParamCriteria();
            criteria.setParamCode(paramCode);
            list = sysParamService.findList(criteria);
            cache.setObject(CacheKey.SYS_PARAM, paramCode, list, CacheTime.SYS_PARAM);
        }

        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 系统参数详情列表
     * @return
     */
    @GetMapping("/detailList")
    @Authority("sys_params_detailList")
    public Result detailList() {
        List<SysParamDto> list = cache.getObject(CacheKey.ALL_SYS_PARAM_DTO, null);
        if (list == null || list.size() == 0) {
            list = sysParamService.reSetRedis();
        }
        return ResultGenerator.genSuccessResult(list);
    }

    @GetMapping("/all")
    @Authority("sys_params_all")
    public Result all() {
        List<SysParam> list = cache.getObject(CacheKey.ALL_SYS_PARAM, null);
        if (list == null || list.size() == 0) {
            list = sysParamService.findAll();
            cache.setObject(CacheKey.ALL_SYS_PARAM, null, list, CacheTime.ALL_SYS_PARAM);
        }
        return ResultGenerator.genSuccessResult(list);
    }
}
