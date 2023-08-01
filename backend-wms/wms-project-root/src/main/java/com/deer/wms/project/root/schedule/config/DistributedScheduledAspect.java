package com.deer.wms.project.root.schedule.config;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import com.deer.wms.project.root.constant.AspectOrder;
import com.deer.wms.project.root.constant.CacheKey;
import com.deer.wms.project.root.schedule.annotation.DistributedScheduled;
import com.deer.wms.project.root.sync.annotation.Synchronized;
import com.deer.wms.project.root.util.AspectUtil;
import com.deer.wms.project.root.util.CacheTool;

import lombok.RequiredArgsConstructor;

/**
 * 分布式定时任务切面，以秒为单位区别任务处理。<br>
 * 
 * @author xuesinuo 2020-07-17
 */
@RequiredArgsConstructor
@Aspect
@Configuration
@Order(AspectOrder.CRON_DISTRIBUTED)
public class DistributedScheduledAspect {
    private final DistributedScheduledAspectSync sync;

    @Around("@annotation(distributedScheduled)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedScheduled distributedScheduled) throws Throwable {
        Long timeStamp = System.currentTimeMillis();
        Object target = joinPoint.getTarget();
        Method targetMethod = AspectUtil.getMethod(joinPoint);
        // fullClassName = lockName
        String fullClassName = ClassUtils.getUserClass(target).getName() + "." + targetMethod.getName();
        boolean lock = sync.getLock(fullClassName, timeStamp, distributedScheduled.timeUnit());
        if (lock) {
            return joinPoint.proceed();
        }
        return null;
    }
}

@RequiredArgsConstructor
@Component
class DistributedScheduledAspectSync {
    private final CacheTool cache;

    @Synchronized("#lockKey")
    public boolean getLock(String lockKey, Long timeStamp, Long timeUnit) {
        Long time = timeStamp / timeUnit;
        Long oldTime = cache.getObject(CacheKey.CRON, lockKey);
        if (oldTime == null || oldTime < time) {
            cache.setObject(CacheKey.CRON, lockKey, time, timeUnit * 3L);
            return true;
        }
        return false;
    }
}
