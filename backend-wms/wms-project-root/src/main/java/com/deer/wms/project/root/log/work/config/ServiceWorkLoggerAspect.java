package com.deer.wms.project.root.log.work.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.deer.wms.project.root.constant.AspectOrder;

import lombok.RequiredArgsConstructor;

/**
 * 将@Service纳入记录范围
 * 
 * @author xuesinuo 2022-01-21
 *
 */
@RequiredArgsConstructor
@Configuration
@Aspect
@Order(AspectOrder.LOGGER_SERVICE)
public class ServiceWorkLoggerAspect {
    private final WorkLoggerHandler workLoggerHandler;

    @Around("@within(serviceAnn)")
    public Object around(ProceedingJoinPoint joinPoint, Service serviceAnn) throws Throwable {
        return workLoggerHandler.saveWlDoJpNoAnn(joinPoint, null);
    }
}
