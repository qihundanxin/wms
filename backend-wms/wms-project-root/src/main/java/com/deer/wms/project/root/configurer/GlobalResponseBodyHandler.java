package com.deer.wms.project.root.configurer;

import java.io.Closeable;
import java.lang.reflect.Method;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.HashMap;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.exception.ServiceException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 1.处理可预期的抛出的代码异常<br>
 * 2.自动封装！
 *
 * @author xuesinuo 2020-09-12
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalResponseBodyHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        ApiResultPacking methodPackingAnn = returnType.getMethodAnnotation(ApiResultPacking.class);
        if (methodPackingAnn != null && methodPackingAnn.value()) {
            return true;
        } else {
            ApiResultPacking classPackingAnn = returnType.getContainingClass().getAnnotation(ApiResultPacking.class);
            if (classPackingAnn != null && classPackingAnn.value()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {
        ApiResultPacking methodPackingAnn = returnType.getMethodAnnotation(ApiResultPacking.class);
        ApiResultPacking classPackingAnn = returnType.getContainingClass().getAnnotation(ApiResultPacking.class);
        ApiResultPacking usingPackingAnn = methodPackingAnn != null ? methodPackingAnn : classPackingAnn;
        if (usingPackingAnn.value()) {
            Result apiResult = new Result();
            apiResult.setCode(CommonCode.SUCCESS);
            apiResult.setData(body);

            Class<?> dataType = returnType.getMethod().getReturnType();
            String badResultTypeName = null;
            if (!Object.class.isAssignableFrom(dataType)) {// 非Object，即基本数据类型
                if (dataType == void.class) {
                    apiResult.setData(new HashMap<>());
                } else {
                    badResultTypeName = dataType.toString();
                }
            } else if (CharSequence.class.isAssignableFrom(dataType)) {// 字符串
                badResultTypeName = "string";
            } else if (Character.class.isAssignableFrom(dataType)) {// 字符
                badResultTypeName = "char";
            } else if (TemporalAccessor.class.isAssignableFrom(dataType)) {// JAVA8 时间存取器
                badResultTypeName = "date";
            } else if (Date.class.isAssignableFrom(dataType)) {// 日期
                badResultTypeName = "date";
            } else if (Number.class.isAssignableFrom(dataType)) {// 数字
                badResultTypeName = "number";
            } else if (Boolean.class.isAssignableFrom(dataType)) {// 布尔
                badResultTypeName = "boolean";
            } else if (Iterable.class.isAssignableFrom(dataType)) {// 集合
                badResultTypeName = "collection(iterable)";
            } else if (dataType.isArray()) {// 数组
                badResultTypeName = "array";
            } else if (Closeable.class.isAssignableFrom(dataType)) {// 可关闭的
                badResultTypeName = "closeable";
            }
            if (badResultTypeName != null) {
                Method method = returnType.getMethod();
                String methodName = returnType.getContainingClass().getName() + "#" + method.getName();
                throw new ServiceException(CommonCode.SYSTEM_ERROR, "Api不支持返回类型：" + badResultTypeName + "  " + methodName);
            }

            return apiResult;
        }
        return body;
    }
}
