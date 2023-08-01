package com.deer.wms.project.root.log.work.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 记录WorkLog<br>
 * 
 * <pre>
 * 标在TYPE上：
 *   记录这个类的全部方法，或配置忽略整个类。
 * 
 * 标在METHOD上：
 *   只记录当前方法，或配置忽略此方法，优先级高于标注在类上。
 *   
 * 标在PARAMETER上：
 *   配置此参数被忽略记录，用于一些敏感参数，比如注册、登录、修改密码时的password字段。
 *   
 * 未标注就被记录了？
 *   参考com.deer.wms.project.root.log.work.config，有一些SpringBean会被默认记录，使用{@link WorkLogger}可以移除。
 * </pre>
 * 
 * @author xuesinuo
 */
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WorkLogger {
    /**
     * @return 是否开启WorkLogger
     */
    boolean value() default true;
}
