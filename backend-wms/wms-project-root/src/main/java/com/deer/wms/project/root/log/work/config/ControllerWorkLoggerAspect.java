package com.deer.wms.project.root.log.work.config;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.constant.AspectOrder;
import com.deer.wms.project.root.log.work.bean.WorkLogBean;
import com.deer.wms.project.root.util.HttpRequestTool;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.project.root.util.SimpleMap;

import lombok.RequiredArgsConstructor;

/**
 * 将Controller和RestController纳入记录范围
 * 
 * @author xuesinuo 2022-01-21
 *
 */
@RequiredArgsConstructor
@Configuration
@Aspect
@Order(AspectOrder.LOGGER_CONTROLLER)
public class ControllerWorkLoggerAspect {
    private final WorkLoggerHandler workLoggerHandler;
    private final HttpServletRequest request;
    private final HttpRequestTool httpRequestTool;

    private Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        WorkLogBean workLogParam = new WorkLogBean();
        // workLogParam.setTraceId(null);
        workLogParam.setWorkType(WorkLogBean.WorkType.HTTP);
        Enumeration<String> requestHeaderNames = request.getHeaderNames();
        SimpleMap headerMap = SimpleMap.init();
        while (requestHeaderNames.hasMoreElements()) {
            String header = requestHeaderNames.nextElement();
            headerMap.append(header, request.getHeader(header));
        }
        SimpleMap fromInfo = SimpleMap.init()
                .append("method", request.getMethod())
                .append("url", request.getRequestURI())
                .append("header", headerMap);
        workLogParam.setFromInfo(fromInfo);
        // workLogParam.setIp(request.getRemoteAddr());
        workLogParam.setIp(Np.i(headerMap.get("cf-connecting-ip")).x(Object::toString).o());// 部署方式，本机Nginx反向代理，所以IP被写在了cf-connecting-ip
        workLogParam.setUserId(httpRequestTool.getUserId());
        return workLoggerHandler.saveWlDoJpNoAnn(joinPoint, workLogParam);
    }

    @Around("@within(controllerAnn)")
    public Object around(ProceedingJoinPoint joinPoint, Controller controllerAnn) throws Throwable {
        return this.around(joinPoint);
    }

    @Around("@within(controllerAnn)")
    public Object around(ProceedingJoinPoint joinPoint, RestController controllerAnn) throws Throwable {
        return this.around(joinPoint);
    }
}