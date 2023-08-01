package com.deer.wms.webhook.service;

import java.util.List;

import com.deer.wms.webhook.model.WebHookSetting;

/**
 * WebHook设置
 * 
 * @author xuesinuo
 *
 */
public interface WebHookSettingService {
    WebHookSetting add(WebHookSetting entity);

    void del(Long id);

    void setEnable(Long id, Boolean hasEnable);

    List<WebHookSetting> getList(WebHookSetting param);
}
