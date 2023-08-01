package com.deer.wms.system.manage.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.Np;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.system.manage.model.permission.Permission;
import com.deer.wms.system.manage.model.resource.Resource;
import com.deer.wms.system.manage.model.resource.ResourceCriteria;
import com.deer.wms.system.manage.model.resource.ResourceDto;
import com.deer.wms.system.manage.model.resource.ResourceUrl;
import com.deer.wms.system.manage.service.PermissionService;
import com.deer.wms.system.manage.service.ResourceService;
import com.deer.wms.system.manage.service.ResourceUrlService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 资源信息api接口
 * 
 * Created by guo on 2017/10/08.
 */
@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ResourceUrlService resourceUrlService;

    @PostMapping("/add")
    @Authority("resources_add")
    public Result add(@RequestBody ResourceDto resourceDto) {
        Integer resourceGroup = Np.i(resourceDto.getResourceGroup()).o(1);
        String state = Np.i(resourceDto.getState()).o("normal");
        //验证与父级状态和位置是否一致
        if(resourceDto.getParentId()!=null){
            Resource resource = resourceService.findById(resourceDto.getParentId());
            if(Np.i(resource.getState()).notEq(state)){
                throw new ServiceException(CommonCode.SERVICE_ERROR, "与上级资源状态不一致，请重新选择！");
            }
            if(Np.i(resource.getResourceGroup()).notEq(resourceGroup)){
                throw new ServiceException(CommonCode.SERVICE_ERROR, "与上级位置不一致，请重新选择！");
            }

        }
        Resource resource = new Resource();
        resource.setResourceCode(resourceDto.getResourceCode());
        resource.setResourceName(resourceDto.getResourceName());
        resource.setResourceType(resourceDto.getResourceType());
        resource.setParentId(resourceDto.getParentId());
        resource.setResourceGroup(resourceGroup);
        resource.setLevel(resourceDto.getLevel());
        resource.setSortNumber(resourceDto.getSortNumber());
        resource.setIcon(resourceDto.getIcon());
        resource.setAuthorityCode(resourceDto.getAuthorityCode());
        resource.setState(state);
        String url = resourceDto.getUrl();
        if (url == null || "".equals(url)) {
            resourceService.save(resource);
        } else {
            resourceService.save(resource);

            ResourceUrl resourceUrl = new ResourceUrl();
            resourceUrl.setUrl(resourceDto.getUrl());
            resourceUrl.setResourceId(resource.getResourceId());
            resourceUrl.setUrlType(resourceDto.getUrlType());

            resourceUrlService.save(resourceUrl);
        }

        Permission permission = new Permission();
        permission.setObjectId(1);

        Integer resourceId = resource.getResourceId();

        permission.setResourceId(resourceId);
        permissionService.save(permission);

        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("resources_delete")
    public Result delete(Integer resourceId) {
        /*Resource resource = new Resource();
        resource = resourceService.findById(resourceId);
        resource.setState("deleted");*/
        resourceService.deleteAllById(resourceId);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("resources_update")
    public Result update(@RequestBody ResourceDto resource) {
        //如果修改了状态或者位置，子级需要进行一致性修改
        Resource resourceOrigin = resourceService.findById(resource.getResourceId());
        //修改了位置
        if(Np.i(resourceOrigin.getResourceGroup()).notEq(resource.getResourceGroup()) || Np.i(resource.getState()).notEq(resourceOrigin.getState())){
            //更新所有子级的位置/状态信息
            resourceService.updateAllByParentId(resource.getResourceId(),resource);

        }
        resourceService.update(resource);

        Integer resourceId = resource.getResourceId();
        ResourceUrl resourceUrl = resourceUrlService.findBy("resourceId", resourceId);
        if (resourceUrl != null) {
            resourceUrl.setUrl(resource.getUrl());
            resourceUrlService.update(resourceUrl);
        } else if (resource.getUrl() != null && !"".equals(resource.getUrl())) {
            resourceUrl = new ResourceUrl();
            resourceUrl.setUrl(resource.getUrl());
            resourceUrl.setResourceId(resource.getResourceId());
            resourceUrl.setUrlType(resource.getUrlType());

            resourceUrlService.save(resourceUrl);
        }

        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("resources_get")
    public Result detail(@PathVariable Integer id) {
        Resource resource = resourceService.findById(id);
        return ResultGenerator.genSuccessResult(resource);
    }

    @GetMapping("/list")
    @Authority("resources_list")
    public Result list(ResourceCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ResourceDto> list = resourceService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/listPage")
    @Authority("resources_listPage")
    public Result listPage(ResourceCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ResourceDto> list = resourceService.findPageList();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
