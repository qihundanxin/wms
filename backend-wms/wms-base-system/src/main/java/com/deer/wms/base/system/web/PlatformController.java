package com.deer.wms.base.system.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.base.system.model.Platform.Platform;
import com.deer.wms.base.system.model.Platform.PlatformCriteria;
import com.deer.wms.base.system.model.Platform.PlatformDto;
import com.deer.wms.base.system.service.PlatformService;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.constant.CacheKey;
import com.deer.wms.project.root.constant.CacheTime;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.CacheTool;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 月台信息api接口
 * 
 * Created by guo on 2019/11/19.
 */
@RestController
@RequestMapping("/platforms")
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    @Autowired
    private CacheTool cache;

    /**
     * 新增月台信息
     */
    @PostMapping("/insert")
    @Authority("platforms_insert")
    public Result add(@RequestBody Platform platform, @User CurrentUser currentUser) {
        platformService.save(platform);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 删除月台信息
     */
    @Transactional
    @GetMapping("/delete")
    @Authority("platforms_delete")
    public Result delete(Integer id, @User CurrentUser currentUser) {
        platformService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 更新月台信息
     */
    @Transactional
    @PostMapping("/update")
    @Authority("platforms_update")
    public Result update(@RequestBody Platform platform, @User CurrentUser currentUser) {
        platformService.update(platform);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("platforms_detail")
    public Result detail(Integer id) {
        Platform platform = platformService.findById(id);
        return ResultGenerator.genSuccessResult(platform);
    }

    @Transactional
    @GetMapping("/list")
    @Authority("platforms_list")
    public Result list(PlatformCriteria criteria, @User CurrentUser currentUser) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());

        List<PlatformDto> list = platformService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Transactional
    @GetMapping("/findByWareId")
    @Authority("platforms_findByWareId")
    public Result findByWareId(PlatformCriteria criteria, @User CurrentUser currentUser) {
        if (criteria.getWareId() == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR);
        }
        List<Platform> list = cache.getObject(CacheKey.PLATFORM, criteria.getWareId().toString());
        if (list == null) {
            list = platformService.findByWareId(criteria.getWareId());
            cache.setObject(CacheKey.PLATFORM, criteria.getWareId().toString(), list, CacheTime.PLATFORM);
        }

        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
