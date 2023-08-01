package com.deer.wms.project.root.sync.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import com.deer.wms.project.root.sync.pojo.SyncParam.LockMode;

/**
 * 默认利用方法名做lockName进行分布式锁，此时禁止重载（Do Not Overload）<br>
 * 
 * 分布式的同步Method<br>
 * 
 * 不要类内部调用，否则切面失效
 * 
 * @author xuesinuo 2020-06-24 15:15:03
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Synchronized {
    /**
     * @return 【参数级】锁Key SpEl表达式
     */
    String value() default "";

    /**
     * @return 【方法级】锁标识（默认不填即可，默认禁止重载）
     */
    String lockName() default "";

    /**
     * @return 失效时长，默认60秒
     */
    long timeout() default 60;

    /**
     * @return 超时时长单位
     */
    TimeUnit timeoutUnit() default TimeUnit.SECONDS;

    /**
     * @return 锁的类型
     */
    LockMode lockMode() default LockMode.RANDOM;
}
