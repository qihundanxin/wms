package com.deer.wms.intercept.annotation;

import java.lang.annotation.*;

/**
 * @Author luolin
 * @Description PDA当前用户数据的注解，作用在方法上，加上该注解将会获取根据请求中的token值获取当前用户的数据，
 * @Date 9:13 2023/7/5
 **/
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PdaUser {
}
