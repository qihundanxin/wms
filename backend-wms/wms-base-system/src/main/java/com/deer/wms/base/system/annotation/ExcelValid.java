package com.deer.wms.base.system.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName ExcelValid
 * @Description 导入Excel数据进行校验的注解
 * @Author dell
 * @Date 2023/6/27 19:26
 * @Version 1.0
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelValid {
    String message() default "导入有未填入的字段";
}
