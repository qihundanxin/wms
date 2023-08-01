package com.deer.wms.project.root.sync.config;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.ClassUtils;

import com.deer.wms.project.root.constant.AspectOrder;
import com.deer.wms.project.root.sync.annotation.Synchronized;
import com.deer.wms.project.root.sync.pojo.SyncParam;
import com.deer.wms.project.root.util.AspectUtil;
import com.deer.wms.project.root.util.SpelUtil;
import com.deer.wms.project.root.util.SyncTool;

import lombok.extern.slf4j.Slf4j;

/**
 * 分布式同步锁：<br>
 * 使用注解{@link Synchronized}
 * 
 * @author xuesinuo 2020-06-24 15:12:49
 */
@Aspect
@Configuration
@Order(AspectOrder.SYNC)
@Slf4j
public class SynchronizedAspect {
    private SyncTool syncTool;

    public SynchronizedAspect(SyncTool syncTool) {
        this.syncTool = syncTool;
    }

    @Around("@annotation(synchronizedAnn)")
    public Object around(ProceedingJoinPoint joinPoint, Synchronized synchronizedAnn) throws Throwable {
        // target messages
        Object target = joinPoint.getTarget();
        Method targetMethod = AspectUtil.getMethod(joinPoint);
        // method level lock
        String methodFullName = ClassUtils.getUserClass(target).getName() + "." + targetMethod.getName();
        String lockName;
        if (synchronizedAnn.lockName().equals("")) {
            lockName = methodFullName;
        } else {
            lockName = synchronizedAnn.lockName();
        }
        // param level lock
        String lockParam = null;
        if (!synchronizedAnn.value().equals("")) {
            lockParam = SpelUtil.getSpelValue(target, synchronizedAnn.value(), targetMethod, joinPoint.getArgs());
            if (lockParam == null) {
                log.warn("[@Synchronized]" + methodFullName + " " + lockName + " " + synchronizedAnn.value() + " 解析结果为空。");
                return joinPoint.proceed();
            }
        }

        SyncParam syncParam = new SyncParam(lockName, lockParam);
        syncParam.setTimeout(synchronizedAnn.timeout());
        syncParam.setTimeoutUnit(synchronizedAnn.timeoutUnit());
        syncParam.setLockMode(synchronizedAnn.lockMode());
        try (AutoCloseable closer = syncTool.lock(syncParam)) {
            return joinPoint.proceed();
        }
    }
}
