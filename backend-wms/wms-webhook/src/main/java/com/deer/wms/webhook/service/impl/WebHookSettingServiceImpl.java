package com.deer.wms.webhook.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.deer.wms.project.root.util.Np;
import com.deer.wms.webhook.dao.WebHookSettingMapper;
import com.deer.wms.webhook.model.WebHookSetting;
import com.deer.wms.webhook.service.WebHookSettingService;

import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebHookSettingServiceImpl implements WebHookSettingService {

    private final WebHookSettingMapper dao;
    @Override
    public WebHookSetting add(WebHookSetting entity) {
        entity.setName(Np.i(entity.getName()).o(""));
        entity.setCreateTime(Np.i(entity.getCreateTime()).o(new Date()));
        entity.setSecretKey(IdUtil.fastSimpleUUID());
        dao.insert(entity);
        return entity;
    }

    @Override
    public void del(Long id) {
        dao.deleteByPrimaryKey(id);
    }

    @Override
    public void setEnable(Long id, Boolean hasEnable) {
        WebHookSetting entity = dao.selectByPrimaryKey(id);
        entity.setWebHookSettingId(id);
        entity.setHasEnable(Np.i(hasEnable).o(false));
        dao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public List<WebHookSetting> getList(WebHookSetting param) {
        return dao.select(param);
    }

}
