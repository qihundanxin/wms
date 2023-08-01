package com.deer.wms.project.root.configurer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 推荐标注在Controller类或方法上，自动封装返回结果<br>
 * 默认使用的封装方式为{@link ApiResult}
 * 
 * @author xuesinuo 2020-09-12
 *
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiResultPacking {
    /**
     * @return 是否使用自动封装
     */
    boolean value() default true;
}