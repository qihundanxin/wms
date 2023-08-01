package com.deer.wms.project.root.validation.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.deer.wms.project.root.validation.config.StringEqualsValidator;

/**
 * JSR303 - 要求字符串必须在给定字符串之中
 * 
 * @author xuesinuo 2021-1-27
 *
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(StringEquals.List.class)
@Documented
@Constraint(validatedBy = { StringEqualsValidator.class })
public @interface StringEquals {
    /**
     * @return 验证不通过时提示
     */
    String message() default "";

    /**
     * @return 验证组
     */
    Class<?>[] groups() default {};

    /**
     * @return 严重等级
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * @return 限定字符串只能是其中之一
     */
    String[] value();

    /**
     * @return 是否允许为空
     */
    boolean nullable() default true;

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        StringEquals[] value();
    }
}
