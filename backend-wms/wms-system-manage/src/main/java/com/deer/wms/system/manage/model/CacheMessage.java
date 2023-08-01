package com.deer.wms.system.manage.model;

import lombok.Data;

/**
 * 缓存信息
 * 
 * @author xuesinuo
 */
@Data
public class CacheMessage {
    /**
     * 缓存键，例如：ck1
     */
    private String cacheKey;
    /**
     * 缓存名，例如：CRON（定时任务）
     */
    private String cacheName;
    /**
     * 当前缓存数量
     */
    private Long count;
}
