package com.deer.wms.webhook;

import com.deer.wms.webhook.model.WebHookAbility;

/**
 * WebHook发送器
 * 
 * @author xuesinuo
 *
 */
public interface WebHookSender {
    /**
     * 发送WebHook
     * 
     * @param WebHookAbility 能力枚举
     * @param data           发送的数据
     * @param key            搜索用关键词
     */
    void send(WebHookAbility WebHookAbility, Object data, String key);
}
