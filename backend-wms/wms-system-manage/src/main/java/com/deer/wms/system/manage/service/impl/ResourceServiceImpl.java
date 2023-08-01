package com.deer.wms.system.manage.service.impl;

import com.deer.wms.system.manage.dao.ResourceMapper;
import com.deer.wms.system.manage.model.resource.Resource;
import com.deer.wms.system.manage.model.resource.ResourceCriteria;
import com.deer.wms.system.manage.model.resource.ResourceDto;
import com.deer.wms.system.manage.service.ResourceService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2018/8/11.
 */
@Service
@Transactional
public class ResourceServiceImpl extends AbstractService<Resource, Integer> implements ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public List<ResourceDto> findList(ResourceCriteria criteria) {
        return resourceMapper.findList(criteria);
    }

    @Override
    public List<ResourceDto> findPageList() {
        return resourceMapper.findPageList();
    }

    /**
     * 根据parentId 更新所有子级
     * @param resourceId
     * @return
     */
    @Override
    public boolean updateAllByParentId(Integer resourceId,ResourceDto resourceDto) {
        ResourceCriteria criteria = new ResourceCriteria();
        criteria.setParentId(resourceId);
        List<Resource> resources =  findListByParam(criteria);
        if(resources.size()>0){
            //更新
            for(Resource resource:resources){
                if(resourceDto.getResourceGroup()!=null){
                    resource.setResourceGroup(resourceDto.getResourceGroup());
                }
                if(resourceDto.getState()!=null){
                    resource.setState(resourceDto.getState());
                }
                update(resource);
                updateAllByParentId(resource.getResourceId(),resourceDto);
            }
        }
        return true;
    }

    /**
     * 根据条件查询列表
     * @param criteria
     * @return
     */
    @Override
    public List<Resource> findListByParam(ResourceCriteria criteria) {
        return resourceMapper.findListByParam(criteria);
    }

    /**
     * 根据id 删除，本身及子级
     * @param id
     * @return
     */
    @Override
    public boolean deleteAllById(Integer id) {
        ResourceCriteria criteria = new ResourceCriteria();
        criteria.setParentId(id);
        List<Resource> resources =  findListByParam(criteria);
        if(resources.size()>0){
            //删除子级
            for(Resource resource:resources){
                deleteById(resource.getResourceId());
                deleteAllById(resource.getResourceId());
            }
        }
        deleteById(id);
        return true;
    }

}
