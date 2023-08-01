package com.deer.wms.project.root.schedule.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 利用方法名做Key进行分布式锁，禁止重载（Do Not Overload）<br>
 * 
 * 分布式定时任务标志
 * 
 * @author xuesinuo 2020-10-31
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedScheduled {
    /**
     * 例如：1000L 代表时间单位1秒，45000L 代表时间单位45秒。
     * 
     * @return 定时任务最小触发间隔
     */
    long timeUnit();
}
