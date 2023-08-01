package com.deer.wms.system.manage.service.impl;

import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.system.manage.dao.WareUserMapper;
import com.deer.wms.system.manage.model.ware.WareUser;
import com.deer.wms.system.manage.model.ware.WareUserCriteria;
import com.deer.wms.system.manage.model.ware.WareUserDto;
import com.deer.wms.system.manage.service.WareUserService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2019/11/05.
 */
@Service
@Transactional
public class WareUserServiceImpl extends AbstractService<WareUser, Integer> implements WareUserService {

    @Autowired
    private WareUserMapper wareUserMapper;

    @Autowired
    private WareUserService wareUserService;

    @Override
    public List<WareUserDto> findList(WareUserCriteria criteria) {
        return wareUserMapper.findList(criteria);
    }

    @Override
    public List<WareUserDto> findByWareId(Integer wareId) {
        return wareUserMapper.findByWareId(wareId);
    }

    @Override
    public void addWareUser(Integer userId, Integer wareId, Integer createUserId) {
        String nowDate = DateUtils.getNowDateTimeString();
        WareUser wareUser = new WareUser();
        wareUser.setUserId(userId);
        wareUser.setWareId(wareId);
        wareUser.setCreateTime(nowDate);
        wareUser.setCreateUserId(createUserId);
        super.save(wareUser);
    }

    @Override
    public void deleteByUserId(Integer userId) {
        wareUserMapper.deleteByUserId(userId);
    }

    @Override
    public void deleteByWareId(Integer wareId) {
        wareUserMapper.deleteByWareId(wareId);
    }

}
