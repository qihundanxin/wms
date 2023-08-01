package com.deer.wms.project.root.sync.pojo;

import java.util.concurrent.TimeUnit;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SyncParam {
    /**
     * @return 【方法级】锁标识
     */
    @NotBlank
    private String lockName;

    /**
     * @return 【参数级】锁Key SpEl表达式
     */
    private String lockParam;

    /**
     * @return 失效时长，默认60秒
     */
    private long timeout = 60;

    /**
     * @return 超时时长单位
     */
    private TimeUnit timeoutUnit = TimeUnit.SECONDS;

    /**
     * @return 锁的类型
     */
    private LockMode lockMode = LockMode.RANDOM;

    /**
     * 最常用的构造方式：指定Key，其他默认
     * 
     * @param lockName
     * @param lockParam
     */
    public SyncParam(String lockName, String lockParam) {
        this.lockName = lockName;
        this.lockParam = lockParam;
    }

    public static enum LockMode {
        /**
         * 随机分配锁
         */
        RANDOM,
        /**
         * 公平锁：先到先得
         */
        FAIR
    }
}
