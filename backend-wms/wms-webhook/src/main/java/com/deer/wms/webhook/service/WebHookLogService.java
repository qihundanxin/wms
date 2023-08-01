package com.deer.wms.webhook.service;

import java.util.List;

import com.deer.wms.webhook.model.WebHookLog;
import com.deer.wms.webhook.model.WebHookLogParam;

/**
 * WebHook设置
 * 
 * @author xuesinuo
 *
 */
public interface WebHookLogService {
    WebHookLog add(WebHookLog entity);

    List<WebHookLog> getList(WebHookLogParam param);
}
