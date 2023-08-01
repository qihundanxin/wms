package com.deer.wms.project.root.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.deer.wms.project.root.validation.config.NotOnlyValidator;

/**
 * 排除NotOnly外，其他属性不为空
 * 
 * @author xuesinuo
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(NotOnly.List.class)
@Documented
@Constraint(validatedBy = { NotOnlyValidator.class })
public @interface NotOnly {
    /**
     * @return 验证不通过时提示
     */
    String message() default "缺少参数";

    /**
     * @return 验证组
     */
    Class<?>[] groups() default {};

    /**
     * @return 严重等级
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * @return 限定的属性
     */
    String[] value();

    /**
     * @return 是否允许本对象为空
     */
    boolean nullable() default true;

    /**
     * @return 是否限定的属性为空
     */
    boolean valueNullable() default true;

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        NotOnly[] value();
    }
}
