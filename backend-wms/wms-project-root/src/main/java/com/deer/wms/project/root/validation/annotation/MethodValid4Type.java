package com.deer.wms.project.root.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.deer.wms.project.root.validation.config.MethodValidator4Type;

/**
 * 通过target.method指定一个本类方法<br>
 * 用此方法作为JSR303的验证逻辑<br>
 * 
 * @author xuesinuo 2021年1月25日
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MethodValid4Type.List.class)
@Documented
@Constraint(validatedBy = { MethodValidator4Type.class })
public @interface MethodValid4Type {
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
     * use this method do valid:<br>
     * 1.public, no static<br>
     * 2.no param<br>
     * 3.return boolean<br>
     * 
     * Ex:<br>
     * public boolean check(){return this.a != this.b}
     * 
     * @return 目标方法
     */
    String method();

    /**
     * @return 是否允许为空
     */
    boolean nullable() default true;

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        MethodValid4Type[] value();
    }
}
