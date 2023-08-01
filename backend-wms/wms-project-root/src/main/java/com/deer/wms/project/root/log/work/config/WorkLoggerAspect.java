package com.deer.wms.project.root.log.work.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.deer.wms.project.root.constant.AspectOrder;
import com.deer.wms.project.root.log.work.annotation.WorkLogger;
import com.deer.wms.project.root.util.AspectUtil;

import lombok.RequiredArgsConstructor;

/**
 * 针对{@link WorkLogger}的切面：
 * 
 * <pre>
 * 这是两个切面：Type上的WorkLogger，Method上的WorkLogger。
 * 两个切面分别生效。
 * 当有Method时，Type切面失效。
 * </pre>
 * 
 * @author xuesinuo 2022-01-22
 *
 */
@RequiredArgsConstructor
@Configuration
@Aspect
@Order(AspectOrder.ANNOTATION_LOGGER_CONTROLLER)
public class WorkLoggerAspect {
    private final WorkLoggerHandler workLoggerHandler;

    @Around("@within(workLoggerAnn)")
    public Object aroundType(ProceedingJoinPoint joinPoint, WorkLogger workLoggerAnn) throws Throwable {
        if (!workLoggerAnn.value()) {
            return joinPoint.proceed();
        }
        Method targetMethod = AspectUtil.getMethod(joinPoint);
        for (Annotation m : targetMethod.getAnnotations()) {
            if (m instanceof WorkLogger) {
                return joinPoint.proceed();
            }
        }
        return workLoggerHandler.saveWorkLogAndDoJoinPoint(joinPoint, null);
    }

    @Around("@annotation(workLoggerAnn)")
    public Object aroundMethod(ProceedingJoinPoint joinPoint, WorkLogger workLoggerAnn) throws Throwable {
        if (!workLoggerAnn.value()) {
            return joinPoint.proceed();
        } else {
            return workLoggerHandler.saveWorkLogAndDoJoinPoint(joinPoint, null);
        }
    }
}
