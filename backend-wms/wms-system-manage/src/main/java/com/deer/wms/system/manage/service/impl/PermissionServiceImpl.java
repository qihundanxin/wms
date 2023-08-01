package com.deer.wms.system.manage.service.impl;

import com.deer.wms.system.manage.dao.PermissionMapper;
import com.deer.wms.system.manage.model.permission.Menu;
import com.deer.wms.system.manage.model.permission.Permission;
import com.deer.wms.system.manage.model.permission.PermissionTreeModel;
import com.deer.wms.system.manage.model.resource.Resource;
import com.deer.wms.system.manage.service.PermissionService;

import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.system.manage.service.ResourceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * Created by guo on 2018/8/11.
 */
@Service
@Transactional
public class PermissionServiceImpl extends AbstractService<Permission, Integer> implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public void emptyPermissionByRoleId(Integer roleId) {
        Condition condition = new Condition(Permission.class);
        Condition.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("objectType", "role");
        criteria.andEqualTo("objectId", roleId);
        permissionMapper.deleteByCondition(condition);
    }

    @Override
    public void deleteByRoleId(Integer roleId) {
        permissionMapper.deleteByRoleId(roleId);
    }

    @Override
    public List<String> findPermissionCodeByUserId(Integer userId) {
        List<String> codes = permissionMapper.selectPermissionCodeByUserId(userId);
        return codes;
    }

    @Override
    public List<String> findPermissionUrlByUserId(Integer userId) {
        List<String> urls = permissionMapper.selectPermissionUrlByUserId(userId);
        return urls;
    }

    @Override
    public List<Menu> findPermissionMenuByUserId(Integer userId) {
        List<Menu> list = permissionMapper.selectPermissionMenuByUserId(userId);
        return list;
    }

    @Override
    public List<PermissionTreeModel> findPermissionTreeFormListByRoleId(Integer roleId) {
        List<PermissionTreeModel> list = permissionMapper.selectPermissionTreeFormListByRoleId(roleId);
        return list;
    }

    @Override
    public List<PermissionTreeModel> findPermissionRangeTreeFormListByRoleId(Integer roleId) {
        List<PermissionTreeModel> list = permissionMapper.selectPermissionRangeTreeFormListByRoleId(roleId);
        return list;
    }

    @Autowired
    private ResourceService resourceService;

    @Override
    public void save2(Permission permission) {
        Integer resourceId = permission.getResourceId();
        Resource resource = resourceService.findById(resourceId);
        if(resource!=null){
            Integer parentId = resource.getParentId();
            Permission parent = permissionMapper.findByRoleIdAndResourceId(permission.getObjectId(), parentId);
            if (parent == null) {
                parent = new Permission();
                BeanUtils.copyProperties(permission, parent);
                parent.setResourceId(parentId);
                super.save(parent);
            }
            super.save(permission);
        }
    }

}
