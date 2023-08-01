package com.deer.wms.base.system.annotation;

import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @ClassName ExcelImportValid
 * @Description Excel导入字段非空校验
 * @Author luolin
 * @Date 2023/6/27 19:28
 * @Version 1.0
 **/
public class ExcelImportValid {
    /**
     * Excel导入字段校验
     * @param object 校验的JavaBean 其属性须有自定义注解
     */
    public static void valid(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            //设置可访问
            field.setAccessible(true);
            //属性的值
            Object fieldValue = null;
            try {
                fieldValue = field.get(object);
            } catch (IllegalAccessException e) {
                throw new ServiceException(CommonCode.SERVICE_ERROR,field.getAnnotation(ExcelValid.class).message());
            }
            //是否包含必填校验注解
            boolean isExcelValid = field.isAnnotationPresent(ExcelValid.class);
            if (isExcelValid && Objects.isNull(fieldValue)) {
                throw new ServiceException(CommonCode.SERVICE_ERROR,field.getAnnotation(ExcelValid.class).message());
            }

        }
    }
}
