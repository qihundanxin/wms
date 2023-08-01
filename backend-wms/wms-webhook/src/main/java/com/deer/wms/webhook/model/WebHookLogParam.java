package com.deer.wms.webhook.model;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * WebHook日志查询条件
 * 
 * @author xuesinuo
 *
 */
@Data
public class WebHookLogParam {

    /**
     * 发送时间（起）
     */
    private LocalDateTime sendDatetimeBegin;

    /**
     * 发送时间（止）
     */
    private LocalDateTime sendDatetimeEnd;

    /**
     * WebHook配置ID
     */
    private Long WebHookSrttingId;

    /**
     * 能力枚举{@link WebHookAbility}
     */
    private WebHookAbility ability;

    /**
     * 数据关键词 Like查询
     */
    private String keyLike;
}
