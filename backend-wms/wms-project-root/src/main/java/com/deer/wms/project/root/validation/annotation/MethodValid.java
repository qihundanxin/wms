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

import com.deer.wms.project.root.validation.config.MethodValidator;

/**
 * 通过target.method指定一个静态方法<br>
 * 用此方法作为JSR303的验证逻辑<br>
 * 
 * @author xuesinuo 2021年1月25日
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(MethodValid.List.class)
@Documented
@Constraint(validatedBy = { MethodValidator.class })
public @interface MethodValid {
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
     * @return 执行判定方法的目标类
     */
    Class<?> target();

    /**
     * 目标方法需要满足：<br>
     * 1.不可出现异常<br>
     * 2.返回boolean<br>
     * 3.Method单参数，参数为验证的目标属性值<br>
     * 4.静态方法<br>
     * 
     * @return 目标方法
     */
    String method();

    /**
     * @return 是否允许为空
     */
    boolean nullable() default true;

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        MethodValid[] value();
    }
}
