package com.deer.wms.system.manage.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.system.manage.model.resource.ResourceUrl;
import com.deer.wms.system.manage.model.resource.ResourceUrlCriteria;
import com.deer.wms.system.manage.service.ResourceUrlService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Created by guo on 2017/10/07.
 */
@RestController
@RequestMapping("/resource/urls")
public class ResourceUrlController {

    @Autowired
    private ResourceUrlService resourceUrlService;

    @PostMapping
    @Authority("resources_urls_add")
    public Result add(@RequestBody ResourceUrl resourceUrl) {
        resourceUrlService.save(resourceUrl);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    @Authority("resources_urls_delete")
    public Result delete(@PathVariable Integer id) {
        resourceUrlService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    @Authority("resources_urls_update")
    public Result update(@RequestBody ResourceUrl resourceUrl) {
        resourceUrlService.update(resourceUrl);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("resources_urls_get")
    public Result c(@PathVariable Integer id) {
        ResourceUrl resourceUrl = resourceUrlService.findById(id);
        return ResultGenerator.genSuccessResult(resourceUrl);
    }

    @GetMapping
    @Authority("resources_urls_list")
    public Result list(ResourceUrlCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ResourceUrl> list = resourceUrlService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
