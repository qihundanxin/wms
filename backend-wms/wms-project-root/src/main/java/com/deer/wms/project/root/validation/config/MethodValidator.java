package com.deer.wms.project.root.validation.config;

import java.lang.reflect.Method;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.deer.wms.project.root.validation.annotation.MethodValid;

import lombok.extern.slf4j.Slf4j;

/**
 * {@link MethodValid} 验证规则
 * 
 * @author xuesinuo 2021-1-27
 */
@Slf4j
public class MethodValidator implements ConstraintValidator<MethodValid, Object> {
    private MethodValid ann;

    @Override
    public void initialize(MethodValid methodValid) {
        this.ann = methodValid;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return ann.nullable();
        }
        boolean result = false;
        Class<?> target = ann.target();
        try {
            Method method = target.getMethod(ann.method(), value.getClass());
            Object methodResult = method.invoke(null, value);
            if (methodResult == null) {
                return result;
            }
            if (!(methodResult instanceof Boolean)) {
                return result;
            }
            result = (boolean) methodResult;
        } catch (Exception e) {
            log.error("JSR303-@MethodValid-公式错误", e);
            throw new RuntimeException(e);
        }
        return result;
    }

}
